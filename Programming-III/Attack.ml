open GameState
open Utils


(** Adds neighboring cells to the target list based on the current hit pattern.
    Implements smart targeting strategy depending on ship type and number of hits:
    - For Carrier with 4 hits: Completes the T-shape pattern
    - For Carrier with 3 hits: Identifies the T-shape orientation and targets missing cells
    - For Frigate/Destroyer/Carrier with 2+ hits: Targets cells along the identified line/column
    - For TorpedoBoat or first hits: Adds all 4 adjacent cells (up, down, left, right)
    @param l Line coordinate of the last hit
    @param c Column coordinate of the last hit
  *)
let add_neighbors l c =
  let neighbors = [(l-1, c); (l+1, c); (l, c+1); (l, c-1)] in
  let boat = match state.last_hit_ship with
    | Some b -> b
    | None -> failwith "There's no such boat"
  in
    if (boat = Carrier && List.length state.last_boat_hits = 4) then(
      let hits_tail = List.tl state.last_boat_hits in
      
      state.targets_to_try <- [];
      let (l1, c1) = List.hd hits_tail in
      let (l2, c2) = List.nth hits_tail 1 in
      let (l3, c3) = List.nth hits_tail 2 in

      (* Get all but the last hit *)
      let all_same_line = List.for_all (fun (nl, _) -> nl = l1) hits_tail in
      let all_same_column = List.for_all (fun (_, nc) -> nc = c1) hits_tail in

      if all_same_line then(
        let middle = (c1 + c2 + c3) / 3 in

        if(l > l1) then  
          state.targets_to_try <- [(l + 1, c)]
        else if (l < l1 && middle = c ) then  
          state.targets_to_try <- [(l - 1, c)]
        else if (l < l1 &&  middle <> c ) then  
          state.targets_to_try <- [(l + 2, c)]  
      )
      else if all_same_column then(
        let middle = (l1 + l2 + l3) / 3 in

        if(c > c1) then  
          state.targets_to_try <- [(l, c + 1)]
        else if (c < c1 && middle = l ) then  
          state.targets_to_try <- [(l, c - 1)]
        else if (c < c1 &&  middle <> l ) then  
          state.targets_to_try <- [(l, c + 2)]
      )
    )
    else if (boat = Carrier && List.length state.last_boat_hits = 3) then(
      let all_same_line = List.for_all (fun (nl, _) -> nl = l) state.last_boat_hits in
      let all_same_column = List.for_all (fun (_, nc) -> nc = c) state.last_boat_hits in

      state.targets_to_try <- []; 

      let (l1, c1) = List.hd state.last_boat_hits in
      let (l2, c2) = List.nth state.last_boat_hits 1 in
      let (l3, c3) = List.nth state.last_boat_hits 2 in

      if all_same_line then(
        List.iter (function (nl, nc) ->
          if is_unknown (nl-1) nc then 
            state.targets_to_try <- (nl - 1, nc) :: state.targets_to_try
        ) state.last_boat_hits;
        
      let middle = (c1 + c2 + c3) / 3 in
        if (is_unknown (l + 1) middle && is_unknown (l + 2) middle) then
          state.targets_to_try <- (l + 1, middle) :: state.targets_to_try
      )
      else if all_same_column then (
        List.iter (function (nl, nc) ->
          if is_unknown nl (nc - 1) then 
            state.targets_to_try <- (nl, nc - 1) :: state.targets_to_try
        ) state.last_boat_hits;
        

      let middle = (l1 + l2 + l3) / 3 in
        if (is_unknown middle (c + 1) && is_unknown middle (c + 2)) then 
          state.targets_to_try <- (middle, c + 1) :: state.targets_to_try
      )    
    )

    else if((boat = Frigate || boat = Destroyer || boat = Carrier) && List.length state.last_boat_hits >= 2) then (

      let all_same_line = List.for_all (fun (nl, _) -> nl = l) state.last_boat_hits in
      let all_same_column = List.for_all (fun (_, nc) -> nc = c) state.last_boat_hits in

      if all_same_line then(
        (* Remove all the positions not in the same line *)
        state.targets_to_try <- List.filter (fun (nl, _) -> nl = l) state.targets_to_try;
        (* Add all the positions in the same line at the left and right of the last hit *)
        List.iter (function (nl, nc) ->
          if is_unknown nl nc then
            state.targets_to_try <- (nl, nc) :: state.targets_to_try
        ) [(l, c-1); (l, c+1)]
      )

      else if all_same_column then (
        (* Remove all the positions not in the same column *)
        state.targets_to_try <- List.filter (fun (_, nc) -> nc = c) state.targets_to_try;
        (* Add all the positions in the same column above and below the last hit *)
        List.iter (function (nl, nc) ->
          if is_unknown nl nc then
            state.targets_to_try <- (nl, nc) :: state.targets_to_try
        ) [(l-1, c); (l+1, c)]
      )
    )

    else   (* Torpedo-Boat, or first other boat hits, all directions *)
      List.iter (function (nl, nc) ->
        (* if the cell is unknown and not already in targets_to_try *)
        if is_unknown nl nc && not (List.exists (function coord -> coord = (nl, nc)) state.targets_to_try) then
          state.targets_to_try <- (nl, nc) :: state.targets_to_try
      ) neighbors


(** Marks all cells surrounding the last boat hits as [Water] on the attack board.
    This is called when a ship is sunk to eliminate impossible positions for other ships.
    Updates all neighboring cells of each hit position. *)
let mark_surroundings_as_water () =
  List.iter (fun (l, c) ->
    for dl = -1 to 1 do
      for dc = -1 to 1 do
        let nl = l + dl in
        let nc = c + dc in
          if is_valid nl nc && state.attack_board.(nl).(nc) = Unknown then
          state.attack_board.(nl).(nc) <- Water
      done
    done
  ) state.last_boat_hits


(** Collects all unknown cells on the board and returns a random one.
    Used when the chess pattern is exhausted and there are no targets on the list.
    @return A random coordinate from all [Unknown] cells, or [(0, 0)] if none exist *)
let choose_random_unknown () =
  let rec collect_unknowns l c acc =
    if l >= state.board_size then acc
    else if c >= state.board_size then collect_unknowns (l + 1) 0 acc
    else if is_unknown l c then collect_unknowns l (c + 1) ((l, c) :: acc)
    else collect_unknowns l (c + 1) acc
  in
  match collect_unknowns 0 0 [] with
  | [] -> (0, 0) (* Should never happen *)
  | lst ->
      let idx = Random.int (List.length lst) in
      List.nth lst idx


(** Chooses the next shot coordinate using a priority-based strategy:
    1. Returns next target from [state.targets_to_try] if available (modo "caça" e "destruição")
    2. Uses chess pattern (odd cells: where l + c is odd) for initial exploration
    3. Goes back to random unknown cell if chess pattern is exhausted
    @return Coordinate (line, column) for the next shot *)
let choose_shot () =
  match state.targets_to_try with
  | (l, c) :: rest ->
      state.targets_to_try <- rest;
      (l, c)
  | [] ->
      (* find next shot using chess odd pattern*)
      let rec find_cell l c =
        if l >= state.board_size then
          (* Chess pattern exausted, random Unknown pick *)
          choose_random_unknown ()
        else if c >= state.board_size then 
          find_cell (l+1) 0
        else if (l + c) mod 2 <> 0 && is_unknown l c then(
          state.last_chess_shot <- (l, c);
          (l, c)
        )
        else find_cell l (c+1)
      in
        let (sl, sc) = state.last_chess_shot in
          find_cell sl sc

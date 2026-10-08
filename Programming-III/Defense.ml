open GameState
open Utils


(** List of information about each boat type in the game.
    Contains tuples of (quantity, boat_type, size) for each ship type. *)
let boats_info = [
  (* (quantity, ship, size) *)
  (1, Carrier, 5);
  (1, Destroyer, 4);
  (2, Frigate, 3);
  (3, TorpedoBoat, 2);
  (1, Submarine, 1);
]

(** Marks the surrounding cells of a ship part as [CantPlaceHere] to prevent placing other ships adjacent to it.
    @param l Line coordinate of the ship part
    @param c Column coordinate of the ship part *)
let restrict_surroundings l c =
  for dl = -1 to 1 do
    for dc = -1 to 1 do
      let nl = l + dl in
      let nc = c + dc in
        if is_valid nl nc && state.defense_board.(nl).(nc) = Water then
        state.defense_board.(nl).(nc) <- CantPlaceHere
    done
  done


(** Places a ship on the defense board at the given coordinates if all cells are available.
    Updates the defense board, marks surrounding cells as restricted, and adds the ship to the game state.
    @param ship_type Type of the ship to place
    @param coords List of coordinates where the ship will be placed
    @return [true] if the ship was placed successfully, [false] if any cell is occupied or invalid *)
let place_ship ship_type coords =
  if List.for_all (function (nl, nc) -> 
      is_valid nl nc && state.defense_board.(nl).(nc) = Water
    ) coords then (
      List.iter (fun (l, c) -> 
        state.defense_board.(l).(c) <- Boat ship_type;
        restrict_surroundings l c
      ) coords;
      let ship = { 
      name = ship_type; 
      cells = coords; 
      remaining = List.length coords 
    } in
    state.ships <- ship :: state.ships;
    state.remaining_ships <- state.remaining_ships + 1;
    true
  )
  else
    false


(** Predefined custom board configuration for 8x8 board.
    Contains ship placements with specific coordinates. *)
let custom_board1 = [
  (Submarine, [(0,7)]);
  (TorpedoBoat, [(2,2);(3,2)]); 
  (TorpedoBoat, [(1,4);(2,4)]); 
  (TorpedoBoat, [(2,6);(2,7)]); 
  (Frigate, [(0,0);(1,0);(2,0)]); 
  (Frigate, [(5,5);(6,5);(7,5)]); 
  (Destroyer, [(4,7);(5,7);(6,7);(7,7)]);
  (Carrier, [(5,1);(6,1);(7,1);(6,2);(6,3)]);
]


(** Applies a custom board configuration by placing all ships at their specified coordinates.
    @param board List of tuples containing (ship_type, coordinates) *)
let apply_custom_board board =
  List.iter (
    function (ship_type, coords) -> 
      ignore (place_ship ship_type coords)
  ) board


(** Attempts to place a specified quantity of ships randomly on the defense board.
    Ships are placed either vertically or horizontally. If placement fails after
    all attempts, returns [false].
    @param qty Quantity of ships of this type to place
    @param ship_type Type of the ship to place
    @param size Size of the ship (number of cells)
    @param n_attempts Number of attempts remaining to place the ships
    @return [true] if all ships were placed successfully, [false] if attempts exhausted *)
let rec try_to_place_ship qty ship_type size n_attempts =
    if qty = 0 then true
    else if n_attempts = 0 then false
    else
      (* orientation = true -> vertical, false -> horizontal *)
      let orientation = Random.bool () in
      (* To guarantee the ship will not go out of the board bounds:
          - if vertical, line can go up to board_size - size + 1
          - if horizontal, column can go up to board_size - size + 1 *)
      let l = Random.int (state.board_size - (if orientation then size - 1 else 0)) in
      let c = Random.int (state.board_size - (if orientation then 0 else size - 1)) in
      let coords = 
        if orientation then
          List.init size (fun i -> (l + i, c))
        else
          List.init size (fun i -> (l, c + i))
      in
        if(place_ship ship_type coords) then
          (* try to place the ship 2*(board_size^2) times *)
          try_to_place_ship (qty - 1) ship_type size (state.board_size*state.board_size*2)
        else
          try_to_place_ship qty ship_type size (n_attempts-1)


(** Attempts to place Carrier ships in a T-shaped configuration on the defense board.
    The T shape can be oriented in 4 different rotations. If placement fails after
    all attempts, returns [false].
    @param qty Quantity of Carrier ships to place
    @param size Size of the Carrier ship (always 5 cells)
    @param n_attempts Number of attempts remaining to place the carriers
    @return [true] if all carriers were placed successfully, [false] if attempts exhausted *)
let rec try_to_place_carrier qty size n_attempts =
  if qty = 0 then true
  else if n_attempts = 0 then false
  else
    let orientation = Random.int (4) in
    let coords = 
      (* Considering the bottom of the T shape as baseline for the random pick *)
      if orientation = 0 then (* Up Regular T)*)
        let l = Random.int (state.board_size - 2) + 2 in
        let c = Random.int (state.board_size - 2) + 1 in
        [(l, c); (l-1, c); (l-2, c); (l-2, c-1); (l-2, c+1)]
      else if orientation = 1 then (* Upside-Down T*)
        let l = Random.int (state.board_size - 2) + 0 in
        let c = Random.int (state.board_size - 2) + 1 in
        [(l, c); (l+1, c); (l+2, c); (l+2, c-1); (l+2, c+1)]
      else if orientation = 2 then (* Left-Rotated T, |-*)
        let l = Random.int (state.board_size - 2) + 1 in
        let c = Random.int (state.board_size - 2) + 2 in
        [(l, c); (l, c-1); (l, c-2); (l+1, c-2); (l-1, c-2)]
      else (* Right-Rotated T, -| *)
        let l = Random.int (state.board_size - 2) + 1 in
        let c = Random.int (state.board_size - 2) + 0 in
        [(l, c); (l, c+1); (l, c+2); (l+1, c+2); (l-1, c+2)]
    in
      if(place_ship Carrier coords) then
        try_to_place_carrier (qty - 1) size (state.board_size*state.board_size*2)
      else
        try_to_place_carrier qty size (n_attempts-1)

        
(** Generates a random board configuration by placing all ships from the given list.
    If any ship type fails to place after all attempts, resets the board and retries
    from the beginning with all ships.
    @param ship_list List of tuples containing (quantity, ship_type, size) for each ship type *)
let rec generate_random_board ship_list =
  match ship_list with
  | [] -> ()
  | (qty, Carrier, size) :: rest ->
    if try_to_place_carrier qty size (state.board_size*state.board_size*2) then
      generate_random_board rest
    else (
      state.defense_board <- create_board state.board_size Water;
      state.ships <- [];
      state.remaining_ships <- 0;
      generate_random_board boats_info
    )
  | (qty, ship_type, size) :: rest ->
    if try_to_place_ship qty ship_type size (state.board_size*state.board_size*2) then
      generate_random_board rest
    else (
      state.defense_board <- create_board state.board_size Water;
      state.ships <- [];
      state.remaining_ships <- 0;
      generate_random_board boats_info
    )

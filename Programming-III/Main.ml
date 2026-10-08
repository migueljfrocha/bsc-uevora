open GameState
open Utils
open Attack
open Defense


(** Executes the agent's turn in the game.
    Chooses a shot coordinate, sends it to the opponent, and processes the response.
    Updates the attack board and targeting strategy based on the result.
    - If hit: Updates board, adds neighbors to target list
    - If sunk: Clears targets, marks surroundings as water, resets hit tracking
    - If water: Updates attack board cell
    - If opponent lost: Exits the program *)
let my_turn () =
  let (l, c) = choose_shot () in
    print_endline ("tiro " ^ string_of_int l ^ " " ^ string_of_int c);
    flush stdout;
    let response = read_line () in
    let parts = String.split_on_char ' ' response in
      match parts with
      | "água" :: _ ->
          state.attack_board.(l).(c) <- Water
      | "tiro":: ship_name :: _ ->
          let ship_type = string_to_boat_type ship_name in
          state.attack_board.(l).(c) <- Hit ship_type;
          state.last_hit_ship <- Some ship_type;
          state.last_boat_hits <- (l,c) :: state.last_boat_hits;
          add_neighbors l c
      | "afundado" :: ship_name :: _ ->
          let ship_type = string_to_boat_type ship_name in
          state.attack_board.(l).(c) <- Hit ship_type;
          state.targets_to_try <- [];
          state.last_hit_ship <- None;
          state.last_boat_hits <- (l,c) :: state.last_boat_hits;
          mark_surroundings_as_water ();
          state.last_boat_hits <- [];
      | "perdi" :: _ ->
          exit 0
      | _ -> ()


(** Processes the opponent's turn in the game.
    Reads the opponent's shot, checks the defense board, and sends appropriate response.
    Updates ship state if hit and checks for sunk ships and game over conditions.
    Responses:
    - "água" if the shot missed
    - "tiro <ship_name>" if the shot hit a ship
    - "afundado <ship_name>" if the ship was sunk
    - "perdi" if all ships are sunk (game over) *)
let opponent_turn () =
  let line = read_line () in
  let parts = String.split_on_char ' ' line in
  match parts with
  | "tiro" :: l_s :: c_s :: _ ->
      let l = int_of_string l_s in
      let c = int_of_string c_s in
      (match state.defense_board.(l).(c) with
       | Boat ship_type ->
          state.defense_board.(l).(c) <- HasBeenHit ship_type;

          let ship = List.find (function s -> 
            s.name = ship_type && (List.exists (function coord -> coord = (l,c)) s.cells)
          ) state.ships in

            ship.remaining <- ship.remaining - 1;
            if ship.remaining = 0 then 
              (
                state.remaining_ships <- state.remaining_ships - 1;
                if state.remaining_ships = 0 then 
                (
                  print_endline "perdi";
                  flush stdout;
                  exit 0
                )
                else
                  print_endline ("afundado " ^ boat_type_to_string ship_type);
                  flush stdout;
              )
            else
             print_endline ("tiro " ^ boat_type_to_string ship_type);
             flush stdout;
        | _ ->
          print_endline "água";
          flush stdout;
      )
  | _ -> ()


(** Main game loop that alternates turns between the agent and the opponent.
    @param my_turn_flag [true] if it's the agent's turn, [false] if it's the opponent's turn *)
let rec game_loop my_turn_flag =
  if my_turn_flag then (
    my_turn ();
    game_loop false
  )else(
    opponent_turn ();
    game_loop true
  )


(** Handles the initial game configuration phase.
    Reads and processes configuration commands:
    - "init <n>" - Initializes boards with size n x n
    - "barco <name> <positions>" - Places a ship at specified coordinates
    - "random" - Generates random board (uses custom board for 8x8, random otherwise)
    - "vou eu" - Starts game with agent going first
    - "vai tu" - Starts game with opponent going first *)
let rec config () =
  let line = read_line () in
  let parts = String.split_on_char ' ' line in 
  match parts with
  | ["init"; n_s] | ["init"; n_s; _] -> 
      let n = int_of_string n_s in
      state.board_size <- n;
      state.defense_board <- create_board n Water;
      state.attack_board <- create_board n Unknown;
      config ()
  | "barco" :: name :: positions -> 
      let ship = string_to_boat_type name in
      let coords = parse_positions positions in
      ignore (place_ship ship coords);
      config ()

  | ["random"] -> 
      if state.board_size = 8 then
        apply_custom_board custom_board1
      else 
        generate_random_board boats_info;
      config ()
  | ["vou"; "eu"] -> 
      game_loop true
  | ["vai"; "tu"] -> 
      game_loop false
  | _ -> 
      config ()


(** Main entry point of the program.
    Initializes the random number generator, creates default boards,
    and starts the configuration phase. *)
let () = 
  Random.self_init ();
  state.defense_board <- create_board state.board_size Water;
  state.attack_board <- create_board state.board_size Unknown;
  config ()
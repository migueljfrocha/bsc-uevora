open GameState

(** Conversion function between boat_type and string 
    @param bt boat_type to be converted
    @return string representation of the boat_type
*)
let boat_type_to_string = function
  | Carrier -> "porta-avioes"
  | Destroyer -> "destroyer"
  | Frigate -> "fragata"
  | TorpedoBoat -> "torpedeiro"
  | Submarine -> "submarino"


(** Conversion function between string and boat_type 
    @param s string representation of the boat_type
    @return boat_type representation of the string
*)
let string_to_boat_type = function
  | "porta-aviões" | "Porta-aviões" | "Porta-Aviões" 
  | "porta-avioes" | "Porta-avioes" | "Porta-Avioes" -> Carrier
  | "destroyer" | "Destroyer" -> Destroyer
  | "fragata" | "Fragata" -> Frigate
  | "torpedeiro" | "Torpedeiro" -> TorpedoBoat
  | "submarino" | "Submarino" -> Submarine
  | _ -> failwith "Tipo de barco desconhecido"


(** Checks if the coordinates are within the board boundaries 
    @param l line coordinate
    @param c column coordinate
    @return true if the coordinates are valid, false otherwise  
*)
let is_valid l c =
  l >= 0 && l < state.board_size && c >= 0 && c < state.board_size


(** Checks if the cell at the given coordinates is unknown on the attack board 
    @param l line coordinate
    @param c column coordinate
    @return true if the cell is unknown, false otherwise  
*)
let is_unknown l c =
  is_valid l c && state.attack_board.(l).(c) = Unknown


(** Initializes a board with the given size and initial value 
    @param n size of the board (n x n)
    @param initial_value initial value to fill the board
    @return a 2D array representing the board
*)
let create_board n initial_value = 
  Array.make_matrix n n initial_value


(** Parses a list of strings into a list of coordinate tuples
    @param lst list of strings representing coordinates
    @return list of coordinate tuples (int * int)
*)
let rec parse_positions = function
  | l_s :: c_s :: rest -> 
      let l = int_of_string l_s in
      let c = int_of_string c_s in
      (l, c) :: parse_positions rest
  | _ -> []

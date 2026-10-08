
(** Boat Types for the game *)
type boat_type =
  | Carrier
  | Destroyer
  | Frigate
  | TorpedoBoat
  | Submarine


(** Cell Types for the game board *)
type board_cell = 
  | Unknown                   (* Ataque: Ainda não foi atacado *)
  | Hit of boat_type          (* Ataque: Acerto em um barco *)
  | Boat of boat_type         (* Defesa: Barco aqui *)
  | HasBeenHit of boat_type   (* Defesa: Célula já atacada *)
  | CantPlaceHere             (* Defesa: Não pode colocar barco aqui *)
  | Water                     (* Ataque/Defesa: Água, não tem barco *)


(** Coordinates Tuple *)
type coord = int * int


(** Ship state for the game *)
type ship_state = {
  name: boat_type;
  cells: coord list;
  mutable remaining: int;
}

(** Game State Declaration *)
type state = {
  mutable board_size: int;
  mutable defense_board: board_cell array array;
  mutable attack_board: board_cell array array;
  
  mutable ships: ship_state list;
  mutable remaining_ships: int;

  mutable targets_to_try: (int * int) list;
  mutable last_hit_ship: boat_type option;
  mutable last_boat_hits: (int * int) list;
  mutable last_chess_shot: (int * int);
}

(** Global State Used *)
let state = {
  board_size = 8;
  defense_board = [||];
  attack_board = [||];
  ships = [];
  remaining_ships = 0;
  targets_to_try = [];
  last_hit_ship = None;
  last_boat_hits = [];
  last_chess_shot = (0, 0);
}
% Estado (NpecasP1, NpecasP2, Turno do jogador)
% estado_inicial(e(10, 11, a)).

%%%%%%%%%%%%%%%%

estado_inicial(e(15, 10, a)).
% estado_inicial(e(9, 14, a)).
% estado_inicial(e(12, 10, a)).
% estado_inicial(e(11, 8, a)).
% estado_inicial(e(8, 8, a)).
% estado_inicial(e(5, 10, a)). 
% estado_inicial(e(6, 7, a)).
% estado_inicial(e(5, 8, a)).
% estado_inicial(e(7, 5, a)).
% estado_inicial(e(6, 6, a)).
% estado_inicial(e(7, 4, a)).
% estado_inicial(e(4, 4, a)).
% estado_inicial(e(3, 5, a)).
% estado_inicial(e(2, 2, a)).
% estado_inicial(e(2, 1, a)).
% estado_inicial(e(1, 1, a)). 

%%%%%%%%%%%%%%%%

inv(a, b).
inv(b, a).


op1(e(P1, P2, J), retiraP12(X), e(P1A, P2A, JS)) :- 
    min(Max, P1, P2),
    between(1, Max, X),
    P1A is P1 - X,
    P2A is P2 - X,
    inv(J, JS).

op1(e(P1, P2, J), retiraP1(X), e(P1A, P2, JS)) :- 
    between(1, P1, X),
    P1A is P1 - X,
    inv(J, JS).

op1(e(P1, P2, J), retiraP2(X), e(P1, P2A, JS)) :- 
    between(1, P2, X),
    P2A is P2 - X,
    inv(J, JS).


min(A,A,B):- A < B,!.
min(B,_,B).

terminal(e(0, 0, _)).


valor(e(0, 0, b), V, P) :- 
    V is 50 - P.
valor(e(0, 0, a), V, P) :-
    V is -50 + P.


avalia(e(X, X, a), 1).
avalia(e(0, _, a), 1).
avalia(e(_, 0, a), 1).

avalia(e(X, X, b), -1).
avalia(e(0, _, b), -1).
avalia(e(_, 0, b), -1).

avalia(e(X, Y, _), 0):-
    X > 0,
    Y > 0,
    X \= Y.
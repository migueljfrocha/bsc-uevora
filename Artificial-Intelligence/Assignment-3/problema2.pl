
%%%%%%%%%%%%

% DESCOMENTAR UM DOS ESTADOS INICIAIS ABAIXO PARA TESTAR O JOGO

estado_inicial(e([s,s,s,v,v,v,r,r,r], s)).

% estado_inicial(e([s,s,v,s,v,r,v,r,r], s)).

% estado_inicial(e([s,v,s,s,r,v,v,r,r], s)).
% estado_inicial(e([s,v,s,s,v,r,r,v,r], s)).
% estado_inicial(e([s,s,v,r,s,v,v,r,r], s)).
% estado_inicial(e([s,s,v,v,s,r,r,v,r], s)).

% estado_inicial(e([v,s,s,s,r,v,r,v,r], s)).
% estado_inicial(e([v,s,s,s,v,r,r,r,v], s)).
% estado_inicial(e([s,v,s,r,s,v,r,v,r], s)).
% estado_inicial(e([s,v,s,v,s,r,r,r,v], s)).
% estado_inicial(e([s,r,s,v,s,v,v,r,r], s)).
% estado_inicial(e([s,s,r,v,v,s,v,r,r], s)).
% estado_inicial(e([s,s,v,r,v,s,r,v,r], s)).
% estado_inicial(e([s,v,s,r,s,v,r,u,r], s)).

%%%%%%%%%%%%%

% op1(EstadoAtual, Operador, NovoEstado)

% sapo anda uma casa para a direita
op1(e(L1, s), anda_s(O, D), e(L2, r)) :-
    append(A, [s, v | T], L1),
    length(A, N),
    O is N + 1,
    D is N + 2,
    append(A, [v, s | T], L2).

% sapo salta por cima de uma rã para a direita
op1(e(L1, s), salta_s(O, D), e(L2, r)) :-
    append(A, [s, r, v | T], L1),
    length(A, N),
    O is N + 1,
    D is N + 3,
    append(A, [v, r, s | T], L2).

% rã anda uma casa para a esquerda
op1(e(L1, r), anda_r(O, D), e(L2, s)) :-
    append(A, [v, r | T], L1),
    length(A, N),
    O is N + 2,
    D is N + 1,
    append(A, [r, v | T], L2).

% rã salta por cima de um sapo para a esquerda
op1(e(L1, r), salta_r(O, D), e(L2, s)) :-
    append(A, [v, s, r | T], L1),
    length(A, N),
    O is N + 3,
    D is N + 1,
    append(A, [r, s, v | T], L2).

% um estado é terminal quando não tem movimentos possíveis
terminal(E) :-
    \+ op1(E, _, _).

valor(e(_, r), V, P):-
    V is 50 - P.
    
valor(e(_, s), V, P):-
    V is -50 + P.

avalia(e(T, _), Valor) :-
    mov_restantes(T, s, MovS),
    mov_restantes(T, r, MovR),
    Valor is MovS - MovR.


mov_restantes(T, J, NMov) :-
    findall(Mov, op1(e(T, J), Mov, _), ListaMovimentos),
    length(ListaMovimentos, NMov).

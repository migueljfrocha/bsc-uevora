estado_inicial(e([
    v(c(1,1), [1, 2, 3, 4], _),
    v(c(1,2), [1, 2, 3, 4], _),
    v(c(1,3), [1, 2, 3, 4], _),
    v(c(1,4), [1, 2, 3, 4], _),

    v(c(2,1), [1, 2, 3, 4], _),
    v(c(2,2), [1, 2, 3, 4], _),
    v(c(2,3), [1, 2, 3, 4], _),
    v(c(2,4), [1, 2, 3, 4], _),

    v(c(3,1), [1, 2, 3, 4], _),
    v(c(3,2), [1, 2, 3, 4], _),
    v(c(3,3), [1, 2, 3, 4], _),
    v(c(3,4), [1, 2, 3, 4], _),

    v(c(4,1), [1, 2, 3, 4], _),
    v(c(4,2), [1, 2, 3, 4], _),
    v(c(4,3), [1, 2, 3, 4], _),
    v(c(4,4), [1, 2, 3, 4], _)
    ], [])).

% Representação das desigualdades
% Lidas de esquerda para direita e de cima para baixo

d(c(1, 3), menor, c(2, 3)).
d(c(1, 4), menor, c(2, 4)).
d(c(2, 2), maior, c(2, 3)).
d(c(2, 3), maior, c(2, 4)).
d(c(3, 4), maior, c(4, 4)).


sucessor(e([v(P, D, _)|R], Li), e(R, [v(P, D, V)|Li])) :-
    member(V, D).


%%%%%% Verifica Restrições

ve_restricoes(e(_, Li)) :-
    ver_desigualdades(Li),
    ver_lista(Li).

ver_desigualdades(Li) :-
    \+ (
        d(P1, maior, P2),
        member(v(P1, _, V1), Li),
        member(v(P2, _, V2), Li),
        V1 =< V2
    ),
    \+ (
        d(P1, menor, P2),
        member(v(P1, _, V1), Li),
        member(v(P2, _, V2), Li),
        V1 >= V2
    ).
    

ver_lista([]).

ver_lista([X | R]) :-
    \+ conflito(X, R),
    ver_lista(R).

conflito(_, []) :- fail.

conflito(v(c(L, C),_, V), [v(c(L1, C1), _, V)|_]) :-
    (L == L1; C == C1), !.

conflito(X, [_|R]) :-
    conflito(X, R).


%%%%%% Forward checking

sucessorMRV(e(Lni, Li), e(R, [v(P, D, V)|Li])) :-
    escolhe_mrv(Lni, v(P, D, _), R),
    member(V, D).


escolhe_mrv(Lni, Var, R) :-
    dominio_mrv(Lni, Var),
    delete(Lni, Var, R).


dominio_mrv([v(P, D, V)| RLni], Var) :-
    length(D, Len),
    dominio_mrv(RLni, Len, v(P, D, V), Var).

dominio_mrv([], _, Var, Var).

dominio_mrv([v(P, D, V)| RLni], MenorLen, _, VarMRV) :-
    length(D, Len),
    Len < MenorLen, !,
    dominio_mrv(RLni, Len, v(P, D, V), VarMRV).

dominio_mrv([_| RLni], Len, Var, VarMRV) :-
    dominio_mrv(RLni, Len, Var, VarMRV).


forCheck(e(Lni,[v(c(L,C),D,V)|Li]), e(Lniii,[v(c(L,C),D,V)|Li])):- 
    cortaDesigual(V, L, C, Lni, Lnii),
    corta(V, L, C, Lnii, Lniii).


cortaDesigual(_ ,_ , _, [], []).

% menor, cortar maiores
cortaDesigual(V, L, C, [v(c(X,Y),D,_)|Li], [v(c(X,Y),D1,_)|Lii]):-
    (d(c(L,C), menor, c(X,Y)) ; d(c(X,Y), maior, c(L,C)) ),
    retira_menores(V, D, D1), !,
    cortaDesigual(V, L, C, Li, Lii).

% maior, cortar menores
cortaDesigual(V, L, C, [v(c(X,Y),D,_)|Li], [v(c(X,Y),D1,_)|Lii]):-
    (d(c(L,C), maior, c(X,Y)) ; d(c(X,Y), menor, c(L,C)) ),
    retira_maiores(V, D, D1), !, 
    cortaDesigual(V, L, C, Li, Lii).

cortaDesigual(V,L,C,[N|Li],[N|Lii]) :-
    cortaDesigual(V,L,C,Li,Lii).

retira_maiores(_, [], []).

retira_maiores(V, [H|T], T1):- 
    H > V, !, 
    retira_maiores(V, T, T1).

retira_maiores(V, [H|T], [H|T1]):- 
    retira_maiores(V, T, T1).


retira_menores(_, [], []).

retira_menores(V, [H|T], T1):- 
    H < V, !, 
    retira_menores(V, T, T1).

retira_menores(V, [H|T], [H|T1]):- 
    retira_menores(V, T, T1).


corta(_ ,_ , _, [], []).

% Mesma Linha
corta(V, L, C, [v(c(L,N),D,_)|Li], [v(c(L,N),D1,_)|Lii]):- 
    !,
    delete(D,V,D1),
    corta(V,L,C,Li,Lii).

% Mesma coluna
corta(V, L, C, [v(c(N,C),D,_)|Li], [v(c(N,C),D1,_)|Lii]):- 
    !,
    delete(D,V,D1),
    corta(V,L,C,Li,Lii).

% Linhas e colunas diferentes
corta(V, L, C, [N|Li], [N|Lii]):- 
    corta(V, L, C, Li,Lii).


%%%%%% Escreve

esc(Li) :-
    sort(Li, LOrd),
    nl,
    esc1(LOrd, 4).

esc1([], _).

esc1([v(c(_, C), _, V)|R], C) :-
    !,
    write(V),
    write(' '),
    nl,
    esc1(R, C).

esc1([v(c(_, _), _, V)|R], N) :-
    write(V),
    write(' '),
    esc1(R, N).
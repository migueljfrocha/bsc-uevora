:- dynamic(nos/1).

nos(0).

inc:- 
    retract(nos(N)), 
    N1 is N+1, 
    asserta(nos(N1)).

p:- 
    estado_inicial(E0), 
    inc, 
    back(E0,A), 
    esc(A).

p_todas :-
    estado_inicial(E0),
    inc,
    back(E0, _),
    fail.

back(e([],A),A) :- !.

back(E,Sol):-
    sucessorMRV(E,E1),
    inc,
    ve_restricoes(E1),
    forCheck(E1, E2),
    back(E2,Sol).
% Minimax

:- dynamic(nos/1).
:- dynamic(memoria/1).
:- dynamic(ativos/1).
:- dynamic(expandidos/1).

ativos(0).
memoria(0).
nos(0).
expandidos(0).

limpa :-
    retractall(nos(_)),
    retractall(memoria(_)),
    retractall(ativos(_)),
    retractall(expandidos(_)),
    asserta(nos(0)),
    asserta(memoria(0)),
    asserta(ativos(0)),
    asserta(expandidos(0)).


inc :-
    retract(nos(N)),
    N1 is N + 1,
    asserta(nos(N1)).

incE :-
    retract(expandidos(N)),
    N1 is N+1,
    asserta(expandidos(N1)).

actmem(N) :-
    memoria(M),
    M >= N, !.

actmem(N) :-
    retract(memoria(_)),
    asserta(memoria(N)).

entra :-
    retract(ativos(N)),
    N1 is N + 1,
    asserta(ativos(N1)),
    actmem(N1).

sai :-
    retract(ativos(N)),
    N1 is N - 1,
    asserta(ativos(N1)).


g(Jogo):- 
    [Jogo], 
    limpa,
    estado_inicial(Ei), 
    minimax_decidir(Ei,Op),
    write(Op), nl,
    
    nos(N),
    memoria(M),
    expandidos(E),

    write('Nos visitados: '),
    write(N), nl,

    write('Memoria maxima: '),
    write(M), nl,

    write('Nos expandidos: '),
    write(E), nl.

% decide qual é a melhor jogada num estado do jogo
% minimax_decidir(Estado, MelhorJogada)

% se é estado terminal não há jogada 
minimax_decidir(Ei,terminou):- 
    terminal(Ei),!,
    entra,
    inc,
    sai.

%Para cada estado sucessor de Ei calcula o valor minimax do estado
%Opf é o operador (jogada) que tem maior valor

minimax_decidir(Ei,Opf):- 
    entra,
    inc,
    incE,
    findall(Es-Op, op1(Ei,Op,Es),L),
    findall(Vc-Op,(member(E-Op,L), minimax_valor(E,Vc,1)),L1),
    escolhe_max(L1,Opf),
    sai.

% se um estado é terminal o valor é dado pela função de utilidade
minimax_valor(Ei,Val,P):-
    terminal(Ei), !,
    entra,
    inc,
    valor(Ei,Val,P),
    sai.

%Se o estado não é terminal o valor é:
% -se a profundidade é par, o maior valor dos sucessores de Ei
% -se aprofundidade é impar o menor valor dos sucessores de Ei
minimax_valor(Ei,Val,P):- 
    entra,
    inc,
    incE,
    findall(Es,op1(Ei,_,Es),L),
    P1 is P+1,
    findall(Val1,(member(E,L),minimax_valor(E,Val1,P1)),V),
    seleciona_valor(V,P,Val),
    sai.


% Se a profundidade (P) é par, retorna em Val o maximo de V
seleciona_valor(V,P,Val):- 
    X is P mod 2, 
    X=0,!, 
    maximo(V,Val).

% Senão retorna em Val o minimo de V
seleciona_valor(V,_,Val):- 
    minimo(V,Val).


maximo([A|R],Val):- 
    maximo(R,A,Val).

maximo([],A,A).
maximo([A|R],X,Val):- A < X,!, maximo(R,X,Val).
maximo([A|R],_,Val):- 
    maximo(R,A,Val).


escolhe_max([A|R],Val):- 
    escolhe_max(R,A,Val).

escolhe_max([],_-Op,Op).
escolhe_max([A-_|R],X-Op,Val):- A < X,!, escolhe_max(R,X-Op,Val).
escolhe_max([A|R],_,Val):- 
    escolhe_max(R,A,Val).


minimo([A|R],Val):- 
    minimo(R,A,Val).

minimo([],A,A).
minimo([A|R],X,Val):- A > X,!, minimo(R,X,Val).
minimo([A|R],_,Val):-
    minimo(R,A,Val).
% Minimax com corte alfa-beta

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
    write(Op),nl, 
    
    nos(N),
    memoria(M),
    expandidos(E),

    write('Nos visitados: '),
    write(N), nl,

    write('Memoria maxima: '),
    write(M), nl,
    
    write('Nos expandidos: '),
    write(E), nl.

% Menores e maiores valores para as comparações de alfa-beta
limite_min(-10000).
limite_max(10000).

% decide qual é a melhor jogada num estado do jogo
% minimax_decidir(Estado, MelhorJogada)

% se é estado terminal não há jogada 
minimax_decidir(Ei,terminou):- 
    terminal(Ei), !,
    entra,
    inc,
    sai.

% Escolhe a jogada com maior valor usando alfa-beta
minimax_decidir(Ei,Opf):- 
    entra,
    inc,
    incE,
    findall(Es-Op, op1(Ei, Op, Es), L),
    limite_min(A),
    limite_max(B),
    escolhe_max_ab(L, A, B, Opf, _), 
    sai, !.


% Se um estado é terminal o valor é dado pela função de utilidade
minimax_valor(Ei,Val,P,_,_):- 
    terminal(Ei), !,
    entra,
    inc,
    valor(Ei,Val,P),
    sai.

% Se o estado não é terminal o valor é:
% - se a profundidade é par, o maior valor de alfa dos sucessores de Ei
% - se aprofundidade é impar o menor valor do beta dos sucessores de Ei
minimax_valor(Ei,Val,P,A,B):- 
    entra,
    inc,
    incE,
    findall(Es,op1(Ei,_,Es),L),
    P1 is P+1,
    seleciona_valor(L,P,P1,A,B,Val),
    sai.



% Se a profundidade (P) é par, retorna em Val o máximo de L usando alfa-beta
seleciona_valor(L,P,P1,A,B,Val):- 
    X is P mod 2, 
    X=0,!, 
    max_ab(L,P1,A,B,Val).

% Senão retorna em Val o minimo de L usando alfa-beta
seleciona_valor(L,_,P1,A,B,Val):- 
    min_ab(L,P1,A,B,Val).



% Escolha da melhor jogada para a raiz (nó MAX)
escolhe_max_ab([E-Op|R],A,B,Opf,Valf):-
    minimax_valor(E,V,1,A,B),
    maior(A1,V,A),
    escolhe_max_ab(R,A1,B,Op,V,Opf,Valf).

% Não há mais sucessores
escolhe_max_ab([],_,_,Op,Val,Op,Val).

% O valor de alfa é maior ou igual ao valor de beta, cortar a pesquisa
escolhe_max_ab(_,A,B,Op,Val,Op,Val):-
    A >= B, !.

% Cálculo dos restantes sucessores
escolhe_max_ab([E-Op|R],A,B,MelhorOp,MelhorVal,Opf,Valf):-
    minimax_valor(E,V,1,A,B),
    atualiza_max(V,Op,MelhorVal,MelhorOp,NovoVal,NovoOp),
    maior(A1,V,A),
    escolhe_max_ab(R,A1,B,NovoOp,NovoVal,Opf,Valf).


% Atualiza o melhor valor e a melhor jogada
atualiza_max(V,Op,MelhorVal,_,V,Op):-
    V > MelhorVal, !.

% Mantém o melhor valor e a melhor jogada
atualiza_max(_,_,MelhorVal,MelhorOp,MelhorVal,MelhorOp).



% Cálculo do nó MAX
% Cáluclo do primeiro nó 
max_ab([E|R],P,A,B,Val):-
    minimax_valor(E,V,P,A,B),
    maior(A1,V,A),
    max_ab(R,P,A1,B,V,Val).

% Não há mais sucessores
max_ab([],_,_,_,Val,Val).

% O valor de alfa é maior ou igual ao valor de beta, cortar a pesquisa
max_ab(_,_,A,B,Val,Val):-
    A >= B, !.

% Cálculo dos restantes nós MAX
max_ab([E|R],P,A,B,Atual,Val):-
    minimax_valor(E,V,P,A,B),
    maior(Atual1,V,Atual),
    maior(A1,V,A),
    max_ab(R,P,A1,B,Atual1,Val).



% Cálculo de nós MIN
% Cálculo do primeiro nó MIN
min_ab([E|R],P,A,B,Val):-
    minimax_valor(E,V,P,A,B),
    menor(B1,V,B),
    min_ab(R,P,A,B1,V,Val).

% Não há mais sucessores
min_ab([],_,_,_,Val,Val).

% O valor de beta é menor ou igual ao valor de alfa, cortar a pesquisa
min_ab(_,_,A,B,Val,Val):-
    A >= B, !.
    
% Cálculo dos restantes nós MIN
min_ab([E|R],P,A,B,Atual,Val):-
    minimax_valor(E,V,P,A,B),
    menor(Atual1,V,Atual),
    menor(B1,V,B),
    min_ab(R,P,A,B1,Atual1,Val).



% Funções auxiliares para comparação de valores
maior(A,A,B):- A >= B, !.
maior(B,_,B).

menor(A,A,B):- A =< B, !.
menor(B,_,B).
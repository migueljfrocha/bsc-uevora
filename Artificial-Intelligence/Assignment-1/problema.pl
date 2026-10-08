
% TROCAR AQUI PARA TESTAR OS OUTROS EXEMPLOS 
% trocar o número do estado inicial e do estado final para testar os outros exemplos
estado_inicial(E) :- 
    estado_inicial_1(E).

estado_final(E) :- 
    estado_final_1(E).


% TROCAR AQUI PARA TESTAR AS DIFERENTES HEURÍSTICAS (h1, h2)
h(E, H) :-
    h1(E, H).

% Estado(agente, maquina, saida, obstaculos, objetos)

estado_inicial_1(
    e(
        a(1,2), 
        m(2,2),
        s(7,2),
        [x(7,4), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [] %o's
    )
).

estado_final_1(
    e(
        _,
        m(7,2),
        s(7,2),
        [x(7,4), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [] %o's
    )
).


% Operadores de transição de estados

op(e(a(La, Ca), m(Lm, Cm), S, Xs, Os), apanhar, e(a(La, Ca), m(Lm, Cm), S, Xs, NovoOs), 1) :-
    pode_apanhar(Lm, Cm, Os),
    adjacente(La, Ca, Lm, Cm),
    apanha_o(Lm, Cm, Os, NovoOs).

op(e(a(La, Ca), m(Lm, Cm), S, Xs, Os), empurrar(D), e(a(Lm, Cm), m(LmN, CmN), S, Xs, Os), 1) :-
    move(D, La, Ca, Lm, Cm),
    move(D, Lm, Cm, LmN, CmN),
    lim(LmN, CmN),
    nao_tem_obstaculos(LmN, CmN, Xs).

op(e(a(La,Ca), m(Lm, Cm), S, Xs, Os), move(D), e(a(LaN,CaN), m(Lm, Cm), S, Xs, Os), 1) :-
    move(D, La, Ca, LaN, CaN),
    lim(LaN, CaN),
    nao_tem_obstaculos(LaN, CaN, Xs),
    nao_sobreposto(LaN, CaN, Lm, Cm).


% Predicados Auxiliares

move(dir, L, C1, L, C2) :-
    C2 is C1 + 1.

move(esq, L, C1, L, C2) :-
    C2 is C1 - 1.

move(cima, L1, C, L2, C) :-
    L2 is L1 + 1.

move(baixo, L1, C, L2, C) :-
    L2 is L1 - 1.


pode_apanhar(Lm, Cm, Os) :-
    member(o(Lm, Cm), Os).
    
apanha_o(Lm, Cm, Os, NovoOs) :-
    select(o(Lm, Cm), Os, NovoOs).

nao_tem_obstaculos(L, C, Xs) :- 
    \+ member(x(L,C), Xs).

nao_sobreposto(La, Ca, Lm, Cm) :-
    (La, Ca) \= (Lm, Cm).

lim(A,B) :- 
    A =< 7, 
    A >= 1, 
    B =< 7, 
    B >= 1.


adjacente(La, Ca, Lm, Cm) :-
    La = Lm,
    (Ca is Cm + 1);(Ca is Cm - 1), !.
    
adjacente(La, Ca, Lm, Cm) :-
    Ca = Cm,
    (La is Lm + 1);(La is Lm - 1).


% Heurísticas

h1(e(_, m(Lm, Cm), s(Ls, Cs), _, Os), H) :-
    % Número de objetos por apanhar
    length(Os, Nobj),
    % Distância da máquina à saída
    dist_manhattan((Lm, Cm), (Ls, Cs), Dms),
    H is Dms + Nobj.

h2(e(a(La, Ca), m(Lm, Cm), s(Ls, Cs), _, Os), H) :-
    % Distância do agente à máquina
    dist_manhattan((La, Ca), (Lm, Cm), D),
    Dam is D - 1,
    % Número de objetos por apanhar
    length(Os, Nobj),
    rota_maxima_MOS((Lm, Cm), (Ls, Cs), Os, Dro),
    H is Dam + Nobj + Dro.

% Sem objetos: máquina vai diretamente à saída
rota_maxima_MOS(M, S, [], D) :-
    dist_manhattan(M, S, D).

% Com objetos: escolhe o maior valor de M -> O -> S
rota_maxima_MOS(M, S, Os, Dmax) :-
    Os \= [],
    findall(
        D,
        (
            member(o(Lo,Co), Os),
            dist_manhattan(M, (Lo,Co), Dmo),
            dist_manhattan((Lo,Co), S, Dos),
            D is Dmo + Dos
        ),
        Ds
    ),
    max_list(Ds, Dmax).

dist_manhattan((L1, C1), (L2, C2), D) :-
    D is abs(L1 - L2) + abs(C1 - C2).


% Estados para os outros exemplos

estado_inicial_2(
    e(
        a(1,2), 
        m(2,2),
        s(7,2),
        [x(7,4), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [o(4,2)] %o's
    )
).

estado_final_2(
    e(
        _,
        m(7,2),
        s(7,2),
        [x(7,4), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [] %o's
    )
).

estado_inicial_3(
    e(
        a(1,2), 
        m(2,2),
        s(7,2),
        [x(7,3), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [o(5,5)] %o's
    )
).

estado_final_3(
    e(
        _, 
        m(7,2),
        s(7,2),
        [x(7,3), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [] %o's
    )
).

estado_inicial_4(
    e(
        a(1,2), 
        m(2,2),
        s(7,5),
        [x(7,3), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [o(4,2), o(2,6)] %o's
    )
).

estado_final_4(
    e(
        _, 
        m(7,5),
        s(7,5),
        [x(7,3), x(6,1), x(6,3), x(6,7), x(4,4), x(3,4), x(2,4)], %x's
        [] %o's
    )
).
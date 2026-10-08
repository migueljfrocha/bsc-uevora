% Estados

% estado([Condicoes])
% Os estados sao representados por listas de condicoes. 
estado_inicial(
    [
        sobre(a, chao), sobre(b, a), sobre(c, b), 
        topo(c, 1), topo(chao, 2), topo(chao, 3),
        em(a, 1), em(b, 1), em(c, 1), 
        em(chao, 1), em(chao, 2), em(chao, 3),
        mao_livre(esq), mao_livre(dir)
    ]
).

estado_final(
    [
        sobre(a, chao), sobre(b, chao), sobre(c, b), 
        topo(c, 3), topo(a, 1), topo(chao, 2), 
        em(a, 1), em(b, 3), em(c, 3), 
        em(chao, 1), em(chao, 2), em(chao, 3),
        mao_livre(esq), mao_livre(dir)
    ]
).


% ----------------------------------

% Ações

% accao(a1,Precondicoes,AddList,DeleteList).
% As ações são representadas por um nome, uma lista de pré-condições, 
% uma lista de condições a adicionar e uma lista de condições a remover.
accao(
    agarrar(X, C, M), 
    [em(X, C), sobre(X, Y), topo(X, C), mao_livre(M)], 
    [na_mao(X, M), topo(Y, C)], 
    [topo(X, C), em(X, C), mao_livre(M), sobre(X, Y)]
    ):- 
        member(X, [a, b, c]), 
        member(Y, [a, b, c, chao]), 
        member(C, [1, 2, 3]), 
        member(M, [esq, dir]),
        X \= Y.


accao(
    largar(X, C, M), 
    [na_mao(X, M), topo(Y, C)], 
    [topo(X, C), mao_livre(M), sobre(X, Y), em(X, C)], 
    [topo(Y, C), na_mao(X, M)]
    ):-
        member(X, [a, b, c]), 
        member(Y, [a, b, c, chao]), 
        member(C, [1, 2, 3]), 
        member(M, [esq, dir]),
        X \= Y.
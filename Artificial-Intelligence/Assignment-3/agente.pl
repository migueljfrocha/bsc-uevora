% agente(nome do ficheiro do problema, nome do ficheiro da estratégia)
agente(Jogo, Estrategia) :-
    [Jogo],
    [Estrategia],
    estado_inicial(Ei),
    write('Estado inicial:'), nl,
    imprime_estado(Ei),
    turno_agente(Ei).


% Jogador ganhou, agente não tem mais jogadas
turno_agente(E) :-
    terminal(E), !,
    write('Jogo terminou: Jogador ganhou'), nl.


% O agente joga uma jogada
turno_agente(E) :-
    minimax_decidir(E, Op),
    write('Agente joga: '),
    write(Op), nl,
    op1(E, Op, E1),
    imprime_estado(E1),
    turno_adversario(E1).


% Agente inteligente ganhou, adversário não tem mais jogadas
turno_adversario(E) :-
    terminal(E), !,
    write('Jogo terminou: Agente ganhou'), nl.


% Lê a jogada do adversário
turno_adversario(E) :-
    write('Jogada do adversario: '),
    read(Op),
    joga_adversario(E, Op, E1),
    imprime_estado(E1),
    turno_agente(E1).


% Se a jogada do adversario for válida, executa a jogada
joga_adversario(E, Op, E1) :-
    op1(E, Op, E1), !.


% Se a jogada for inválida, pede outra
joga_adversario(E, _, E1) :-
    write('Jogada invalida. Tente outra vez.'), nl,
    write('Jogada do adversario: '),
    read(Op),
    joga_adversario(E, Op, E1).

imprime_estado(E) :-
    write(E), nl.
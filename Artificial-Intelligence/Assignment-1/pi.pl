:- dynamic(fechado/1). 
:- dynamic(maxNL/1).
:- dynamic(nos/1).
:- dynamic(nfechados/1).
:- dynamic(memoria/1).

nfechados(0).
memoria(0).
maxNL(0).
nos(0).



inc :- 
    retract(nos(N)), 
    N1 is N+1, 
    asserta(nos(N1)).

incF :-
    retract(nfechados(N)),
    N1 is N + 1,
    asserta(nfechados(N1)).


actmax(N) :- 
    maxNL(N1), 
    N1 >= N,!.

actmax(N) :- 
    retract(maxNL(_N1)), 
    asserta(maxNL(N)).

actmem(N):-
    memoria(N1),
    N1 >= N, !.

actmem(N):-
    retract(memoria(_N1)),
    asserta(memoria(N)).

atualiza_memoria(ListaAberta):-
    length(ListaAberta, NAbertos),
    nfechados(NFechados),
    Total is NAbertos + NFechados,
    actmem(Total).
    

limpa :- 
    retractall(fechado(A)),
    retractall(maxNL(A)),
    retractall(nos(A)),
    retractall(memoria(A)),
    retractall(nfechados(A)),
    asserta(maxNL(0)),
    asserta(nos(0)),
    asserta(memoria(0)),
    asserta(nfechados(0)).


%estado_inicial(Estado)
%estado_final(Estado)

%representacao dos operadores
%op(Eact,OP,Eseg,Custo)

%representacao dos nos
%no(Estado,no_pai,Operador,Custo,H+C,Profundidade)


pesquisa(Problema,Alg):-
    consult(Problema),
    estado_inicial(S0),
    limpa,
    ListaInicial = [no(S0,[],[],0,1,0)],
    atualiza_memoria(ListaInicial),
    pesquisa(Alg,ListaInicial,Solucao),
    escreve_seq_solucao(Solucao),
    nos(Ns),
    maxNL(NL),
    nfechados(NF),
    memoria(Mem),
    write(nos(visitados(Ns),lista(NL), fechados(NF), memoria(Mem))).

pesquisa(a,E,S) :- pesquisa_a(E,S).
pesquisa(g,E,S) :- pesquisa_g(E,S).


%pesquisa_a([],_):- !,fail.
pesquisa_a([no(E,Pai,Op,C,HC,P)|R],no(E,Pai,Op,C,HC,P)) :- 
    estado_final(E),
    atualiza_memoria([no(E,Pai,Op,C,HC,P)|R]),
    inc.


pesquisa_a([No|R],Sol):- 
    No = no(E,_,_,_,_,_),
    inc, 
    asserta(fechado(E)),
    incF,
    expande(No,Lseg),
    insere_ord(Lseg,R,Resto),
    length(Resto,N),
    actmax(N),
    atualiza_memoria(Resto),
    pesquisa_a(Resto,Sol).


%pesquisa_g([],_):- !,fail.
pesquisa_g([no(E,Pai,Op,C,HC,P)|_],no(E,Pai,Op,C,HC,P)):- 
    estado_final(E),
    atualiza_memoria([no(E,Pai,Op,C,HC,P)|R]), 
    inc.

pesquisa_g([No|R],Sol):- 
    No = no(E,_,_,_,_,_),
    inc,
    asserta(fechado(E)),
    incF,
    expande_g(No,Lseg),
    insere_ord(Lseg,R,Resto),
    length(Resto,N), 
    actmax(N),
    atualiza_memoria(Resto),
    pesquisa_g(Resto,Sol).


expande(no(E,Pai,Op,C,HC,P),L):- 
    findall(no(En,no(E,Pai,Op,C,HC,P),Opn,Cnn,HCnn,P1),
				(op(E,Opn,En,Cn), 
                \+ fechado(En),
                P1 is P+1, 
                Cnn is Cn + C, 
                h(En,H), 
                HCnn is Cnn + H), 
                L).


expande_g(no(E,Pai,Op,C,HC,P),L):- 
    findall(no(En, no(E,Pai,Op,C,HC,P),Opn,Cnn,H,P1),
            (op(E,Opn,En,Cn),
            \+ fechado(En),
            P1 is P+1, 
            Cnn is Cn + C, 
            h(En,H)), 
            L).


insere_ord([],L,L).

insere_ord([A|L],L1,L2):- 
    insereE_ord(A,L1,L3), 
    insere_ord(L,L3,L2).


insereE_ord(A,[],[A]).

insereE_ord(A,[A1|L],[A,A1|L]):- 
    menor_no(A,A1),!.

insereE_ord(A,[A1|L], [A1|R]):- 
    insereE_ord(A,L,R).


menor_no(no(_,_,_,_,N,_), no(_,_,_,_,N1,_)) :- N < N1.

escreve_seq_solucao(no(E,Pai,Op,Custo,_HC,Prof)):- 
    write(custo(Custo)),nl,
    write(profundidade(Prof)),nl,
    escreve_seq_accoes(no(E,Pai,Op,_,_,_)).

escreve_seq_accoes([]).

escreve_seq_accoes(no(E,Pai,Op,_,_,_)):- 
    escreve_seq_accoes(Pai),
    write(e(Op,E)),nl.

esc(A):- write(A), nl.


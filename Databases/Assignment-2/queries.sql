/**************************************************************************************************************************

Bases de Dados 2024/2025
Trabalho Prático 2
Loja Virtual: Livraria
Elementos do Grupo:
  Miguel Pombeiro - 57829
  Miguel Rocha - 58501


-- Queries utilizadas para a pergunta 10. 

**************************************************************************************************************************/



-- Views utilizadas ao longo do exercício:

-- View usada na alínea m) e na alínea p)
CREATE VIEW leitores_alugaram_memorial as( 
    SELECT NIF
    FROM alugar NATURAL INNER JOIN exemplar NATURAL INNER JOIN livros
    WHERE Titulo = 'Memorial do Convento'
);


--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- Respostas às várias alíneas da pergunta 10
-- alinea a)
SELECT NIF, NomeL
FROM leitor
WHERE TipoL = 'Regular';


--------------------------------------------------------------------------------------------------------------------------
-- alinea b)
SELECT Genero, Titulo, NomeA
FROM generos NATURAL INNER JOIN livros NATURAL INNER JOIN autoria NATURAL INNER JOIN autor
ORDER by Genero, Titulo;


--------------------------------------------------------------------------------------------------------------------------
-- alinea c)
SELECT Genero, COUNT(ISBN) AS nLivros
FROM generos
GROUP BY Genero;


--------------------------------------------------------------------------------------------------------------------------
-- alínea d)
WITH amigosLFrequentes as(
    SELECT NIF, NIF2 as NIFamigo
    FROM leitor, amigo
    WHERE NIF = NIF1 AND TipoL = 'Frequente'
)
SELECT NIF, COUNT(NIFamigo) AS nAmigos
FROM amigosLFrequentes
GROUP BY NIF;


--------------------------------------------------------------------------------------------------------------------------
-- alínea e)
SELECT ISBN, NoExemplares
FROM encomenda_livro
WHERE DataE BETWEEN '2024-01-15' AND '2024-01-21';


--------------------------------------------------------------------------------------------------------------------------
-- alínea f)
SELECT NIF, Titulo, Semana, Ano, Valor
FROM alugar NATURAL INNER JOIN exemplar NATURAL INNER JOIN livros
ORDER BY NIF, Ano, Semana;


--------------------------------------------------------------------------------------------------------------------------
-- alínea g)
SELECT ISBN, NIF, Semana, Ano, Valor
FROM alugar NATURAL INNER JOIN exemplar
ORDER BY ISBN, Ano, Semana;


--------------------------------------------------------------------------------------------------------------------------
-- alínea h)
WITH valores_mes1 as (
    SELECT NIF, Valor
    FROM alugar
    WHERE Semana >= 1 AND Semana <= 4 AND Ano = 2024

    UNION ALL

    Select NIF, 0.0 AS Valor
    FROM leitor as l
    WHERE NIF NOT IN (SELECT NIF FROM alugar)
),                                       
  valor_em_divida_sem_mensalidade as (
    SELECT NIF, SUM(Valor) as ValorSemMensalidade
    FROM valores_mes1
    GROUP BY NIF
  ),
  valor_em_divida_com_mensalidade as (
    SELECT NIF, (ValorSemMensalidade + Mensalidade) as ValorComMensalidade
    FROM valor_em_divida_sem_mensalidade NATURAL INNER JOIN leitor NATURAL INNER JOIN tipoLeitor
)
SELECT NIF, ValorComMensalidade AS ValoremDivida, Valor as ValorPago
FROM valor_em_divida_com_mensalidade NATURAL INNER JOIN Pagamento
WHERE Mes = 2 AND Ano = 2024;
 

--------------------------------------------------------------------------------------------------------------------------
-- alínea i)
SELECT NIF, Estrelas, Comentario
FROM avalia NATURAL INNER JOIN livros
WHERE titulo = 'Memorial do Convento';


--------------------------------------------------------------------------------------------------------------------------
-- alínea j)
WITH livros_misterio_fantasia as(
    SELECT g1.ISBN
    FROM generos as g1, generos as g2
    WHERE g1.ISBN = g2.ISBN AND g1.Genero = 'mistério' AND g2.Genero = 'fantasia'
),
   leitoreslivrosMF as(
    SELECT NIF
    FROM livros_misterio_fantasia NATURAL INNER JOIN avalia
   )
  SELECT DISTINCT NIF2
  FROM leitoreslivrosMF, amigo
  WHERE NIF1 = NIF;


--------------------------------------------------------------------------------------------------------------------------
-- alínea k)
WITH nlivros as (
  SELECT NIF, IdExemplar
  FROM alugar
  WHERE Semana >= 1 AND Semana <= 4 AND Ano = 2024
)
SELECT NIF, COUNT(IdExemplar) as c
FROM nlivros
GROUP BY NIF
HAVING COUNT(IdExemplar) > 10;


--------------------------------------------------------------------------------------------------------------------------
-- alínea l)
SELECT ISBN, AVG(Estrelas) as MediaEstrelas
FROM avalia
GROUP BY ISBN
HAVING AVG(Estrelas) > 3;


--------------------------------------------------------------------------------------------------------------------------
-- alínea m)


SELECT NIF
FROM leitor
WHERE TipoL = 'Frequente'

EXCEPT

SELECT NIF
FROM leitores_alugaram_memorial;


--------------------------------------------------------------------------------------------------------------------------
-- alínea n)
WITH livros_alugados_sem4 as(
      SELECT NIF, IdExemplar
      FROM alugar
      WHERE Semana = 4 AND Ano = 2024
),
      contagem_sem4 as(
      SELECT NIF, COUNT(IdExemplar) as c
      FROM livros_alugados_sem4
      GROUP BY NIF
),
    leitores_que_nao_alugaram_mais as(
      SELECT c1.NIF
      FROM contagem_sem4 as c1, contagem_sem4 as c2
      WHERE c1.c < c2.c
)
SELECT NIF
FROM livros_alugados_sem4

EXCEPT

SELECT NIF
FROM leitores_que_nao_alugaram_mais;


--------------------------------------------------------------------------------------------------------------------------
-- alínea o)
WITH livros_alugados_8sem as (
    SELECT ISBN, IdExemplar
    FROM alugar NATURAL INNER JOIN exemplar
    WHERE Semana >= 1 AND Semana <= 8 AND Ano = 2024
),
    livros_count_8sem as (
    SELECT ISBN, COUNT(IdExemplar) as c
    FROM livros_alugados_8sem
    GROUP BY ISBN
),
    livros_nao_foram_mais_alugados as(
    SELECT c1.ISBN
    FROM livros_count_8sem as c1, livros_count_8sem as c2
    WHERE c1.c < c2.c
)

SELECT ISBN
FROM livros_alugados_8sem

EXCEPT

SELECT ISBN
FROM livros_nao_foram_mais_alugados;


--------------------------------------------------------------------------------------------------------------------------
-- alínea p)
SELECT DISTINCT NIF
FROM leitor as l
WHERE NOT EXISTS(
                  SELECT NIF
                  FROM leitores_alugaram_memorial
                  WHERE NIF != l.NIF

                  EXCEPT

                  SELECT NIF2
                  FROM amigo
                  WHERE l.NIF = amigo.NIF1);


--------------------------------------------------------------------------------------------------------------------------
-- alínea q)
SELECT NIF
FROM leitor
WHERE NIF NOT IN (SELECT NIF1 FROM amigo);


--------------------------------------------------------------------------------------------------------------------------
-- alínea r)
WITH valores_mes_atual as (
    SELECT NIF, Valor
    FROM alugar
    WHERE Semana >= 5 AND Semana <= 8 AND Ano = 2024

    UNION ALL

    Select NIF, 0.0 AS Valor
    FROM leitor as l
    WHERE NIF NOT IN (SELECT NIF FROM alugar)
),                                       
  valor_em_divida_sem_mensalidade as (
    SELECT NIF, SUM(Valor) as ValorSemMensalidade
    FROM valores_mes_atual
    GROUP BY NIF
  ),
  valor_em_divida_com_mensalidade as (
    SELECT NIF, (ValorSemMensalidade + Mensalidade) as ValorComMensalidade
    FROM valor_em_divida_sem_mensalidade NATURAL INNER JOIN leitor NATURAL INNER JOIN tipoLeitor
)
SELECT NIF
FROM valor_em_divida_com_mensalidade NATURAL INNER JOIN Pagamento
WHERE Mes = 3 AND Ano = 2024 AND (Valor - ValorComMensalidade) = 0;


--------------------------------------------------------------------------------------------------------------------------
-- alínea s)
SELECT ISBN, SUM(NoExemplares) as TotalExemplares
FROM encomenda_livro
GROUP BY ISBN;


--------------------------------------------------------------------------------------------------------------------------
-- alínea t)
SELECT ISBN
FROM livros as l
WHERE NOT EXISTS(
                  SELECT IdExemplar
                  FROM exemplar
                  WHERE l.ISBN = exemplar.ISBN

                  EXCEPT 

                  SELECT IdExemplar
                  FROM alugar);


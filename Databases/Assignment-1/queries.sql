/**************************************************************************************************************************

Bases de Dados 2024/2025
Trabalho Prático 1
Rede Social: Fãs de Doces
Elementos do Grupo:
  Miguel Pombeiro - 57829
  Miguel Rocha - 58501


-- Queries utilizadas para a pergunta 4. 

**************************************************************************************************************************/



-- Views utilizadas ao longo do exercício:

BEGIN;
-- Amigos do Joaquim José (alineas b, l)
CREATE VIEW amigos_joaquim AS( -- Vamos procurar pelo nome do Joaquim José e não pelo idmembro (Pode não correr bem se houver mais do que um Joaquim José)
      SELECT membro2 AS membro
      FROM amigo, membro
      WHERE membro1 = idmembro AND 
      	    nome = 'Joaquim José');

-- Custo de cada doce (alíneas g, h, k, o)
CREATE VIEW doce_custo AS(
    SELECT temIngrediente.doce, 
           SUM(ingrediente.custo * temIngrediente.quantidade) AS custo
    FROM ingrediente JOIN temIngrediente ON 
         ingrediente.nome = temIngrediente.ingrediente
    GROUP BY temingrediente.doce);

-- Número de doces criados por cada membro (alíneas i, j)
CREATE VIEW no_doce_memb AS (
  SELECT membro, COUNT(doce) AS no_doces
  FROM criou
  GROUP BY membro);

-- Tempo médio de preparação de cada doce (alínea n, o)
CREATE VIEW avg_time AS(
  SELECT doce, AVG(tempo) AS avg_t
  FROM fez
  GROUP BY doce);

COMMIT;


--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- Respostas às várias alíneas da pergunta 4
-- alinea a)
WITH sp_cria_regional AS (
  SELECT membro.nome, criou.doce
  FROM membro, criou, doce
  WHERE membro.pais = 'Espanha' AND 
        membro.idmembro = criou.membro AND 
        doce.nome = criou.doce AND 
        doce.genero = 'Regional'),

  choc_canela AS (
    SELECT tI1.doce
    FROM temingrediente AS tI1, temingrediente AS tI2
    WHERE tI1.doce = tI2.doce AND 
          tI1.ingrediente = 'Canela' AND 
          tI2.ingrediente = 'Chocolate')

SELECT DISTINCT sp_cria_regional.nome
FROM sp_cria_regional NATURAL INNER JOIN choc_canela;


--------------------------------------------------------------------------------------------------------------------------
-- alinea b)
WITH doce_amigos AS (
SELECT doce.nome AS doce
FROM (criou NATURAL INNER JOIN amigos_joaquim), doce
WHERE doce.genero = 'Tradicional' AND 
      doce.nome = criou.doce)
    
SELECT DISTINCT doce
FROM doce_amigos NATURAL INNER JOIN fez
WHERE aspeto >= 5;


--------------------------------------------------------------------------------------------------------------------------
-- alínea c)
WITH doce_pessoas AS (
  	SELECT membro, COUNT(doce) AS ndoces
  	FROM fez
  	GROUP BY membro)

SELECT DISTINCT Nome
FROM membro AS m, doce_pessoas
WHERE idmembro = doce_pessoas.membro AND 
      ndoces > 0 AND 
      NOT EXISTS (SELECT DISTINCT doce 
                  FROM fez
				          WHERE fez.membro = m.idmembro
                  
                  EXCEPT
                  
                  SELECT DISTINCT doce
                  FROM criou, amigo
				          WHERE amigo.membro1 = m.idmembro AND 
                        criou.membro = amigo.membro2);


--------------------------------------------------------------------------------------------------------------------------
-- alínea d)
-- abordagem 1: Doces que não foram avaliados com sabor 5 por todos os membros que os fizeram.
SELECT DISTINCT doce
FROM fez NATURAL INNER JOIN temingrediente
WHERE sabor < 5 AND 
      ingrediente = 'Natas'

-- abordagem 2: Doces que não foram avaliados com sabor 5 por nenhum dos membros que os fizeram.
SELECT DISTINCT doce
FROM temingrediente
WHERE ingrediente = 'Natas'

EXCEPT

SELECT DISTINCT doce
FROM fez
WHERE sabor = 5


--------------------------------------------------------------------------------------------------------------------------
-- alínea e)
SELECT DISTINCT membro
FROM criou, temingrediente AS ti1, temingrediente AS ti2
WHERE ti1.doce = ti2.doce AND 
	    ti1.doce = criou.doce AND 
      ti1.ingrediente = 'Canela' AND 
      ti2.ingrediente = 'Leite';


--------------------------------------------------------------------------------------------------------------------------
-- alínea f)
SELECT DISTINCT doce
FROM temingrediente
WHERE ingrediente = 'Maçã' OR
	    ingrediente = 'Baunilha';


--------------------------------------------------------------------------------------------------------------------------
-- alínea g)
SELECT custo 
FROM doce_custo
WHERE doce = 'Arroz Doce';


--------------------------------------------------------------------------------------------------------------------------
-- alínea h)
SELECT *
FROM doce_custo


--------------------------------------------------------------------------------------------------------------------------
-- alínea i)
SELECT * 
FROM no_doce_memb;


--------------------------------------------------------------------------------------------------------------------------
-- alínea j)
WITH memb_menos_doce AS (
  SELECT doces1.membro
  FROM no_doce_memb AS doces1, no_doce_memb AS doces2
  WHERE doces1.no_doces < doces2.no_doces),
  
  answer_id AS (
    SELECT membro 
    FROM no_doce_memb
    
    EXCEPT
    
    SELECT membro 
    FROM memb_menos_doce)
   
   
 SELECT nome 
 FROM answer_id, membro
 WHERE idmembro = answer_id.membro;


--------------------------------------------------------------------------------------------------------------------------
-- alínea k)
WITH menos_caros AS (
  SELECT dc1.doce
  FROM doce_custo AS dc1, doce_custo AS dc2
  WHERE dc1.custo < dc2.custo),

  mais_caro AS(
    SELECT doce 
    FROM doce_custo
    
    EXCEPT
    
    SELECT doce 
    FROM menos_caros)
  
SELECT nome
FROM membro, fez, mais_caro
WHERE fez.doce = mais_caro.doce AND
      idmembro = fez.membro;


--------------------------------------------------------------------------------------------------------------------------
-- alínea l)
SELECT DISTINCT membro1
FROM amigo AS am
WHERE NOT EXISTS (SELECT DISTINCT membro 
                  FROM amigos_joaquim
                  WHERE am.membro1 != membro -- To avoid Joaquim having to be friends with himself if the member being considered is firends with Joaquim
                  
                  EXCEPT
                  
                  SELECT DISTINCT membro2
                  FROM amigo
                  WHERE am.membro1 = amigo.membro1);


--------------------------------------------------------------------------------------------------------------------------
-- alínea m)
SELECT DISTINCT membro
FROM fez AS f
WHERE NOT EXISTS (SELECT DISTINCT doce
                   FROM temingrediente
                   WHERE ingrediente = 'Baunilha'
                   
                   EXCEPT
                   
                   SELECT DISTINCT doce
                   FROM fez
                   WHERE f.membro = fez.membro); 


--------------------------------------------------------------------------------------------------------------------------
-- alínea n)
SELECT doce
FROM avg_time
WHERE avg_t = 1;


--------------------------------------------------------------------------------------------------------------------------
-- alínea o)
WITH menos_rapidos AS(
    SELECT DISTINCT t1.doce
    FROM avg_time AS t1, avg_time AS t2
    WHERE t1.avg_t > t2.avg_t),
    
  mais_rapidos AS(
    SELECT doce
    FROM fez

    EXCEPT

    SELECT doce
    FROM menos_rapidos),

  menos_baratos AS(
    SELECT DISTINCT dc1.doce
    FROM doce_custo AS dc1, doce_custo AS dc2
    WHERE dc1.custo > dc2.custo),
    
  mais_baratos AS(
    SELECT doce 
    FROM fez

    EXCEPT
    
    SELECT doce 
    FROM menos_baratos)

SELECT doce
FROM mais_baratos

INTERSECT

SELECT doce
FROM mais_rapidos;
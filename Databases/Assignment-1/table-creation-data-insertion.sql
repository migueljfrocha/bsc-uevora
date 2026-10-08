/**************************************************************************************************************************

Bases de Dados 2024/2025
Trabalho Prático 1
Rede Social: Fãs de Doces
Elementos do Grupo:
  Miguel Pombeiro - 57829
  Miguel Rocha - 58501


-- Criação das tabelas e inserção de dados.

**************************************************************************************************************************/



-- 2. Table creation 

BEGIN;


CREATE TABLE membro (
    Nome TEXT NOT NULL,
    IdMembro VARCHAR(100) NOT NULL UNIQUE ,
    Pais VARCHAR(50) NOT NULL,
    DataNasc DATE NOT NULL,
    PRIMARY KEY (IdMembro)
);

CREATE TABLE doce (
    Nome VARCHAR(50) NOT NULL UNIQUE,
    Descricao TEXT NOT NULL UNIQUE,
    Genero VARCHAR(50) NOT NULL,
    PRIMARY KEY (Nome)
);

CREATE TABLE amigo (
    Membro1 VARCHAR(100) NOT NULL, 
    Membro2 VARCHAR(100) NOT NULL,
    PRIMARY KEY (Membro1, Membro2)
);

CREATE TABLE criou(
    Membro VARCHAR(100) NOT NULL,
    Doce VARCHAR(50) NOT NULL,
    PRIMARY KEY (Membro, Doce)   
);

CREATE TABLE fez (
    Membro VARCHAR(100) NOT NULL,
    Doce VARCHAR(50) NOT NULL,
    Tempo SMALLINT NOT NULL, -- Tempo (1 a 5) que demorou a fazer o doce
    Aspeto SMALLINT NOT NULL, -- Aspeto (1 a 5) do doce
    Sabor SMALLINT NOT NULL, -- Sabor (1 a 5) do doce
    PRIMARY KEY (Membro, Doce)
);

CREATE TABLE ingrediente ( 
    Nome VARCHAR(50) NOT NULL UNIQUE,
    Custo NUMERIC(10, 7) NOT NULL, 
    PRIMARY KEY (Nome)
);

CREATE TABLE temIngrediente (
    Doce VARCHAR(50) NOT NULL,
    Ingrediente VARCHAR(50) NOT NULL,
    Quantidade INT NOT NULL, -- Quantidade de ingrediente necessária para fazer o doce
    PRIMARY KEY (Doce, Ingrediente)
);



-- Foreign keys restrictions

-- Tabela amigo: Membro1 e Membro2 têm de existir na tabela membro
ALTER TABLE amigo ADD FOREIGN KEY (Membro1) REFERENCES membro(IdMembro) on DELETE RESTRICT; 
ALTER TABLE amigo ADD FOREIGN KEY (Membro2) REFERENCES membro(IdMembro) on DELETE RESTRICT;

-- Tabela criou: Membro e Doce têm de existir na tabela membro e doce respetivamente
ALTER TABLE criou ADD FOREIGN KEY (Membro) REFERENCES membro(IdMembro) on DELETE RESTRICT;
ALTER TABLE criou ADD FOREIGN KEY (Doce) REFERENCES doce(Nome) on DELETE RESTRICT;
ALTER TABLE criou ADD FOREIGN KEY (Membro, Doce) REFERENCES fez(Membro, Doce) on DELETE RESTRICT;

-- Tabela fez: Membro e Doce têm de existir na tabela membro e doce respetivamente
ALTER TABLE fez ADD FOREIGN KEY (Membro) REFERENCES membro(IdMembro) on DELETE RESTRICT;
ALTER TABLE fez ADD FOREIGN KEY (Doce) REFERENCES doce(Nome) on DELETE RESTRICT;

-- Tabela temIngrediente: Doce e Ingrediente têm de existir na tabela doce e ingrediente respetivamente
ALTER TABLE temIngrediente ADD FOREIGN KEY (Doce) REFERENCES doce(Nome) on DELETE RESTRICT;
ALTER TABLE temIngrediente ADD FOREIGN KEY (Ingrediente) REFERENCES ingrediente(Nome) on DELETE RESTRICT;


COMMIT;


--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------


-- 3. Data insertion

-- Populating membro table
BEGIN;

INSERT INTO membro (nome, IdMembro, pais, datanasc) VALUES
    ('Joaquim José',                'joaquimjose',          'Portugal',     '1990-01-01'),
	('Maria de Lourdes Modesto',    'marialourdesmodesto',  'Portugal',     '1930-06-01'),
    ('Alex Atala',                  'alexatala',            'Brasil',       '1968-06-03'),
    ('Ferran Adriá',                'ferranadria',          'Espanha',      '1962-05-14'),
    ('Pierre Hérme',                'pierreherme',          'França',       '1961-11-20'),
    ('Elizabeth Falkner',           'elizabethfalkner',     'EUA',          '1966-02-12'),
    ('Lorraine Pascale',            'lorrainepascale',      'Reino Unido',  '1972-11-17'),
    ('Gaston Lenôtre',              'gastonlenotre',        'França',       '1920-05-28'),
    ('Heston Blumenthal',           'hestonblumenthal',     'Reino Unido',  '1966-05-27'),
    ('Mineko Kato',                 'minekokato',           'Japão',        '1900-01-01');

COMMIT;


-- Populating amigo table
BEGIN;

INSERT INTO amigo (Membro1, Membro2) VALUES
    ('joaquimjose',             'marialourdesmodesto'),
    ('joaquimjose',             'alexatala'),
    ('joaquimjose',             'ferranadria'),
    ('joaquimjose',             'pierreherme'),
    ('joaquimjose',             'elizabethfalkner'),
    ('joaquimjose',             'lorrainepascale'),
    ('joaquimjose',             'gastonlenotre'),    -- Se isto acontece o Gaston tem de ser amigo de si próprio para que ele seja resposta à pergunta 4.l)
    ('joaquimjose',             'hestonblumenthal'),
    ('joaquimjose',             'minekokato'),
    ('marialourdesmodesto',     'joaquimjose'),
    ('alexatala',               'joaquimjose'),
    ('ferranadria',             'joaquimjose'),
    ('pierreherme',             'joaquimjose'),
    ('elizabethfalkner',        'joaquimjose'),
    ('lorrainepascale',         'joaquimjose'),
    ('gastonlenotre',           'joaquimjose'),
    ('hestonblumenthal',        'joaquimjose'),
    ('minekokato',              'joaquimjose'),

    ('hestonblumenthal',        'gastonlenotre'),
    ('hestonblumenthal',        'minekokato'),
    ('gastonlenotre',           'hestonblumenthal'),
    ('minekokato',              'hestonblumenthal'),
    
    ('alexatala',               'lorrainepascale'),
    ('lorrainepascale',         'alexatala'),

    ('ferranadria',             'elizabethfalkner'),
    ('ferranadria',             'alexatala'),
    ('elizabethfalkner',        'ferranadria'),
    ('alexatala',               'ferranadria'),

    ('marialourdesmodesto',     'lorrainepascale'),
    ('marialourdesmodesto',     'elizabethfalkner'),
    ('lorrainepascale',         'marialourdesmodesto'),
    ('elizabethfalkner',        'marialourdesmodesto'),

    --para a pergunta l)
    ('gastonlenotre',           'minekokato'),
    ('gastonlenotre',           'marialourdesmodesto'),
    ('gastonlenotre',           'ferranadria'),
    ('gastonlenotre',           'pierreherme'),
    ('gastonlenotre',           'elizabethfalkner'),
    ('gastonlenotre',           'lorrainepascale'),
    ('gastonlenotre',           'alexatala'),
    ('minekokato',              'gastonlenotre'),
    ('marialourdesmodesto',     'gastonlenotre'),
    ('ferranadria',             'gastonlenotre'),
    ('pierreherme',             'gastonlenotre'),
    ('elizabethfalkner',        'gastonlenotre'),
    ('lorrainepascale',         'gastonlenotre'),
    ('alexatala',               'gastonlenotre');

COMMIT;

-- Populating doce table
BEGIN;

INSERT INTO doce (Nome, Descricao, Genero) VALUES
    ('Arroz Doce',                      'Arroz cozido em leite com açúcar.',                        'Tradicional'),
    ('Mousse de Chocolate',             'Mousse de chocolate com água.',                            'Tradicional'),
    ('Bolo de Chocolate e Canela',      'Simples Bolo com chocolate e canela.',                     'Regional'),
    ('Sericaia',                        'Doce conventual alentejano cozido num prato de barro.',    'Regional'),
    ('Pudim Abade de Priscos',          'Pudim de ovos com toucinho.',                              'Regional'),
    ('Bolo de Bolacha',                 'Bolachas embebidas em café e natas.',                      'Tradicional'),
    ('Pão de Rala',                     'Doce conventual alentejano com amêndoa e ovos.',           'Regional'),
    ('Tarte de Alfarroba',              'Tarte com farinha de alfarroba.',                          'Regional'),
    ('Ovos Moles de Aveiro',            'Doce conventual aveirense com ovos e açúcar.',             'Regional'),
    ('Tarte de Natas',                  'Tarte com natas e açúcar.',                                'Tradicional'),
    ('Tarte de Maçã',                   'Tarte com maça e canela.',                                 'Tradicional');

COMMIT;


-- Populating fez table
BEGIN;

INSERT INTO fez (Membro, Doce, Tempo, Aspeto, Sabor) VALUES
    ('marialourdesmodesto',         'Arroz Doce',                       4, 2, 4),
    ('hestonblumenthal',            'Arroz Doce',                       3, 2, 5),

    ('hestonblumenthal',            'Mousse de Chocolate',              1, 3, 4),
    ('pierreherme',                 'Mousse de Chocolate',              1, 5, 4),
    ('minekokato',                  'Mousse de Chocolate',              3, 1, 2),

    ('ferranadria',                 'Bolo de Chocolate e Canela',       5, 3, 4),
    ('alexatala',                   'Bolo de Chocolate e Canela',       2, 2, 3),

    ('pierreherme',                 'Sericaia',                         1, 2, 4),
    ('alexatala',                   'Sericaia',                         2, 1, 1),

    ('marialourdesmodesto',         'Pudim Abade de Priscos',           1, 5, 5),
    ('joaquimjose',                 'Pudim Abade de Priscos',           2, 1, 4),
    
    ('elizabethfalkner',            'Bolo de Bolacha',                  1, 4, 5),
    ('lorrainepascale',             'Bolo de Bolacha',                  2, 5, 5),
    ('hestonblumenthal',            'Bolo de Bolacha',                  2, 5, 5),

    ('minekokato',                  'Pão de Rala',                      4, 1, 1),
    ('lorrainepascale',             'Pão de Rala',                      4, 2, 2),
    ('hestonblumenthal',            'Pão de Rala',                      3, 3, 3),    

    ('marialourdesmodesto',         'Tarte de Alfarroba',               2, 5, 5),
    ('ferranadria',                 'Tarte de Alfarroba',               1, 1, 1),

    ('alexatala',                   'Ovos Moles de Aveiro',             1, 5, 5),
    ('joaquimjose',                 'Ovos Moles de Aveiro',             1, 4, 2),

    ('ferranadria',                 'Tarte de Natas',                   2, 1, 1),
    ('joaquimjose',                 'Tarte de Natas',                   1, 5, 4),

    ('pierreherme',                 'Tarte de Maçã',                    5, 4, 4),
    ('marialourdesmodesto',         'Tarte de Maçã',                    5, 5, 5);

COMMIT;


-- Populating criou table
BEGIN;

INSERT INTO criou (Membro, Doce) VALUES
    ('marialourdesmodesto',         'Arroz Doce'),
    ('hestonblumenthal',            'Mousse de Chocolate'),
    ('ferranadria',                 'Bolo de Chocolate e Canela'),
    ('pierreherme',                 'Sericaia'),
    ('marialourdesmodesto',         'Pudim Abade de Priscos'),
    ('elizabethfalkner',            'Bolo de Bolacha'),
    ('hestonblumenthal',            'Pão de Rala'),
    ('marialourdesmodesto',         'Tarte de Alfarroba'),
    ('alexatala',                   'Ovos Moles de Aveiro'),
    ('ferranadria',                 'Tarte de Natas'),
    ('pierreherme',                 'Tarte de Maçã');

COMMIT;


-- Populating ingrediente table
BEGIN;

INSERT INTO ingrediente (Nome, Custo) VALUES
    ('Açúcar',          0.00109),
    ('Farinha',         0.00069),
    ('Chocolate',       0.00995),
    ('Ovos',            0.2),
    ('Natas',           0.00375),
    ('Leite',           0.00082),
    ('Água',            0.00014),
    ('Maçã',            0.00229),
    ('Manteiga',        0.00672),
    ('Alfarroba',       0.01316),
    ('Baunilha',        3.495),
    ('Canela',          0.4975),
    ('Pimenta',         0.37556),
    ('Café',            0.01845),
    ('Arroz',           0.00119),
    ('Limão',           0.00199),
    ('Bolacha',         0.00211),
    ('Amêndoa',         0.01495);

COMMIT;


-- Populating temIngrediente table
BEGIN;

INSERT INTO temIngrediente (Doce, Ingrediente, Quantidade) VALUES
    -- Arroz Doce
    ('Arroz Doce',                          'Arroz',        300),
    ('Arroz Doce',                          'Açúcar',       300),
    ('Arroz Doce',                          'Leite',        600),
    ('Arroz Doce',                          'Canela',       7),
    ('Arroz Doce',                          'Baunilha',     7),
    ('Arroz Doce',                          'Limão',        1),

    -- Mousse de Chocolate
    ('Mousse de Chocolate',                 'Chocolate',    200),
    ('Mousse de Chocolate',                 'Água',         200),

    --Bolo de Chocolate e Canela
    ('Bolo de Chocolate e Canela',          'Canela',       30),
    ('Bolo de Chocolate e Canela',          'Açúcar',       150),
    ('Bolo de Chocolate e Canela',          'Farinha',      600),
    ('Bolo de Chocolate e Canela',          'Ovos',         4),
    ('Bolo de Chocolate e Canela',          'Leite',        900),
    ('Bolo de Chocolate e Canela',          'Manteiga',     40),
    ('Bolo de Chocolate e Canela',          'Chocolate',    150),

    --Sericaia
    ('Sericaia',                            'Leite',        500),
    ('Sericaia',                            'Açúcar',       170),
    ('Sericaia',                            'Ovos',         6),
    ('Sericaia',                            'Canela',       30),
    ('Sericaia',                            'Limão',        1),

    --Pudim de Abade de Priscos
    ('Pudim Abade de Priscos',              'Ovos',         15),
    ('Pudim Abade de Priscos',              'Açúcar',       500),
    ('Pudim Abade de Priscos',              'Água',         650),
    ('Pudim Abade de Priscos',              'Canela',       30),

    --Bolo de Bolacha
    ('Bolo de Bolacha',                     'Manteiga',     250),
    ('Bolo de Bolacha',                     'Bolacha',      400),
    ('Bolo de Bolacha',                     'Açúcar',       200),
    ('Bolo de Bolacha',                     'Ovos',          3),
    ('Bolo de Bolacha',                     'Café',         90),
    ('Bolo de Bolacha',                     'Baunilha',     7),

    --Pão de Rala
    ('Pão de Rala',                         'Ovos',         20),
    ('Pão de Rala',                         'Açúcar',       700),
    ('Pão de Rala',                         'Amêndoa',      500),

    --Tarte de Alfarroba
    ('Tarte de Alfarroba',                  'Açúcar',       150),
    ('Tarte de Alfarroba',                  'Ovos',         6),
    ('Tarte de Alfarroba',                  'Amêndoa',      100),
    ('Tarte de Alfarroba',                  'Alfarroba',    50),

    --Ovos Moles de Aveiro
    ('Ovos Moles de Aveiro',                'Ovos',         8),
    ('Ovos Moles de Aveiro',                'Açúcar',       150),
    ('Ovos Moles de Aveiro',                'Água',         150),

    --Tarte de Natas
    ('Tarte de Natas',                      'Natas',        600),
    ('Tarte de Natas',                      'Ovos',         6),
    ('Tarte de Natas',                      'Açúcar',       200),
    ('Tarte de Natas',                      'Bolacha',      300),

    --Tarte de Maça
    ('Tarte de Maçã',                       'Farinha',      80),
    ('Tarte de Maçã',                       'Açúcar',       200),
    ('Tarte de Maçã',                       'Leite',        250),
    ('Tarte de Maçã',                       'Água',         250),
    ('Tarte de Maçã',                       'Ovos',         4),
    ('Tarte de Maçã',                       'Maçã',         600);

COMMIT;


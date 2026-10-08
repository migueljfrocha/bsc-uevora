/**************************************************************************************************************************

Bases de Dados 2024/2025
Trabalho Prático 2
Loja Virtual: Livraria
Elementos do Grupo:
  Miguel Pombeiro - 57829
  Miguel Rocha - 58501


-- Criação das tabelas e inserção de dados.

**************************************************************************************************************************/


-- 8. Table Creation

BEGIN;

CREATE TABLE leitor (
    NomeL VARCHAR(100) NOT NULL,
    NIF VARCHAR(10) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Nacionalidade VARCHAR(50) NOT NULL,
    TipoL VARCHAR(25) NOT NULL,
    PRIMARY KEY (NIF)
);

CREATE TABLE livros (
    ISBN VARCHAR(15) NOT NULL,
    Titulo VARCHAR(100) NOT NULL,
    NIFed VARCHAR(10) NOT NULL,
    PRIMARY KEY (ISBN)
);

CREATE TABLE generos (
    ISBN VARCHAR(15) NOT NULL,
    Genero VARCHAR(50) NOT NULL,
    PRIMARY KEY (ISBN, Genero)
);

CREATE TABLE autor (
    IdAutor VARCHAR(50) NOT NULL,
    NomeA VARCHAR(100) NOT NULL,
    Nacionalidade VARCHAR(50) NOT NULL,
    PRIMARY KEY (IdAutor)
);

CREATE TABLE autoria (
    ISBN VARCHAR(15) NOT NULL,
    IdAutor VARCHAR(50) NOT NULL,
    PRIMARY KEY (ISBN, IdAutor)
);

CREATE TABLE exemplar ( 
    IdExemplar VARCHAR(50) NOT NULL,
    Link TEXT NOT NULL,
    ISBN VARCHAR(15) NOT NULL,
    PRIMARY KEY (IdExemplar)
);

CREATE TABLE editora (
    NIFed VARCHAR(10) NOT NULL,
    NomeE VARCHAR(100) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    PRIMARY KEY (NIFed)
);

CREATE TABLE encomenda (
    NIFed VARCHAR(10) NOT NULL,
    DataE DATE NOT NULL,
    Valor NUMERIC(10, 7) NOT NULL,
    PRIMARY KEY (NIFed, DataE)
);

CREATE TABLE tipoLeitor (
    TipoL VARCHAR(25) NOT NULL,
    NmaxLivros INT NOT NULL,
    Mensalidade NUMERIC(10, 7) NOT NULL,
    PRIMARY KEY (TipoL)
);

CREATE TABLE pagamento (
    NIF VARCHAR(10) NOT NULL,
    Mes INT NOT NULL,
    Ano INT NOT NULL,
    Valor NUMERIC(10, 7) NOT NULL,
    PRIMARY KEY (NIF, Mes, Ano)
);

CREATE TABLE amigo (
    NIF1 VARCHAR(10) NOT NULL,
    NIF2 VARCHAR(10) NOT NULL,
    PRIMARY KEY (NIF1, NIF2)
);
CREATE TABLE alugar (
    NIF VARCHAR(10) NOT NULL,
    IdExemplar VARCHAR(50) NOT NULL,
    Valor NUMERIC(10, 7) NOT NULL,
    Semana INT NOT NULL,
    Ano INT NOT NULL,
    PRIMARY KEY (IdExemplar, Semana, Ano)
);
CREATE TABLE avalia (
    NIF VARCHAR(10) NOT NULL,
    ISBN VARCHAR(15) NOT NULL,
    Estrelas INT NOT NULL,
    Comentario TEXT NOT NULL,
    PRIMARY KEY (NIF, ISBN)
);
CREATE TABLE encomenda_livro (
    ISBN VARCHAR(15) NOT NULL,
    NIFed VARCHAR(10) NOT NULL,
    DataE DATE NOT NULL,
    NoExemplares INT NOT NULL,
    PRIMARY KEY (ISBN, NIFed, DataE)
);

COMMIT;
-- Foreign keys restrictions
-- Tabela leitor: TipoL tem de existir na relação membro
ALTER TABLE leitor ADD FOREIGN KEY (TipoL) REFERENCES tipoLeitor(TipoL) on DELETE RESTRICT;

-- Tabela livros: NIFed tem de existir na relação editora
ALTER TABLE livros ADD FOREIGN KEY (NIFed) REFERENCES editora(NIFed) on DELETE RESTRICT;

-- Tabela generos: ISBN tem de existir na relação livros
ALTER TABLE generos ADD FOREIGN KEY (ISBN) REFERENCES livros(ISBN) on DELETE RESTRICT;

-- Tabela autoria: ISBN e IdAutor têm de existir nas relações livros e autor respetivamente
ALTER TABLE autoria ADD FOREIGN KEY (ISBN) REFERENCES livros(ISBN) on DELETE RESTRICT;
ALTER TABLE autoria ADD FOREIGN KEY (IdAutor) REFERENCES autor(IdAutor) on DELETE RESTRICT;

-- Tabela exemplar: ISBN tem de existir na relação livros
ALTER TABLE exemplar ADD FOREIGN KEY (ISBN) REFERENCES livros(ISBN) on DELETE RESTRICT;

-- Tabela encomenda: NIFed tem de existir na relação editora
ALTER TABLE encomenda ADD FOREIGN KEY (NIFed) REFERENCES editora(NIFed) on DELETE RESTRICT;

-- Tabela pagamento: NIF tem de existir na relação leitor
ALTER TABLE pagamento ADD FOREIGN KEY (NIF) REFERENCES leitor(NIF) on DELETE RESTRICT;

-- Tabela amigo: NIF1 e NIF2 têm de existir na relação leitor
ALTER TABLE amigo ADD FOREIGN KEY (NIF1) REFERENCES leitor(NIF) on DELETE RESTRICT;
ALTER TABLE amigo ADD FOREIGN KEY (NIF2) REFERENCES leitor(NIF) on DELETE RESTRICT;

-- Tabela alugar: NIF e IdExemplar têm de existir nas relações leitor e exemplar respetivamente
ALTER TABLE alugar ADD FOREIGN KEY (NIF) REFERENCES leitor(NIF) on DELETE RESTRICT;
ALTER TABLE alugar ADD FOREIGN KEY (IdExemplar) REFERENCES exemplar(IdExemplar) on DELETE RESTRICT;

-- Tabela avalia: NIF e ISBN têm de existir nas relações leitor e livros respetivamente
ALTER TABLE avalia ADD FOREIGN KEY (NIF) REFERENCES leitor(NIF) on DELETE RESTRICT;
ALTER TABLE avalia ADD FOREIGN KEY (ISBN) REFERENCES livros(ISBN) on DELETE RESTRICT;

-- Tabela encomenda_livro: ISBN e NIFed têm de existir nas relações livros e editora respetivamente
ALTER TABLE encomenda_livro ADD FOREIGN KEY (ISBN) REFERENCES livros(ISBN) on DELETE RESTRICT;
ALTER TABLE encomenda_livro ADD FOREIGN KEY (NIFed) REFERENCES editora(NIFed) on DELETE RESTRICT;

COMMIT;


-- 9. Populate Tables

BEGIN;
INSERT INTO tipoLeitor(TipoL, NmaxLivros, Mensalidade) VALUES
    ('Frequente', 5,  1.00),
    ('Regular',   2,  2.00);
COMMIT;


BEGIN;
INSERT INTO leitor (NomeL, NIF, Email, Nacionalidade, TipoL) VALUES
    ('Zé Povinho',           '000000000', 'zepovinho112@gmail.com',    'Portuguesa',  'Frequente'),
    ('Pina Manique',         '111111111', 'pinamanique@gmail.com',     'Portuguesa',  'Frequente'),
    ('FlorBela Espanca',     '222222222', 'florbelaespanca@gmail.com', 'Portuguesa',  'Frequente'),
    ('Salvador Dalí',        '333333333', 'salvadordali@gmail.com',    'Espanhola',   'Frequente'),
    ('Charlie Chaplin',      '444444444', 'charliechaplin@gmail.com',  'Britânico',   'Frequente'),
    ('Coco Chanel',          '555555555', 'cocochanel@gmail.com',      'Francesa',    'Regular'),
    ('Mata Hari',            '666666666', 'matahari@gmail.com',        'Neerlandesa', 'Regular'),
    ('Cristiano Ronaldo',    '777777777', 'cristiano@gmail.com',       'Portuguesa',  'Regular'),
    ('António Champalimaud', '888888888', 'antoniochampali@gmail.com', 'Portuguesa',  'Regular'),
    ('Guglielmo Marconi',    '999999999', 'guglielmomarcon@gmail.com', 'Italiana',    'Regular');
COMMIT;


BEGIN;
INSERT INTO editora(NIFed, NomeE, Email) VALUES 
    ('000000000', 'Porto Editora',    'portoeditora@gmail.com'),
    ('111111111', 'LeYa',             'leya@gmail.com'),
    ('222222222', 'Areal Editores',   'arealeditores@gmail.com'),
    ('444444444', 'Texto Editora',    'textoeditora@gmail.com'),
    ('999999999', 'Caminho',          'caminho@gmail.com'),
    ('888888888', 'Raiz Editora',     'raizeditora@gmail.com'),
    ('777777777', 'Edições ASA',      'edicoesasa@gmail.com'),
    ('666666666', 'Livros Horizonte', 'livroshorizonte@gmail.com');
COMMIT;


BEGIN;
INSERT INTO livros(ISBN, Titulo, NIFed) VALUES
    ('0000011111', 'Memorial do Convento',              '000000000'),
    ('1111122222', 'Os Maias',                          '222222222'),
    ('2222233333', 'O Ano da Morte de Ricardo Reis',    '000000000'),
    ('3333344444', 'Ensaio sobre a Cegueira',           '444444444'),
    ('4444455555', 'Harry Potter e a Pedra Filosofal',  '111111111'),
    ('5555566666', 'Mensagem',                          '666666666'),
    ('6666677777', 'Sermão de Santo António',           '777777777'),
    ('7777788888', '1984',                              '888888888'),
    ('8888899999', 'Os Lusíadas',                       '999999999'),
    ('9999900000', 'Uma Aventura na Cidade',            '111111111'),
    ('0011001100', 'Database System Concepts',          '444444444');
COMMIT;


BEGIN;
INSERT INTO generos(ISBN, Genero) VALUES
    ('0000011111', 'drama'),
    ('1111122222', 'drama'),
    ('2222233333', 'drama'),
    ('3333344444', 'ensaio'),
    ('4444455555', 'fantasia'),
    ('5555566666', 'fantasia'),
    ('6666677777', 'crime'),
    ('7777788888', 'fantasia'),
    ('8888899999', 'mistério'),
    ('9999900000', 'mistério'),
    ('0011001100', 'informática'),
    ('0011001100', 'educação'),

    ('0000011111', 'ensaio'),
    ('1111122222', 'horror'),
    ('2222233333', 'mistério'),
    ('3333344444', 'romance'),
    ('4444455555', 'mistério'),
    ('5555566666', 'drama'),
    ('6666677777', 'humor'),
    ('7777788888', 'drama'),
    ('8888899999', 'fantasia'),
    ('9999900000', 'aventura');
COMMIT;


BEGIN;
INSERT INTO autor(IdAutor, NomeA, Nacionalidade) VALUES
    ('josesaramago',        'José Saramago',        'Portuguesa'),
    ('ecadequeiros',        'Eça de Queirós',       'Portuguesa'),
    ('jkrowling',           'J. K. Rowling',        'Britânica'),
    ('fernandopessoa',      'Fernando Pessoa',      'Portuguesa'),
    ('antoniovieira',       'António Vieira',       'Portuguesa'),
    ('georgeorwell',        'George Orwell',        'Britânico'),
    ('luisdecamoes',        'Luís de Camões',       'Portuguesa'),
    ('anamariamagalhaes',   'Ana Maria Magalhães',  'Portuguesa'),
    ('isabelalcada',        'Isabel Alçada',        'Portuguesa'),
    ('avisilberschatz',     'Avi Silberschatz',     'Americana'),
    ('henryfkorth',         'Henry F. Korth',       'Americana');
COMMIT;


BEGIN;
INSERT INTO autoria(ISBN, IdAutor) VALUES
    ('0000011111', 'josesaramago'),
    ('1111122222', 'ecadequeiros'),
    ('2222233333', 'josesaramago'),
    ('3333344444', 'josesaramago'),
    ('4444455555', 'jkrowling'),
    ('5555566666', 'fernandopessoa'),
    ('6666677777', 'antoniovieira'),
    ('7777788888', 'georgeorwell'),
    ('8888899999', 'luisdecamoes'),
    ('9999900000', 'anamariamagalhaes'),
    ('9999900000', 'isabelalcada'),
    ('0011001100', 'avisilberschatz'),
    ('0011001100', 'henryfkorth');
COMMIT;


BEGIN;
INSERT INTO exemplar(IdExemplar, Link, ISBN) VALUES 
    ('memorialConvento1', 'https://www.book.com/memorialconvento1', '0000011111'),--
    ('memorialConvento2', 'https://www.book.com/memorialconvento2', '0000011111'),--

    ('osmaias1',          'https://www.book.com/osmaias1',          '1111122222'),--
    ('osmaias2',          'https://www.book.com/osmaias2',          '1111122222'),--

    ('anoRicardoReis1',   'https://www.book.com/anoRicardoReis1',   '2222233333'),--
    ('anoRicardoReis2',   'https://www.book.com/anoRicardoReis2',   '2222233333'),--
    ('anoRicardoReis3',   'https://www.book.com/anoRicardoReis3',   '2222233333'),--

    ('ensaioCegueira1',   'https://www.book.com/ensaioCegueira1',   '3333344444'),--
    ('ensaioCegueira2',   'https://www.book.com/ensaioCegueira2',   '3333344444'),--

    ('harryPotter1',      'https://www.book.com/harryPotter1',      '4444455555'),--
    ('harryPotter2',      'https://www.book.com/harryPotter2',      '4444455555'),--

    ('mensagem1',         'https://www.book.com/mensagem1',         '5555566666'),--
    ('mensagem2',         'https://www.book.com/mensagem2',         '5555566666'),--

    ('sermaoAntonio1',    'https://www.book.com/sermaoAntonio1',    '6666677777'),--
    ('sermaoAntonio2',    'https://www.book.com/sermaoAntonio2',    '6666677777'),

    ('1984_1',            'https://www.book.com/19841',             '7777788888'),--
    ('1984_2',            'https://www.book.com/19842',             '7777788888'),--
    ('1984_3',            'https://www.book.com/19843',             '7777788888'),--

    ('lusíadas1',         'https://www.book.com/lusíadas1',         '8888899999'),--
    ('lusíadas2',         'https://www.book.com/lusíadas2',         '8888899999'),--

    ('aventuraCidade1',   'https://www.book.com/aventuraCidade',    '9999900000'),--
    ('aventuraCidade2',   'https://www.book.com/aventuraCidade',    '9999900000'),--

    ('databaseSystem1',   'https://www.book.com/databaseSystem1',   '0011001100'),
    ('databaseSystem2',   'https://www.book.com/databaseSystem2',   '0011001100'),
    ('databaseSystem3',   'https://www.book.com/databaseSystem3',   '0011001100'),
    ('databaseSystem4',   'https://www.book.com/databaseSystem4',   '0011001100');
COMMIT;


BEGIN;
INSERT INTO encomenda(NIFed, DataE, Valor) VALUES 
    ('000000000', '2024-01-15', 100.50),
    ('111111111', '2024-01-31', 150.50),
    ('222222222', '2024-02-02', 50.00);
COMMIT;


BEGIN;
INSERT INTO pagamento(NIF, Mes, Ano, Valor) VALUES 
    ('000000000', 1, 2024, 13.0),
    ('111111111', 1, 2024, 13.0),
    ('222222222', 1, 2024, 13.0),
    ('333333333', 1, 2024, 13.0),
    ('444444444', 1, 2024, 13.0),
    ('555555555', 1, 2024, 10.0),
    ('666666666', 1, 2024, 10.0),
    ('777777777', 1, 2024, 10.0),
    ('888888888', 1, 2024, 10.0),
    ('999999999', 1, 2024, 2.00),

    ('000000000', 2, 2024, 13.0),
    ('111111111', 2, 2024, 13.0),
    ('222222222', 2, 2024, 13.0),
    ('333333333', 2, 2024, 13.0),
    ('444444444', 2, 2024, 13.0),
    ('555555555', 2, 2024, 10.0),
    ('666666666', 2, 2024, 10.0),
    ('777777777', 2, 2024, 10.0),
    ('888888888', 2, 2024, 10.0),
    ('999999999', 2, 2024, 2.00),

    ('000000000', 3, 2024, 13.0),
    ('111111111', 3, 2024, 13.0),
    ('222222222', 3, 2024, 13.0),
    ('333333333', 3, 2024, 13.0),
    ('444444444', 3, 2024, 13.0),
    ('555555555', 3, 2024, 10.0),
    ('666666666', 3, 2024, 10.0),
    ('777777777', 3, 2024, 10.0),
    ('888888888', 3, 2024, 10.0),
    ('999999999', 3, 2024, 1.50);
COMMIT;


BEGIN;
INSERT INTO amigo(NIF1, NIF2) VALUES
    ('000000000', '111111111'),
    ('000000000', '222222222'),
    ('000000000', '333333333'),
    ('000000000', '444444444'),
    ('000000000', '555555555'),
    ('000000000', '666666666'),
    ('000000000', '777777777'),
    ('000000000', '888888888'),

    ('111111111', '000000000'),
    ('222222222', '000000000'),
    ('333333333', '000000000'),
    ('444444444', '000000000'),
    ('555555555', '000000000'),
    ('666666666', '000000000'),
    ('777777777', '000000000'),
    ('888888888', '000000000'),

    ('111111111', '222222222'),
    ('222222222', '111111111'),

    ('333333333', '444444444'),
    ('444444444', '333333333'),

    ('555555555', '666666666'),
    ('666666666', '555555555'),

    ('777777777', '888888888'),
    ('888888888', '777777777'),

    ('111111111', '555555555'),
    ('222222222', '555555555'),
    ('333333333', '888888888'),
    ('444444444', '222222222'),

    ('555555555', '111111111'),
    ('555555555', '222222222'),
    ('888888888', '333333333'),
    ('222222222', '444444444');
COMMIT;


-- 1 por semana nos regulares e 3 por semana nos frequentes. considerar só 8 semanas e 4 semanas por mês
BEGIN;
INSERT INTO alugar(NIF, IdExemplar, Valor, Semana, Ano) VALUES 
    ('000000000', 'memorialConvento1',  1,  1,  2024),
    ('000000000', 'anoRicardoReis1',    1,  1,  2024),
    ('000000000', 'ensaioCegueira1',    1,  1,  2024),

    ('000000000', 'memorialConvento1',  1,  2,  2024),
    ('000000000', 'anoRicardoReis1',    1,  2,  2024),
    ('000000000', 'ensaioCegueira1',    1,  2,  2024),

    ('000000000', 'memorialConvento1',  1,  3,  2024),
    ('000000000', 'anoRicardoReis1',    1,  3,  2024),
    ('000000000', 'ensaioCegueira1',    1,  3,  2024),

    ('000000000', 'memorialConvento1',  1,  4,  2024),
    ('000000000', 'anoRicardoReis1',    1,  4,  2024),
    ('000000000', 'ensaioCegueira1',    1,  4,  2024),


    ('111111111', 'harryPotter1',       1,  1,  2024), 
    ('111111111', '1984_1',             1,  1,  2024), 
    ('111111111', 'osmaias1',           1,  1,  2024), 

    ('111111111', 'harryPotter1',       1,  2,  2024), 
    ('111111111', '1984_1',             1,  2,  2024), 
    ('111111111', 'osmaias1',           1,  2,  2024), 

    ('111111111', 'harryPotter1',       1,  3,  2024), 
    ('111111111', '1984_1',             1,  3,  2024), 
    ('111111111', 'osmaias1',           1,  3,  2024), 

    ('111111111', 'harryPotter1',       1,  4,  2024), 
    ('111111111', '1984_1',             1,  4,  2024), 
    ('111111111', 'osmaias1',           1,  4,  2024),


    ('222222222', 'memorialConvento2',  1,  1,  2024),
    ('222222222', 'osmaias2',           1,  1,  2024),
    ('222222222', 'harryPotter2',       1,  1,  2024),

    ('222222222', 'memorialConvento2',  1,  2,  2024),
    ('222222222', 'osmaias2',           1,  2,  2024),
    ('222222222', 'harryPotter2',       1,  2,  2024),

    ('222222222', 'memorialConvento2',  1,  3,  2024),
    ('222222222', 'osmaias2',           1,  3,  2024),
    ('222222222', 'harryPotter2',       1,  3,  2024),

    ('222222222', 'memorialConvento2',  1,  4,  2024),
    ('222222222', 'osmaias2',           1,  4,  2024),
    ('222222222', 'harryPotter2',       1,  4,  2024),


    ('333333333', 'anoRicardoReis2',    1,  1,  2024),
    ('333333333', 'lusíadas1',          1,  1,  2024),
    ('333333333', 'aventuraCidade1',    1,  1,  2024),

    ('333333333', 'anoRicardoReis2',    1,  2,  2024),
    ('333333333', 'lusíadas1',          1,  2,  2024),
    ('333333333', 'aventuraCidade1',    1,  2,  2024),

    ('333333333', 'anoRicardoReis2',    1,  3,  2024),
    ('333333333', 'lusíadas1',          1,  3,  2024),
    ('333333333', 'aventuraCidade1',    1,  3,  2024),

    ('333333333', 'anoRicardoReis2',    1,  4,  2024),
    ('333333333', 'lusíadas1',          1,  4,  2024),
    ('333333333', 'aventuraCidade1',    1,  4,  2024),


    ('444444444', 'mensagem1',          1,  1,  2024),
    ('444444444', 'sermaoAntonio1',     1,  1,  2024),
    ('444444444', 'aventuraCidade2',    1,  1,  2024),

    ('444444444', 'mensagem1',          1,  2,  2024),
    ('444444444', 'sermaoAntonio1',     1,  2,  2024),
    ('444444444', 'aventuraCidade2',    1,  2,  2024),

    ('444444444', 'mensagem1',          1,  3,  2024),
    ('444444444', 'sermaoAntonio1',     1,  3,  2024),
    ('444444444', 'aventuraCidade2',    1,  3,  2024),

    ('444444444', 'mensagem1',          1,  4,  2024),
    ('444444444', 'sermaoAntonio1',     1,  4,  2024),
    ('444444444', 'aventuraCidade2',    1,  4,  2024),


    ('555555555', 'ensaioCegueira2',    2,  1,  2024),
    ('555555555', 'anoRicardoReis3',    2,  2,  2024),
    ('555555555', 'mensagem2',          2,  3,  2024),
    ('555555555', 'sermaoAntonio2',     2,  4,  2024),
    

    ('666666666', 'anoRicardoReis3',    2,  1,  2024),
    ('666666666', 'sermaoAntonio2',     2,  2,  2024),
    ('666666666', '1984_2',             2,  3,  2024),
    ('666666666', 'lusíadas2',          2,  4,  2024),


    ('777777777', '1984_2',             2,  1,  2024),
    ('777777777', 'lusíadas2',          2,  2,  2024),
    ('777777777', 'ensaioCegueira2',    2,  3,  2024),
    ('777777777', 'ensaioCegueira2',    2,  4,  2024),


    ('888888888', '1984_3',             2,  1,  2024),
    ('888888888', '1984_3',             2,  2,  2024),
    ('888888888', 'anoRicardoReis3',    2,  3,  2024),
    ('888888888', 'mensagem2',          2,  4,  2024),



    ('111111111', 'memorialConvento1',  1,  5,  2024),
    ('111111111', 'anoRicardoReis1',    1,  5,  2024),
    ('111111111', 'ensaioCegueira1',    1,  5,  2024),

    ('111111111', 'memorialConvento1',  1,  6,  2024),
    ('111111111', 'anoRicardoReis1',    1,  6,  2024),
    ('111111111', 'ensaioCegueira1',    1,  6,  2024),

    ('111111111', 'memorialConvento1',  1,  7,  2024),
    ('111111111', 'anoRicardoReis1',    1,  7,  2024),
    ('111111111', 'ensaioCegueira1',    1,  7,  2024),

    ('111111111', 'memorialConvento1',  1,  8,  2024),
    ('111111111', 'anoRicardoReis1',    1,  8,  2024),
    ('111111111', 'ensaioCegueira1',    1,  8,  2024),


    ('000000000', 'harryPotter1',       1,  5,  2024), 
    ('000000000', '1984_1',             1,  5,  2024), 
    ('000000000', 'osmaias1',           1,  5,  2024), 

    ('000000000', 'harryPotter1',       1,  6,  2024), 
    ('000000000', '1984_1',             1,  6,  2024), 
    ('000000000', 'osmaias1',           1,  6,  2024), 

    ('000000000', 'harryPotter1',       1,  7,  2024), 
    ('000000000', '1984_1',             1,  7,  2024), 
    ('000000000', 'osmaias1',           1,  7,  2024), 

    ('000000000', 'harryPotter1',       1,  8,  2024), 
    ('000000000', '1984_1',             1,  8,  2024), 
    ('000000000', 'osmaias1',           1,  8,  2024),


    ('333333333', 'memorialConvento2',  1,  5,  2024),
    ('333333333', 'osmaias2',           1,  5,  2024),
    ('333333333', 'harryPotter2',       1,  5,  2024),

    ('333333333', 'memorialConvento2',  1,  6,  2024),
    ('333333333', 'osmaias2',           1,  6,  2024),
    ('333333333', 'harryPotter2',       1,  6,  2024),

    ('333333333', 'memorialConvento2',  1,  7,  2024),
    ('333333333', 'osmaias2',           1,  7,  2024),
    ('333333333', 'harryPotter2',       1,  7,  2024),

    ('333333333', 'memorialConvento2',  1,  8,  2024),
    ('333333333', 'osmaias2',           1,  8,  2024),
    ('333333333', 'harryPotter2',       1,  8,  2024),


    ('222222222', 'anoRicardoReis2',    1,  5,  2024),
    ('222222222', 'lusíadas1',          1,  5,  2024),
    ('222222222', 'aventuraCidade1',    1,  5,  2024),

    ('222222222', 'anoRicardoReis2',    1,  6,  2024),
    ('222222222', 'lusíadas1',          1,  6,  2024),
    ('222222222', 'aventuraCidade1',    1,  6,  2024),

    ('222222222', 'anoRicardoReis2',    1,  7,  2024),
    ('222222222', 'lusíadas1',          1,  7,  2024),
    ('222222222', 'aventuraCidade1',    1,  7,  2024),

    ('222222222', 'anoRicardoReis2',    1,  8,  2024),
    ('222222222', 'lusíadas1',          1,  8,  2024),
    ('222222222', 'aventuraCidade1',    1,  8,  2024),


    ('444444444', 'mensagem1',          1,  5,  2024),
    ('444444444', 'sermaoAntonio1',     1,  5,  2024),
    ('444444444', 'aventuraCidade2',    1,  5,  2024),

    ('444444444', 'mensagem1',          1,  6,  2024),
    ('444444444', 'sermaoAntonio1',     1,  6,  2024),
    ('444444444', 'aventuraCidade2',    1,  6,  2024),

    ('444444444', 'mensagem1',          1,  7,  2024),
    ('444444444', 'sermaoAntonio1',     1,  7,  2024),
    ('444444444', 'aventuraCidade2',    1,  7,  2024),

    ('444444444', 'mensagem1',          1,  8,  2024),
    ('444444444', 'sermaoAntonio1',     1,  8,  2024),
    ('444444444', 'aventuraCidade2',    1,  8,  2024),


    ('666666666', 'ensaioCegueira2',    2,  5,  2024),
    ('666666666', 'anoRicardoReis3',    2,  6,  2024),
    ('666666666', 'mensagem2',          2,  7,  2024),
    ('666666666', 'sermaoAntonio2',     2,  8,  2024),
    

    ('555555555', 'anoRicardoReis3',    2,  5,  2024),
    ('555555555', 'sermaoAntonio2',     2,  6,  2024),
    ('555555555', '1984_2',             2,  7,  2024),
    ('555555555', 'lusíadas2',          2,  8,  2024),


    ('888888888', '1984_2',             2,  5,  2024),
    ('888888888', 'lusíadas2',          2,  6,  2024),
    ('888888888', 'ensaioCegueira2',    2,  7,  2024),
    ('888888888', 'ensaioCegueira2',    2,  8,  2024),


    ('777777777', '1984_3',             2,  5,  2024),
    ('777777777', '1984_3',             2,  6,  2024),
    ('777777777', 'anoRicardoReis3',    2,  7,  2024),
    ('777777777', 'mensagem2',          2,  8,  2024);

COMMIT;


BEGIN;
INSERT INTO avalia(NIF, ISBN, Estrelas, Comentario) VALUES 
    ('000000000', '0000011111', 5, 'Muito bom'),
    ('111111111', '4444455555', 4, 'Bom'),
    ('222222222', '0000011111', 3, 'Razoável'),
    ('333333333', '2222233333', 2, 'Mais ou menos'),
    ('444444444', '5555566666', 0, 'Muito mau'),
    ('555555555', '3333344444', 5, 'Muito bom'),
    ('666666666', '6666677777', 4, 'Bom'),
    ('777777777', '8888899999', 3, 'Razoável'),
    ('888888888', '5555566666', 2, 'Mais ou menos'),
    ('999999999', '8888899999', 1, 'Mau'),
    ----------------------------------------
    ('000000000', '2222233333', 1, 'Mau'),
    ('111111111', '7777788888', 2, 'Mais ou menos'),
    ('222222222', '1111122222', 1, 'Mau'),
    ('333333333', '8888899999', 1, 'Mau'),
    ('444444444', '6666677777', 0, 'Muito mau'),
    ('555555555', '2222233333', 4, 'Bom'),
    ('666666666', '2222233333', 5, 'Muito bom'),
    ('777777777', '7777788888', 3, 'Razoável'),
    ('888888888', '2222233333', 2, 'Mais ou menos'),
    ('999999999', '6666677777', 5, 'Muito bom'),
    ----------------------------------------------
    ('000000000', '3333344444', 1, 'Mau'),
    ('111111111', '1111122222', 0, 'Muito mau'),
    ('222222222', '4444455555', 3, 'Razoável'),
    ('333333333', '9999900000', 2, 'Mais ou menos'),
    ('444444444', '9999900000', 2, 'Mais ou menos'),
    ('555555555', '5555566666', 1, 'Mau'),
    ('666666666', '7777788888', 5, 'Muito bom'),
    ('777777777', '9999900000', 5, 'Muito bom'),
    ('888888888', '7777788888', 4, 'Bom'),
    ('999999999', '0000011111', 3, 'Razoável');
COMMIT;


BEGIN;
INSERT INTO encomenda_livro(ISBN, NIFed, DataE, NoExemplares) VALUES 
   ('0000011111', '000000000', '2024-01-15', 2),
   ('2222233333', '000000000', '2024-01-15', 3),
   ('4444455555', '111111111', '2024-01-31', 1),
   ('1111122222', '222222222', '2024-02-02', 2);
COMMIT;
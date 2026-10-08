# Agente interativo de Batalha Naval - Relatório

## Autores
- Miguel Pombeiro, 57829
- Miguel Rocha, 58501

## Estruturas de dados utilizadas

### Barcos

Para separar os barcos nos seus devidos tipos, decidimos utilizar a seguinte estrutura de dados, representada por um tipo soma:

```ocaml
type boat_type =
  | Carrier
  | Destroyer
  | Frigate
  | TorpedoBoat
  | Submarine
```


### Tabuleiros

Os nossos tabuleiros são compostos por uma "matriz", ou seja, um array de arrays que contém as células do tabuleiro. Estas células do tabuleiro podem ter um significado semelhante para ambos os tabuleiros (ataque e defesa), ou podem só pertencer a um desses tabuleiros. Sendo assim os tipos escolhidos foram os seguintes: 

```ocaml
type board_cell = 
  | Unknown
  | Hit of boat_type
  | Boat of boat_type
  | HasBeenHit of boat_type
  | CantPlaceHere
  | Water
```


#### Células disponíveis para o tabuleiro de ataque

As células escolhidas para o tabuleiro de ataque podem ser de três tipos:

- `Unknown`, que representa uma célula não vista e que poderá ser usada na escolha do próximo ataque. Caso esta célula contenha um barco, ao acertá-la, passará a ser do tipo `Hit`, descrita a seguir.

- `Hit`, que representa uma célula de acerto num barco, e que não deve ser voltado a tentar acertar. Não foi necessário criar outro tipo para o afundamento do barco, devido à estratégia utilizada para o ataque, já que esta não requere esta informação.

- `Water`, que representa uma célula já atacada e que não continha um barco. Devido à estratégia utilizada, são também marcadas as vizinhanças do afundamento do barco como água, não sendo portanto necessário de ter a célula atacada, para esses casos. 


#### Células disponíveis para o tabuleiro de defesa

As celulas escolhidas para o tabuleiro de defesa podem ser de quatro tipos:

- `Boat`, que representa um barco colocado nessa célula. Esta célula será de um tipo específico de barco, de modo a que o agente possa identificar qual o barco que foi atingido, para poder comunicar ao oponente.
- `HasBeenHit`, que representa uma célula que já foi atacada pelo oponente, sendo ainda necessária para identificar o afundamento do barco do nosso lado.
- `CantPlaceHere`, que representa uma célula onde não é possível colocar um barco, normalmente, porque já existe um barco adjacente a essa célula. Esta célula foi utilizada para facilitar a colocação dos barcos na geração aleatória de tabuleiros de defesa .
- `Water`, que representa uma célula sem barco, ou seja, água.


### Estado do jogo

As coordenadas de um barco são representadas por um par de inteiros:

```ocaml
type coord = int * int
```

Estas coordenadas são utilizadas na seguinte estrutura, que contém o estado de um barco, isto é, que contém o barco, composto por uma lista de coordenadas que contém todas as células que o barco contém. Este tipo também tém um valor (remaining) que indica o número de células restantes do barco, ou seja o número de células que falta acertar pelo oponente.

```ocaml
type ship_state = {
  name: boat_type;
  cells: coord list;
  mutable remaining: int;
}
```


O estado do jogo é portanto, representado pela seguinte estrutura de dados:

```ocaml
type state = {
  mutable board_size: int;
  mutable defense_board: board_cell array array;
  mutable attack_board: board_cell array array;
  
  mutable ships: ship_state list;
  mutable remaining_ships: int;

  mutable targets_to_try: (int * int) list;
  mutable last_hit_ship: boat_type option;
  mutable last_boat_hits: (int * int) list;
  mutable last_chess_shot: (int * int);
}
```

Nela estão representados os seguintes elementos:
- `board_size`: tamanho N do tabuleiro (N x N).
- `defense_board`: tabuleiro de defesa, onde está representado o estado dos barcos do agente. Contém células do tipo `board_cell`, já descrito anteriormente. É atualizado sempre que o oponente ataca.
- `attack_board`: tabuleiro de ataque, onde está representado o estado dos ataques do agente ao oponente. Contém células do tipo `board_cell`, já descrito anteriormente. É atualizado sempre que o agente ataca.
- `ships`: lista dos barcos do agente, representados pelo tipo `ship_state`, já descrito anteriormente.
- `remaining_ships`: número de barcos que ainda não foram afundados pelo oponente. Quando este valor chega a 0, o agente perde o jogo.

-`targets_to_try`: lista de coordenadas que representam os alvos a tentar atacar. Esta lista é utilizada na estratégia de ataque do agente, onde são adicionadas coordenadas adjacentes a um acerto para tentar afundar o barco. Esta lista é atualizada sempre que o agente acerta num barco, de acordo com a estratégia descrita adiante.

-`last_hit_ship`: Tipo opcional de barco do último acerto feito pelo agente, de modo a personalizar a estratégia de ataque com base neste. 

-`last_boat_hits`: Lista que contém os acertos dos barcos, é também utilizada para inferir o próximo ataque, com a nossa estratégia customizada.

-`last_chess_shot`: Último tiro usado na nossa estrátegia para percorrer o tabuleiro eficiente, descrita na implementação da nossa estratégia. Este é guardado de modo a otimizá-la ao usar o último tiro realizado em xadrez como referência.



## Estratégia de "Inteligência Artificial" implementada

A nossa estratégia de "Inteligência Artificial" implementada foi inspirada na fornecida no enunciado, bem como recorrendo a algumas pequenas alterações que a tornaram "mais esperta". Sendo assim é possível dividi-la nas seguintes partes:

### Procura no tabuleiro
  De modo a tornar a procura no tabuleiro mais eficiente, decidimos usar uma estratégia em xadrez que nos permite adivinhar a maioria dos barcos existentes em apenas uma passagem ao tabuleiro. Esta estratégia usa um padrão em xadrez (l + c) ímpar, de cima para baixo para testar a próxima célula de forma eficiciente. No entanto, a estratégia tem uma desvantagem, uma vez que pode não acertar no submarino numa única passagem ao tabuleiro, sendo portanto necessário de "adivinhar" à sorte (aleatoriamente) posteriormente. 
  
  Seria também possível realizar o padrão de xadrez alternado de cima para baixo e de baixo para cima, ou até aleatoriamente em xadrez, mas decidimos implementar de cima para baixo de modo a simplificar o trabalho.


### Modo caça
  Após obter uma primeira informação de "tiro", o agente entra no modo "caça", pausando a procura em xadrez. Neste modo, é utilizada a lista `targets_to_try`, que é atualizada sempre que o agente acerta num barco. Quando esta lista contém algum elemento, o agente irá atacar essas coordenadas, de modo a tentar afundar o barco o mais rapidamente possível. Para tal, quando ocorre um acerto num barco, são adicionadas à lista as coordenadas adjacentes (cima, baixo, esquerda, direita) à célula acertada, desde que estas estejam dentro do tabuleiro e ainda não tenham sido atacadas.

  Caso se trate de um submarino (barco de tamanho 1), o agente não adiciona coordenadas à lista, uma vez que o barco é afundado imediatamente.

### Modo destruição
  Quando o agente acerta novamente num barco (acertou 2+ tiros), o agente vai inferir a orientação do barco (horizontal ou vertical), com base nas coordenadas dos acertos anteriores. Esta inferência é feita ao verificar se as coordenadas dos acertos anteriores têm a mesma linha ou a mesma coluna. Com base nesta inferência, o agente irá atualizar a lista `targets_to_try`, removendo as coordenadas que não estão alinhadas. Por exemplo, se o agente inferir que o barco está na vertical, irá remover todas as coordenadas da lista que não têm a mesma coluna que os acertos anteriores. Por fim, o agente irá, ainda, adicionar à lista as coordenadas adjacentes ao último acerto, que estão na mesma orientação inferida. 
  
  Esta estratégia é utilizada tanto para Fragatas como para os Destroyers e permite ao agente focar os seus ataques nas coordenadas mais prováveis de conter o restante do barco, aumentando a eficiência. Para estes barcos, o agente continuará à procura das coordenadas adjacentes utilizando esta estratégia, até que o barco seja afundado.

  Quando o barco é afundado, o agente limpa a lista `target_to_try` volta modo de procura em xadrez.

### Alteração ao modo destruição
  Como o modo de destruição não funcionava para o porta-aviões, decidimos realizar uma extensão ao mesmo, de modo a torná-lo também mais "esperto".
  Ao analisar as peças do porta-aviões reparámos que em qualquer orientação que este estivesse, existria sempre uma "linha" ou "coluna" com três células consecutivas, o que nos permitiu continuar com a estratégia base de destruição. Ao acertar essa linha/coluna, é apenas necessário marcar mais 4 possíveis tiros, três destes num dos lados dessa linha/coluna na mesma orientação, e outro tiro no lado oposto no centro, como se pode ver no exemplo seguinte: 
  
      |---|---|---|---|---|     |---|---|---|---|---|    
      |   |   |   |   |   |     |   |   |   |   |   |     Legenda:    
      |---|---|---|---|---|     |---|---|---|---|---|     x -> barco
      |   | ? | x |   |   |     |   | ? | ? | ? |   |     ? -> previsão
      |---|---|---|---|---|     |---|---|---|---|---|
      |   | ? | x | ? |   |     |   | x | x | x |   |
      |---|---|---|---|---|     |---|---|---|---|---|
      |   | ? | x |   |   |     |   |   | ? |   |   |
      |---|---|---|---|---|     |---|---|---|---|---|
      |   |   |   |   |   |     |   |   |   |   |   |
      |---|---|---|---|---|     |---|---|---|---|---|

Ao realizar isso evitamos tiros desnecessários, e ao acertar qualquer uma destas posições conseguimos descobrir a última célula, independente de qualquer uma das quatro possíveis rotações. A estratégia para "adivinhar" o último tiro é feita da seguinte forma:

- Inferimos a orientação, usando os primeiros 3 tiros tal como anteriormente, neste caso usando a tail da lista, já que os tiros mais recentes são adicionados na cabeça.

- Se a orientação é vertical, então verificamos o último tiro. Caso este tiro, seja numa coluna que não tenha como linha o meio da "coluna única", então temos a certeza que o formato do T continua no outro lado dessa coluna (espelho). Caso o acerto seja numa coluna da linha do meio, então sabemos que a direção continua para o lado desse acerto (esquerda ou direita da "coluna única").

- Se a orientação é horizontal, então verificamos o último tiro. Caso este tiro, seja numa linha que não seja o meio da "linha única" então temos a certeza que o formato do T continua no outro lado dessa linha (espelho). Caso o acerto seja numa linha da coluna do meio, então sabemos que a direção continua para o lado desse acerto (cima ou baixo).

### Uso da informação afundado
  Tal como foi referido no enunciado, a informação recebida que um navio foi afundado é muito importante, já que nos permite parar de procurar o barco (porque já foi afundado), bem como que permite marcar as células adjacentes ao barco afundado como água, otimizando os próximos disparos. Esta estratégia é muito importante, já que pode minimizar, no máximo, `nCells*2+6` ataques para os barcos comuns e `nCells*3+1` para o porta-aviões. 


## Instruções de compilação e execução


### Compilar

De forma a compilar o agente basta abrir o terminal no diretório onde se encontra o código fonte e executar o seguinte comando:

```shell
ocamlopt -o agente GameState.ml Utils.ml Defense.ml Attack.ml Main.ml
```


### Executar
Para executar o agente, basta correr o seguinte comando no terminal:

```shell
./agente
```


## Melhorias futuras

Uma possível melhoria futura seria tentar eliminar o uso de mutáveis na nossa implementação, de modo a tornar o código mais funcional e aproveitar melhor as características da linguagem OCaml. Poderíamos adotar uma abordagem semelhante à utilizada em Prolog, onde o estado do jogo seria passado como um argumento para as funções, sendo atualizado e retornado conforme necessário. Isso permitiria evitar efeitos colaterais e tornar o código mais previsível e fácil de testar. No entanto, essa mudança exigiria uma reformulação significativa da lógica do agente, especialmente na forma como é feita a gestão do estado do jogo e as listas de alvos a tentar, que acabamos por não ter tempo de implementar.

Relativamente à utilização de nomes arbitrários na inicialização de um tabuleiro de jogo personalizado, optamos por não fazer essa funcionalidade, uma vez que o enunciado não estava especificado e acabamos por não ficar esclarecidos sobre como deveria ser implementado. Esta "feature" seria bastantante simples de implementar, no entanto não sabemos como seria feita a comunicação entre os agentes, ou seja, se teriamos de responder com "tiro fragata" ou "tiro carolina".

## Dificuldades encontradas

Durante a implemetação do agente, uma das principais dificuldades encontradas foi durante a fase de testes, uma vez que o agente tinha de ser testado contra outro agente e não nos foi facultada o script e as configurações que irão ser utilizadas durante o torneio. Isto leva a que os testes realizados possam não ir de encontro com todas as configurações possíveis do torneio.
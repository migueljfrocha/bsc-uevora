# TrabalhoFinalMDS: Eventastic

## Introdução
O objetivo deste trabalho é implementar um sistema de gestão de eventos, denominado Eventastic. Este sistema surge da necessidade de apoiar o processo de organização, inscrição, pagamento e participação em atividades como conferências, workshops, encontros temáticos, entre outros.

O Eventastic é um sistema de gestão de eventos que permite centralizar toda a informação relacionada com eventos e a sua gestão. O sistema permite aos administradores criar e gerir eventos, bem como gerir as inscrições de participantes. Os particiantes, para além de poderem consultar todas as informações sobre eventos disponíveis, podem, também, inscrever-se, bem como consultar as informações relativas à sua participação no evento e a pagamentos associados.

Nesta fase do trabalho, foi implementada o sistema de acordo com o diagrama de classes e os _use cases_ definidos na primeira parte do trabalho (na Wiki do repositório). A implementação foi realizada em Java, sendo que, tal como foi imposto no enunciado, o sistema não utiliza uma base de dados, armazenando toda a informação em memória. Para além disso, o sistema foi implementado como uma biblioteca, com uma API definida, que pode ser utilizada a partir de outras aplicações. Assim sendo, e de modo a mostrar a sua funcionalidade, foi, ainda, implementada um pequeno programa de linha de comandos, que permite exemplificar e verificar o funcionamento do sistema.


## Decisões da implementação

Ao longo do desenvolvimento do sistema, foram tomadas várias decisões. Algumas destas decisões impactaram drasticamente o rumo do projeto, sendo importante discutí-las aqui.

Relativamente ao diagrama de classes e à especificação inicial nele definida, é possível identificar algumas diferenças em comparação com o que foi implementado. Ao nível das Opções Adicionais para eventos, é possível verificar que estas não tinham uma representação na classe Evento, o que tornava impossível a sua persistência no sistema. Desta forma, foi necessário adicionar uma lista de Opções Adicionais na classe Evento, que guarda todas as opções associadas a esse evento. Desta forma, tornou-se possível gerir as Opções Adicionais de cada evento, bem como associá-las às inscrições dos participantes. Para além disso, as opções adicionais deixaram de ter uma data limite de inscrição própria, passando a utilizar a data de inscrição do evento ao qual estão associadas. 
Ainda na classe Evento, foram implementados os métodos de criação (construtor), edição e cancelamento de eventos, que, no diagrama de classes estavam associados à classe Administrador. Esta decisão prendeu-se principalmente com uma questão de organização do código, bem como de simplificação da implementação, uma vez que estas funcionalidades estão diretamente relacionadas com o Evento.

Apesar de não ser necessário o controlo de acessos, decidimos, ainda assim, realizar verificações simples, como por exemplo, garantir que só o dono do evento pode realizar operações no mesmo, entre outros. 

Pelo facto de existirem restrições ao enunciado, decidimo não implementar alguns métodos relativos à autenticação como criar conta, não implementar a classe check-in, nem realizar a confirmação de pagamento, ou seja, o pagamento manter-se-á sempre no mesmo estado (CONFIRMADO). Também relativamente ao check-in, não foi implementado o anexo/registo do comprovativo de pagamento, não só devido à restrição de implementação, mas também devido à complexidade envolvida nesta operação.

Também devido à complexidade envolvida, não foram implementados quaisquer envios de e-mail, como a notificação por e-mail do estado de inscrição do participante, ou até ao convite de operadores. Sendo assim, foi necessário implementar a classe `Invite`, que representa um convite ao operador de modo a que este consiga gerir um evento. Este convite é criado por um administrador e contém a informação necessária para que o operador possa ser associado ao evento, caso aceite.

Uma vez que o sistema implica a gestão de datas e prazos, foi necessário garantir a sua consistência. Para isso, foi utilizada a classe `DateInterval` que permite representar um intervalo de datas e verificar a sua validade. Esta classe auxiliar foi utilizada em várias partes do sistema, como por exemplo na criação/edição, de forma a garantir que as datas são coerentes.

Como no encuniado é pedida a implementação do sistema como uma biblioteca, foi criada a classe `Eventastic`, que funciona como a interface principal do sistema. Esta classe contém os métodos necessários para interagir com o sistema e que encapsulam as suas funcionalidades principais. Desta forma, o sistema tornou-se mais modular e fácil de utilizar por outras aplicações.
    
Por fim, tinhamos planeado desenhar testes unitários com recurso à biblioteca JUnit, no entanto, após desenharmos o primeiro conjunto de testes, decidimos não prosseguir com esse plano. Assim, para as restantes classes implementadas foram, apenas, realizados testes manuais antes de enviar para o repositório.


## Dificuldades encontradas

A nossa primeira dificuldade foi no planeamento do projeto, já que este tinha que ser coerente com o diagrama de classes, bem como com os _use cases_ definidos na primeira parte do trabalho. Sendo assim, foi necessário ter a certeza do que implementar e do que não implementar, de modo a cobrir todas as funcionalidades descritas. Este planeamento foi importante para a definição das _issues_ que acabou por definir o rumo do projeto.

A nossa implementação acabou por tornar-se muito complexa, pelo facto de termos realizado uma específicação dos _use cases_ e requisitos demasiado detalhada, obrigando-nos a torná-la mais simples, como por exemplo, o facto de termos referido certos formatos de ficheiros.

Algumas das restrições impostas no enunciado acabaram por dificultar a nossa implementação, como por exemplo o facto de não podermos utilizar base de dados. Isto deve-se ao facto de termos planeado o nosso diagrama de classes (definido na primeira parte do trabalho), para o uso das mesmas. Desta forma, tivemos que fazer adaptações para que a informação fosse guardada em memória, o que acabou por dificultar a implementação de algumas funcionalidades, nomeadamente ao nível da gestão de entidades (e.g. identificadores únicos, processos de procura).

Por fim, a gestão de versões e _branches_ também representou alguma dificuldade, especialmente no início, mas que rapidamente foi superada após ver com atenção os exemplos da aula teórica.


## Balanço crítico e melhorias futuras

O sistema implementado cumpre com os requisitos definidos na primeira parte do trabalho, bem como com as restrições e funcinalidades impostas no enunciado. Ainda assim, existem algumas melhorias que poderiam ser implementadas, de forma a tornar o sistema mais funcional e eficiente.

Ao nível da funcionalidade, seria interessante implementar o envio de notificações por e-mail, tanto para os participantes como para os administradores. Estas notificações poderiam incluir confirmações de inscrições, entre outros. O suporte para anexar comprovativos de pagamento e a exportação de ficheiros em formatos mais comuns, como PDF, seriam também melhorias interessantes de implementar.

Relativamente à performance do sistema, a utilização de estruturas de dados mais eficientes, como _hash maps_, poderia melhorar significativamente a velocidade de acesso aos dados, especialmente em situações com um grande número de eventos e participantes.

## Membros do grupo

[Miguel Pombeiro, 57829](https://github.com/MiguelPombeiro)

[Miguel Rocha, 58501](https://github.com/migueljfrocha)
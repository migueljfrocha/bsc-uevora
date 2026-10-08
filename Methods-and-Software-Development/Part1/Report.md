# Relatório de Trabalho

## Objetivo

O objetivo deste trabalho é especificar e modelar o sistema **Eventastic v1.0**, uma aplicação de gestão de eventos que apoia o processo de organização, inscrição, pagamento e participação em atividades como conferências, *workshops*,  encontros temáticos, entre outros.

Pretende‑se fazer a especificação dos requisitos de utilizador e de sistema, dos *use cases* principais e dos diagramas de classe, de forma a servir de base ao desenvolvimento de *backend* para a plataforma.


## Introdução

A instituição promotora realiza regularmente eventos dirigidos a estudantes e não estudantes, recorrendo atualmente a formulários dispersos e folhas de cálculo para gerir inscrições, datas limite e pagamentos. Este processo "manual" acaba por dificultar todo o processo de gestão necessário, como o controlo do número de participantes, o cumprimento de prazos, a confirmação dos pagamentos, o que aumenta o risco de erros e o trabalho administrativo.

O sistema **Eventastic** surge para centralizar toda a informação relacionada com eventos, inscrições, fases de inscrição, opções adicionais e pagamentos, permitindo gerir vários eventos em simultâneo e disponibilizar aos participantes um processo de inscrição online simples e transparente.
 
No âmbito deste trabalho foram identificados os diferentes tipos de utilizadores da plataforma (administrador, participante e operador) e definidos os respetivos requisitos, [requisitos de utilizador](./UserRequirements.md). A partir destes, foram derivados os [requisitos de sistema](./SystemRequirements.md), elaborados os [*use cases* textuais](./UseCasesTextualDescription.md) e produzidos o [diagrama de *use cases*](./UseCaseDiagram.md) e o [diagrama de classes](./ClassDiagram.md).


## Decisões Tomadas

Ao longo do processo de especificação e desenvolvimento do sistema, foram encontrados vários pontos que careciam de clarificação e para os quais tiveram de ser tomadas decisões. Estas decisões influenciam diretamente a estrutura do sistema e o seu funcionamento. Foram, também, tomadas algumas decisões ao nível da forma como os requisitos foram organizados e apresentados. Nesta secção são descritas as principais decisões tomadas durante o desenvolvimento do trabalho.

De forma a modelar os atores do sistema, foram definidos três tipos de utilizadores distintos, de acordo com as suas responsabilidades e ações que podem realizar: 
- **Administrador**: é responsável pela criação, gestão, configuração de eventos e todas as atividades relacionadas, bem como pela validação e registo manual de pagamentos. 
- **Operador**: apoia o processo no dia do evento, podendo realizar pesquisas de participantes para fazer o *check-in* e, quando autoriazado, registar comprovativos de pagamento. 
- **Participante**: pode inscrever-se nos eventos e consultar o estado das suas inscrições.
Esta separação permite uma gestão mais eficiente, garantindo que cada utilizador apenas tem acesso às funcionalidades relevantes para o seu papel.

Os eventos são modelados como entidades centrais do sistema, com um conjunto de atributos neceessários para a sua definição e gestão. As inscrições são sempre associadas a um evento específico, a um participante e a um tipo de inscrição (estudante ou não estudante), incluindo informação sobre a fase de inscrição, as opções adicionais selecionadas e o estado do pagamento. Desta forma, é possível gerir múltiplos eventos em simultâneo, cada um com as suas próprias regras e configurações.

Ao nível das diferentes fases de inscrição, optou-se por fazer a sua modelação como entidades próprias, assoiadas a cada evento. Cada fase tem uma data de ínicio e fim, bem como preços diferenciados por tipo de inscrição. O sistema determina automaticamente a fase aplicável com base na data/hora de inscrição, o que garante que as fases não se sobrepõem. O valor final da inscrição é calculado somando o preço base (considerando tipo e fase de inscrição) ao cuso das opções adicionais selecionadas.

As opções adicionais são também modeladas como entidades associadas a cada evento, permitindo definir diferentes tipos de opções (almoço, *coffee-break*) e têm a indicação de obrigatoriedade, custo e, quando aplicável, uma data limite própria. Decidimos, ainda, que opções obrigatórias só podem ser definidas aquando da criação do evento e são automaticamente selecionadas no momento da inscrição. 

Os pagamentos são exclusivamente realizados por transferência bancária, sendo o registo e validação feitos manualmente pelo administrador. Este processo inclui a associação de cada transferência a uma inscrição específica e a atualização do estado do pagamento.

Para além das decisões relacionadas com a especificação do sistema em si, foram também tomadas algumas decisões relacionadas com a forma como essa especificação foi feita.

De modo a simplificar o diagrama de classes, decidimos omitir os *getters* e *setters*, já que estes "apenas" manipulam propriedades, e apenas complicariam o diagrama ainda mais. Neste diagrama, também tivemos que decidir entre usar classes para cada utilizador do sistema, onde os métodos destas seriam o que estes poderiam fazer, ou então, usar classes extra de controlo e gestão das funcionalidades (e.g. gestor de pagamentos, gestor de eventos). Foi escolhida a primeira opção, para salientar as funcionalidades que cada utilizador poderia fazer.

Relativamente ao diagrama de *use cases*, deixamos a autenticação representada como sendo uma inclusão de quaisquer outras funcionalidades, para salientar que o utilizador só poderá realizá-las após se autenticar, apesar de termos definido como pré condição na descrição dos *use cases* a autenticação. Ponderamos, ainda, colocar o sistema como ator dos *use cases*, mas decidimos não o fazer porque as funcionalidades são sempre realizadas por um utilizador, e o sistema apenas "responde" a essas ações.


## Dificuldades Encontradas

A nossa maior dificuldade foi a transformação inicial do problema num conjunto de requisitos de utilizador e de sistema, coerentes com a descrição do problema. Para tal, foi necessário ler várias vezes a descrição do problema com atenção, bem como os requisitos realizados, de modo a produzir requisitos de qualidade.
Ao realizar o diagrama de classes, tivemos alguma dificuldade na escolha das classes e métodos a representar, de modo a estar coerente com o resto do trabalho. Esta dificuldade esteve relacionada ao facto de termos diferentes utilizadores com diferentes cargos e que realizam operações diferentes.
Por fim, também tivemos alguma dificuldade em decidir o nível de detalhe com que iriamos desenvolver o trabalho, tentando sempre não adicionar demasiado detalhe desnecessário aos requisitos, e aos diagramas.
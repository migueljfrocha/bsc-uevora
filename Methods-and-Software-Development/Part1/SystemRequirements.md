# Requisitos de Sistema

## ADMINISTRADOR

1. "O administrador deve conseguir autenticar-se de modo a ter acesso às funcionalidades de gestão dos eventos."

    1.1. "O sistema deverá permitir a autenticação a partir das credênciais de administrador (e-mail e palavra-passe)"

    1.2. "O sistema deverá permitir a recuperação da palavra-passe através do e-mail associado à conta de administrador."
    
    1.3. "O sistema deve permitir o registo de contas de administrador de eventos."


2. "O administrador deve conseguir criar, editar ou cancelar eventos."

    2.1. "O sistema deve permitir a criação de um novo evento, exigindo as seguintes informações: nome, descrição, local, datas (início e fim), horário, número máximo de participantes e o período global de inscrições."

    2.2. "O sistema deve validar que a data de início do evento é anterior à data de fim do evento e rejeitar datas que não existem."

    2.3. "O sistema deve permitir registar, para cada evento, um local composto pelo nome (se tiver), morada, código postal, cidade e país."

    2.4. "Para cada evento, o sistema deve permitir definir o período global de inscrições com uma data de início e uma data de fim. Deve portanto, garantir que a data de início é anterior à data de fim, e rejeitar as datas que não existem."

    2.5. "O sistema deve permitir a edição de qualquer detalhe do evento, bem como ainda o cancelamento do evento."

    2.6. "O sistema deve permitir a realização de inscrições durante o evento caso exista uma fase de inscrição que abranja esse período."

    2.7. "Após a criação bem sucedida do evento, o sistema deve registá-lo."


3. "O administrador deve conseguir definir várias fases de inscrição, com preços distintos."
    
    3.1. "O sistema deve permitir ao administrador criar, editar e remover fases de inscrição associados a um evento."
    
    3.2. "O sistema deve permitir configurar, para cada fase de inscrição, um nome (por exemplo, early, late, on-site), uma data de início, uma data de fim e um preço por cada tipo de inscrição."

    3.3. "O sistema deve validar que, para o mesmo evento, as fases de inscrição não se sobrepõem."

    3.4. "O sistema deve validar que a data de início de uma fase de inscrição é posterior à data de fim da fase de inscrição imediatamente anterior e rejeitar datas que não existem."

    3.5. "O sistema deve determinar automaticamente a fase de inscrição aplicável a uma inscrição, com base na data e hora em que o participante se inscreve."


4. "O administrador deve conseguir definir preços associados a cada tipo de inscrição."

    4.1. "Para cada fase de inscrição, o sistema deve permitir ao administrador definir preços distintos conforme o tipo de inscrição selecionado (estudante ou não estudante)."

    4.2. "O sistema não deve permitir a conclusão de uma inscrição se não existir preço definido para o tipo de inscrição escolhido."


5. "O administrador deve conseguir definir opções adicionais para o evento, com um certo custo associado."

    5.1. "O sistema deve permitir ao administrador criar opções adicionais, exigindo as seguintes informações: nome, descrição, custo, obrigatoriedade (opcional ou obrigatória) e data limite de inscrição (no caso de ser opcional)."

    5.2. "O sistema deve apenas permitir a adição de opções adicionais obrigatórias aquando a criação do evento, sendo a sua data limite de inscrição igual à data de fim do período global de inscrições."

    5.3. "O sistema deve incluir o custo das opções adicionais selecionadas no cálculo do valor total da inscrição."


6. "Durante o período de gestão, o administrador pode consultar a lista de participantes inscritos num evento, bem como todos os detalhes que lhe estão associados (tipos de inscrição, opções adicionais, fase de inscrição, valor total, estado do pagamento... )."

    6.1. "O sistema deve disponibilizar ao administrador uma listagem dos participantes inscritos num evento."

    6.2. "O sistema deve permitir filtrar a lista de participantes do evento por estado de pagamento (pendente, confirmado) ou por tipo de inscrição (estudante, não estudante)."

    6.3. "O sistema deve permitir ao administrador procurar participantes pelo seu nome ou e-mail na lista de inscrições do evento."

    6.4. "O sistema deve permitir ao administrador consultar os detalhes da inscrição de cada participante, nomeadamente os dados do seu formulário de inscrição."
    
    6.5. "O sistema deve permitir ordenar a lista de participantes por data de inscrição."


7. "O administrador deve conseguir exportar e imprimir a lista de participantes inscritos num evento."
    
    7.1. "O sistema deve permitir ao administrador exportar a lista de participantes de um evento num ficheiro .pdf ou .csv, que pode ser transferido." 

    7.2. "O sistema deve permitir ao administrador do evento imprimir a sua lista de participantes."


8. "O administrador deve conseguir nomear operadores para os eventos que gere, bem como definir as suas permissões (e.g. validar inscrições na hora)." 

    8.1. "O sistema deve permitir ao administrador associar operadores a um evento específico, exigindo o nome e um e-mail para o seu registo."

    8.2. "O sistema deve permitir ao administrador definir as permissões dos operadores associados a um evento (por exemplo: registar comprovativos de pagamento)." 

    8.3. "O sistema deve enviar um e-mail ao operador com um convite para ser operador do evento."

    8.4. "O sistema deve enviar um e-mail ao operador sempre que as suas permissões forem alteradas."

    8.5. "O sistema deve permitir ao administrador remover operadores de um evento ou alterar as suas permissões."
    

9. "O administrador deve conseguir registar a informação das trânsferências bancárias recebidas, associando-as às inscrições dos participantes e inserir notas sobre as mesmas."

    9.1. "O sistema deve permitir que o administrador registe as informações de uma transferência bancária recebida e a associe a uma inscrição."

    9.2. "Para cada transferência bancária registada, o sistema deve exigir as seguintes informações: data da transferência, valor transferido, IBAN de origem, comprovativo da transferência (ficheiro)."

    9.3. "O sistema deve permitir que o administrador insira e edite notas adicionais sobre a transferência bancária registada."


10. "O administrador deve confirmar os pagamentos pendentes e corrigir situações excecionais, relativas ao pagamento."

    10.1. "O sistema deve permitir que o administrador alterar o estado do pagamento de uma inscrição pendente, associando uma transferência bancária registada."

    10.2. "Ao confirmar um pagamento, o sistema deve atualizar automaticamente o estado da inscrição do participante."

    10.3. "O sistema deve permitir ao administrador corrigir situações excecionais de pagamento, como dissociar ou reassociar transferências bancárias a uma inscrição, mediante uma justificação textual."


## PARTICIPANTE

11. "Os participantes devem conseguir consultar a lista de eventos com inscrições abertas."

    11.1. "O sistema deve disponibilizar uma lista de eventos cujo período global de inscrições está aberto."

    11.2. "O sistema deve permitir pesquisar eventos por data, local ou nome do evento."

    11.3. "O sistema deve permitir ordenar a lista de eventos pela sua data de início."


12. "Os participantes devem conseguir ver os detalhes de um evento."
    
    12.1. "O sistema deve apresentar as informações disponíveis para o evento (nome, descrição, local, datas de início e de fim, horário, número máximo de participantes, o período global de inscrições e os detalhes das opções adicionais existentes)."

    12.2. "O sistema deve indicar se ainda existem vagas disponíveis ou se o evento está esgotado."

    12.3. "Caso o evento esteja esgotado, o sistema deve bloquear as inscrições do mesmo, não permitindo mais nenhuma inscrição."


13. "Os participantes devem conseguir inscrever-se nos eventos, e para tal, necessitam de preencher um formulário com os seus dados pessoais, o tipo de inscrição e as opções adicionais associadas ao evento."

    13.1. "O sistema deve disponibilizar um formulário de inscrição que exige os seguintes dados: nome completo, e-mail, telefone, morada, tipo de inscrição (estudante ou não estudante) e a seleção das opções adicionais em que o participante pretende inscrever-se."

    13.2. "Caso o participante se inscreva como estudante, o sistema deve pedir-lhe os dados necessários para verifcar esse estatuto (por exemplo comprovativo de matrícula, ou foto do cartão de estudante)."

    13.3. "O sistema deve determinar automaticamente a fase de inscrição aplicável, com base na data e hora em que o participante se inscreve."

    13.4. "O sistema deve selecionar automaticamente, no formulário de inscrição, as opções adicionais obrigatórias definidas pelo administrador."

    13.5. "O sistema deve calcular o valor total da inscrição somando o preço da fase de inscrição com base no tipo de participante, com o custo das opções adicionais selecionadas pelo participante ou pelo sistema."

    13.6. "O sistema tem de apresentar o valor total da inscrição ao participante antes da submissão do formulário de inscrição."

    13.7. "Após a submissão bem sucedida do formulário, o sistema deve registar a inscrição do participante como pendente e enviar-lhe os dados para o pagamento."


14. "Os participantes devem conseguir consultar as informações necessárias para realizar o pagamento de uma inscrição num evento, bem como dados adicionais como a descrição da transferência."

    14.1. "Após a concretização da inscrição, o sistema deve permitir ao participante consultar os dados necessários para efetuar o pagamento (IBAN, valor a pagar, descrição para a transferência e data limite para o pagamento)."

    14.2. "Dois dias antes da data limite para o pagamento, o sistema deve informar o participante para realizar o pagamento do valor que ainda está em dívida."

    14.3. "Caso a data limite do pagamento expire, a inscrição do participante é cancelada."
    
    14.4. "Após a concretização da inscrição, o sistema deve enviar por e-mail os dados necessários para efetuar o pagamento."
    

15. "Os participantes devem conseguir verificar qual o estado da sua inscrição num evento (pendente, confirmada, cancelada), de forma a confirmar se o seu lugar está garantido."

    15.1. "O sistema deve informar o participante, através do seu e-mail, sempre que o estado da sua inscrição for alterada."
    
    15.2. "O sistema deve atualizar automaticamente o estado da inscrição do participante quando o pagamento é confirmado."


## OPERADOR

16. "O operador deve conseguir autenticar-se de modo a conseguir ter acesso às funcionalidades de operador."

    16.1. "O sistema deve permitir a autenticação a partir das credênciais de operador (e-mail e palavra-passe)."

    16.2. "O sistema deverá permitir a recuperação da palavra-passe através do e-mail associado à conta de operador."

    16.3. "O sistema deve permitir o registo de contas de operador."


17. "O operador deve conseguir aceitar convites para ser operador de um certo evento."
    
    17.1. "O sistema deve permitir ao operador aceitar ou recusar convites para ser operador de um evento."

    17.2. "Após o operador aceitar o convite, o sistema deve conseguir associar a conta do operador ao certo evento." 


18. "O operador deve conseguir procurar participantes pelo nome ou e-mail."

    18.1. "O sistema deve permitir ao operador procurar participantes inscritos no evento pelo seu nome ou e-mail."


19. "O operador deve conseguir consultar o tipo de inscrição, a fase de inscrição, opções adicionais e estado do pagamento, de um certo participante do evento."

    19.1. "O sistema deve permitir ao operador aceder aos detalhes da inscrição de um participante num evento: dados pessoais relevantes, tipo de inscrição, fase de inscrição, opções adicionais selecionadas, valor total e estado do pagamento."

    19.2. "O sistema deve respeitar as permissões atribuídas pelo administrador, impedido o acesso do operador a funcionalidades que lhe estão vedadas."


20. "O operador deve conseguir registar o check-in dos participantes cuja inscrição foi confirmada, no dia do evento."

    20.1. "O sistema deve permitir ao operador registar o check-in os participantes cuja inscrição foi confirmada."

    20.2. "O sistema não deve permitir o check-in de participantes sem inscrição ou cuja inscrição não tenha sido confirmada."
    
    20.3. "O sistema deve permitir consultar a lista de participantes que já efetuaram check-in e os que ainda não o fizeram."


21. "Caso seja permitido pela administração, o operador deve conseguir registar o comprovativo da transferência bancária, de modo a validar a inscrição do participante."
    
    21.1. "O sistema deve permitir aos operadores, quando tiverem permissão, registar um comprovativo de pagamento e associá-lo à inscrição de um participante."

    21.2. "O sistema deve permitir ao operador indicar o valor pago, a data da transferência e submeter o comprovativo de transferência."

    
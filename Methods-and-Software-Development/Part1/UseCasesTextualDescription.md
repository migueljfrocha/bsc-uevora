# Descrição textual dos use cases


## 1. Criar Conta

**Atores:** Administrador/Operador

**Objetivo:** Criar uma nova conta de utilizador no sistema.

**Cenário principal de sucesso:**
1. O utilizador seleciona a opção para criar uma nova conta.
2. O sistema apresenta o menu de criar conta.
3. O utilizador escolhe o tipo de conta que pretende criar.
4. O utilizador preenche o formulário de inscrição com os seus dados.
5. O sistema valida os dados e confirma a inscrição na plataforma.
6. O sistema envia um e-mail de confirmação ao utilizador.

**Extensões:**
- 5a. Dados inválidos
   1. O sistema não valida a inscrição.
   2. O sistema informa o utilizador do erro, e retorna para o passo 2 do MSS.



## 2. Autenticação

**Atores:** Administrador/Operador

**Objetivo:** Autenticar um utilizador no sistema.

**Cenário principal de sucesso:**
1. O utilizador seleciona a opção de login.
2. O sistema apresenta o menu de autenticação.
3. O utilizador insere as suas credenciais (e-mail e palavra-passe).
4. O sistema valida as credenciais e autentica o utilizador.

**Extensões:**
- 4a. Credenciais inválidas
    1. O sistema não autentica o utilizador.
    2. O sistema informa o utilizador do erro e retorna ao passo 2 do MSS.



## 3. Criar Evento

**Atores:** Administrador

**Objetivo:** Criar um novo evento.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.

**Cenário principal de sucesso:**
1. O administrador seleciona a opção de criar evento.
2. O sistema apresenta o menu de criar evento.
3. O administrador preenche os dados necessários do evento (e.g. nome, local, datas do evento).
4. O administrador preenche as datas das várias fases de inscrição.
5. O administrador preenche o preço para cada tipo de inscrição, por cada fase de inscrição existente.
6. O administrador submete os dados do evento.
7. O sistema valida os dados e apresenta-lhe o menu de configurações adicionais.
8. O administrador preenche os dados das opções adicionais que pretende adicionar para o evento.
9. O administrador submete as opções adicionais.
10. O sistema valida os dados e apresenta-lhe uma mensagem de sucesso.

**Extensões:**
- 7a. Dados inválidos
    1. O sistema não valida a criação do evento.
    2. O sistema informa o administrador do(s) erro(s) que ocorreram, e retorna ao passo 2 do MSS.
- 8a. Opções Inválidas
    1. O sistema não valida as opções adicionais.
    2. O sistema informa o administrador do(s) erro(s) que ocorreram, e retorna ao passo 7 do MSS.
- 8b. Administrador não adiciona opções adicionais
    1. O administrador opta por não adicionar opções adicionais.
    2. O fluxo continua para o passo 10 do MSS.



## 4. Consultar Lista de Eventos

**Atores:** Administrador

**Objetivo:** Consultar a lista dos eventos de que é administrador.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.

**Cenário principal de sucesso:**
1. O administrador seleciona a opção de consultar lista de eventos.
2. O sistema apresenta a lista de todos os eventos criados pelo administrador.



## 5. Editar Evento

**Atores:** Administrador

**Objetivo:** Editar os dados de um evento existente.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.
- O administrador tem que administrar o evento que pretende editar.

**Cenário principal de sucesso:**
1. O administrador consulta a lista dos seus eventos.
2. O administrador seleciona o evento que pretende editar.
3. O sistema apresenta os dados atuais, bem como o menu do evento.
4. O administrador seleciona a opção de editar evento.
5. O sistema apresenta as várias opções de edição do evento.
6. O administrador faz a edição dos dados pretendidos e submete as alterações.
7. O sistema valida as alterações e apresenta uma mensagem de sucesso.

**Extensões:**
- 7a. Dados inválidos
    1. O sistema não valida as alterações.
    2. O sistema informa o administrador do(s) erro(s) que ocorreram, e retorna ao passo 5 do MSS.



## 6. Confirmar e Associar Pagamento

**Atores:** Administrador

**Objetivo:** Confirmar o pagamento de um participante de um certo evento, bem como associá-lo a uma inscrição.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.
- O administrador tem que administrar o evento que pretende confirmar pagamentos.

**Cenário principal de sucesso:**
1. O administrador acede ao menu do evento a que pretende confirmar.
2. O sistema apresenta o menu do evento.
3. O administrador consulta os pagamentos recebidos para o evento.
4. O administrador seleciona a opção para registar informações da transferência.
5. O sistema apresenta-lhe o menu de registo de uma transferência.
6. O administrador insere os dados da transferência manualmente na plataforma.
7. O sistema confirma a submissão e mostra a lista dos participantes.
8. O administrador procura o participante que pagou e associa-lhe o pagamento. 
9. O administrador submete as informações.
10. O sistema valida os dados, confirma a inscrição do participante, e apresenta uma mensagem de sucesso. 

**Extensões:**
- 7a. Dados inválidos
    1. O sistema não valida a submissão.
    2. O sistema informa o administrador do(s) erro(s) que ocorreram, e retorna ao passo 5 do MSS.

- 10a. Dados inválidos 
    1. O sistema não valida a submissão.
    2. O sistema informa o administrador do(s) erro(s) que ocorreram, e retorna ao passo 7 do MSS.

**Notas de implementação:**
- Validar o formato do comprovativo de transferência (e.g. .pdf, .jpg).



## 7. Consultar Lista de Participantes

**Atores:** Administrador

**Objetivo:** Consultar a lista de participantes inscritos num evento.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.
- O administrador tem que administrar o evento cuja lista de participantes pretende consultar.

**Cenário principal de sucesso:**
1. O administrador consulta a lista dos seus eventos.
2. O administrador seleciona o evento que pretende consultar.
3. O sistema apresenta os dados atuais, bem como  o menu do evento.
4. O administrador seleciona o menu da listagem de participantes.
5. O sistema apresenta a lista de participantes inscritos no evento.
6. O administrador pode aplicar filtros à lista de participantes (e.g. por tipo de inscrição, estado do pagamento).
7. O sistema apresenta a lista de participantes filtrada.



## 8. Exportar e Imprimir Lista de Participantes

**Atores:** Administrador

**Objetivo:** Exportar e imprimir a lista de participantes.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.
- O administrador tem que administrar o evento que pretende exportar e imprimir a lista de participantes.


**Cenário principal de sucesso:**
1. O administrador seleciona o menu do evento onde pretende fazer operações.
2. O sistema apresenta o menu desejado.
3. O administrador seleciona o menu da listagem de participantes.
4. O sistema apresenta o menu desejado.
5. O administrador seleciona a opção para exportar participantes.
6. O sistema apresenta o menu desejado.
7. O administrador escolhe o formato para exportar a lista de participantes e confirma.
8. O sistema apresenta-lhe a opção para imprimir, mostrando as suas impressoras.
9. O administrador confirma a impressora e pede para imprimir.
10. O sistema mostra uma mesagem de sucesso.

**Extensões:**
- 8a. Nenhuma impressora
    1. O sistema não encontra nenhuma impressora e não será impressa a lista.
    2. O fluxo continua para o passo 10 do MSS.

- 8b. Administrador recusa a impressão
    1. O administrador opta por não imprimir a lista de participantes.
    2. O fluxo continua para o passo 10 do MSS.

**Notas de implementação:**
- O formato da exportação será do formato .csv ou .pdf . 



## 9. Adicionar Operadores

**Atores:** Administrador

**Objetivo:** Adicionar um novo operador a um evento.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.
- O administrador tem que administrar o evento que pretende adicionar um operador.

**Cenário principal de sucesso:**
1. O administrador consulta a lista dos seus eventos.
2. O administrador seleciona o evento que pretende consultar.
3. O sistema apresenta os dados atuais, bem como o menu do evento.
4. O administrador seleciona o menu de gestão de operadores.
5. O sistema apresenta a lista de operadores do evento.
6. O administrador seleciona a opção para adicionar um novo operador.
7. O sistema apresenta o formulário para adicionar um novo operador.
8. O administrador preenche os dados do operador que pretende adicionar (e.g. e-mail, permissões) e submete o formulário.
9. O sistema valida as alterações, apresenta uma mensagem uma mensagem de sucesso e envia um e-mail ao possível operador a informá-lo do convite.

**Extensões:**
- 9a. Dados inválidos/Conta inexistente/Operador já existe
    1. O sistema não valida as alterações.
    2. O sistema informa o administrador do(s) erro(s) que ocorreram, e retorna ao passo 7 do MSS.

**Notas de implementação:**
- O email enviado ao possível operador poderá conter um URL único para para responder ao convite.



## 10. Editar Operadores

**Atores:** Administrador

**Objetivo:** Editar as permissões de um operador de um evento.

**Pré‑condições:**
- O administrador tem de estar autenticado no sistema.
- O administrador tem que administrar o evento que pretende remover um operador.

**Cenário principal de sucesso:**
1. O administrador consulta a lista dos seus eventos.
2. O administrador seleciona o evento que pretende consultar.
3. O sistema apresenta os dados atuais, bem como o menu do evento.
4. O administrador seleciona o menu de gestão de operadores.
5. O sistema apresenta a lista de operadores do evento.
6. O administrador seleciona o operador que pretende editar.
7. O sistema exibe as informações atuais do operador, incluindo as suas permissões.
8. O administrador edita as permissões do operador conforme pretende e submete as alterações.
9. O sistema valida as alterações, apresenta uma mensagem uma mensagem de sucesso e envia um e-mail ao operador a informá-lo das alterações feitas. 

**Extensões:**
- 9a. Dados inválidos
    1. O sistema não valida as alterações.
    2. O sistema informa o administrador do(s) erro(s) que ocorreram, e retorna ao passo 7 do MSS.



## 11. Responde a Convites Recebidos (de Operador)

**Atores:** Operador

**Objetivo:** Aceitar ou recusar convites para ser operador de um evento.

**Pré‑condições:**
- O operador tem de estar autenticado no sistema.

**Cenário principal de sucesso:**
1. O operador seleciona a opção da caixa de entrada.
2. O sistema apresenta todas os seus convites.
3. O operador realiza uma operação ao convite (aceitar ou recusar).
4. O sistema valida a operação e mostra uma mensagem de confirmação.

**Extensões:**
- 4a. Convite inválido
    1. O sistema não realiza a operação.
    2. O sistema informa o operador do(s) erro(s) que ocorreram, e retorna ao passo 2 do MSS.

**Notas de implementação:**
- O operador também poderá clicar num URL único (enviado por e-mail) para responder aos convites.



## 12. Procurar Participante

**Atores:** Operador 

**Objetivo:** Procurar um certo participante, de modo a consultar as suas informações ou fazer o check-in.

**Pré‑condições:**
- O operador tem de estar autenticado no sistema.
- O operador tem que fazer parte da lista de operadores do evento que pretende procurar participante.


**Cenário principal de sucesso:**
1. O operador seleciona o evento que pretende procurar o participante.
2. O sistema mostra o menu do evento, que contém os participantes.
3. O operador insere o nome ou email que pretende consultar e clica na opção de procurar. 
4. O sistema valida os dados e mostra os resultados da pesquisa.

**Extensões:**
- 4a. Dados inválidos
    1. O sistema não realiza a operação.
    2. O sistema informa o operador do(s) erro(s) que ocorreram, e retorna ao passo 2 do MSS.
    


## 13. Consultar Informações de Participante

**Atores:** Operador

**Objetivo:** Consultar informação de um determinado participante de um evento.

**Pré‑condições:**
- O operador tem de estar autenticado no sistema.
- O operador tem que fazer parte da lista de operadores do evento que pretende consultar participante.

**Cenário principal de sucesso:**
1. Após procurar o participante, o operador seleciona o participante que pretende consultar da lista apresentada.
2. O sistema apresenta as informações do participante selecionado.



## 14. Fazer Check-in de Participante

**Atores:** Operador

**Objetivo:** Fazer o check-in de um participante num evento.

**Pré‑condições:**
- O operador tem de estar autenticado no sistema.
- O operador tem que fazer parte da lista de operadores do evento que pretende fazer check-in do participante.

**Cenário principal de sucesso:**
1. Após consultar as informação do participante, o operador seleciona a opção de fazer check-in.
2. O sistema regista o check-in do participante e apresenta uma mensagem de sucesso.

**Extensões:**
- 2a. Participante não tem inscrição confirmada e a organização não aceita pagamento no local
    1. O sistema não realiza a operação.
    2. O sistema informa o operador do(s) erro(s) que ocorreram, e retorna ao passo 1 do MSS.
    
- 2b. Participante não tem inscrição confirmada e a organização aceita pagamento no local
    1. O sistema informa o operador com os dados para pagamento.
    2. Após fazer o pagamento, o participante envia a confirmação ao operador.
    3. O operador regista o pagamento no sistema .
    4. O sistema valida as informações, confirma o pagamento e retorna ao passo 1 do MSS.

- 2c. Participante já fez check-in
    1. O sistema não realiza a operação.
    2. O sistema informa o operador do(s) erro(s) que ocorreram, e retorna ao passo 1 do MSS.



## 15. Consultar Lista de Eventos

**Atores:** Participante

**Objetivo:** Consultar a lista de eventos disponíveis na plataforma.

**Cenário principal de sucesso:**
1. O participante seleciona a opção de consultar a lista de eventos.
2. O sistema apresenta a lista de todos os eventos disponíveis na plataforma.



## 16. Procurar Evento

**Atores:** Participante

**Objetivo:** Procurar um evento

**Cenário principal de sucesso:**
1. O participante insere a informação do evento que pretende consultar (e.g. data, local, nome do evento) e clica na opção de procurar. 
2. O sistema valida os dados e mostra os resultados da pesquisa.

**Extensões:**
- 2a. Dados inválidos
    1. O sistema não realiza a operação.
    2. O sistema informa o participante do(s) erro(s) que ocorreram, e retorna ao passo 2 do MSS.
    


## 17. Consultar Detalhes de um Evento

**Atores:** Participante

**Objetivo:** Consultar os detalhes de um evento.


**Cenário principal de sucesso:**
1. O participante seleciona o evento que pretende consultar da lista de eventos apresentada (ou dos resultados da pesquisa).
2. O sistema apresenta os detalhes do evento selecionado.



## 18. Inscrever num Evento

**Atores:** Participante

**Objetivo:** Inscrever num evento com inscrições ativas.

**Pré‑condições:**
- O evento não está cheio.

**Cenário principal de sucesso:**
1. O participante consulta a lista de eventos, e seleciona o evento em que prentende insrever-se.
2. O sistema apresenta um formulário de inscrição que contém dados pessoais.
3. O participante preenche o formulário com os seus dados e submete o formulário.
4. O sistema valida os dados.
5. O sistema mostra as opções adicionais do evento com inscrições abertas.
6. O participante seleciona as opções adicionais que pretende frequentar.
7. O sistema confirma as opções adicionais do participante.
8. O sistema calcula automaticamente o preço final da inscrição e mostra ao participante.
9. O participante aceita o preço e confirma a inscrição.
10. O sistema mostra os dados para pagamento, o id da sua inscrição e coloca a inscrição como pendente.
11. O sistema envia também um email com os dados para pagamento, caso pretenda pagar mais tarde.

**Extensões:**
- 4a. Dados inválidos
    1. O sistema não realiza a operação.
    2. O sistema informa o participante do(s) erro(s) que ocorreram, e retorna ao passo 2 do MSS.

- 9.a Participante não aceita o preço
    1. O participante opta por não aceitar o preço final da inscrição.
    2. O sistema volta para o passo 5 do MSS.

**Notas de implementação:**
- O email enviado ao participante poderá conter o id único da inscrição, um URL para consultar o estado da inscrição e os dados para efetuar o pagamento.



## 19. Verificar Estado da Inscrição

**Atores:** Participante

**Objetivo:** Verificar o estado da sua inscrição num evento.

**Pré‑condições:**
- O participante tem de estar inscrito no evento.

**Cenário principal de sucesso:**
1. O participante seleciona o menu para verificar o estado de inscrição.
2. O sistema mostra o menu selecionado.
3. O participante indica o id da inscrição ao qual pretende consultar.
4. O sistema valida o id.
5. O sistema mostra o estado da sua inscrição.

**Extensões:**
- 4.a. Id inválido
    1. O sistema não realiza a operação.
    2. O sistema informa o participante do(s) erro(s) que ocorreram, e retorna ao passo 2 do MSS.

**Notas de implementação:**
- O id da inscrição será apresentado ao efetuar a inscrição, bem como enviado no e-mail de confirmação da inscrição no evento.
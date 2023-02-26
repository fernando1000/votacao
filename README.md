## Cadastrar uma nova pauta
Enviar uma requisição do tipo POST para o endpoint "localhost:8080/api-votacao/v1/pautas"<br />
Exemplo de requisição:<br />
{<br />
    "numeroPauta": 2,<br />
    "assuntoPauta": "Querem um CDB que rende 16% ao ano?"<br />
}<br />
Exemplo de resposta:<br />
{<br />
    "id": "465b7eee-5ef7-42f0-8e53-b3084b32bb7a"<br />
}<br />

## Abrir uma sessão de votação
Enviar uma requisição do tipo POST para o endpoint "localhost:8080/api-votacao/v1/sessao-votacao"<br />
Exemplo de requisição:<br />
{<br />
    "numeroPauta": 2,<br />
    "dataLimite": "2023-01-05 18:01:00"<br />
}<br />
Exemplo de resposta:<br />
{<br />
    "id": "9055a9e2-6c96-4ad2-a885-d04995ccb5ca",<br />
    "numeroPauta": 2,<br />
    "dataLimite": "2023-01-05T18:01:00"<br />
}<br />

## Realizar votação
Enviar uma requisição do tipo POST para o endpoint "localhost:8080/api-votacao/v1/votacao"<br />
Exemplo de requisição:<br />
{<br />
    "numeroPauta": 2,<br />
    "cpfAssociado": 36989526814,<br />
    "concordaComPauta": "SIM"<br />
}<br />
Exemplo de resposta:<br />
{<br />
    "id": "0566557c-baee-4ed6-b3a5-1d9087ea7ceb",<br />
    "numeroPauta": 2,<br />
    "cpfAssociado": 36989526814,<br />
    "concordaComPauta": "SIM"<br />
}<br />

## Obter resultado da votação
Enviar uma requisição do tipo GET para o endpoint "localhost:8080/api-votacao/v1/votacao/2"<br />
Exemplo de resposta:<br />
{<br />
    "numeroPauta": 2,<br />
    "assuntoPauta": "Querem um CDB que rende 16% ao ano?",<br />
    "votosPositivos": 3,<br />
    "votosNegativos": 2<br />
}<br />


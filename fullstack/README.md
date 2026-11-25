# FinControl — Java, Oracle FIAP e React

Projeto acadêmico de gestão com CRUD completo de **Usuários, Contas e Investimentos**, login, dashboard e página 404. Evolui as entidades do trabalho GRUPO_77 e a identidade visual do dashboard FinControl.

**Situação da entrega:** código implementado; a conexão real ao Oracle FIAP e o teste de ponta a ponta com esse banco ainda devem ser executados na máquina do aluno. Consulte `VALIDACAO.md` para distinguir verificações executadas e pendentes. Não entregue na plataforma antes de concluir essa validação.

## 1. Tecnologias e organização

- Java 17 ou 21, Maven, Spring Boot 3.5.7.
- Spring Web, Spring Data JPA, Validation, Oracle JDBC e Spring Boot Test: dependências presentes no material do estudo de caso.
- React 19, TypeScript, Vite 7, React Router; hooks, props, Context API e `fetch`.
- CSS próprio, sem biblioteca de componentes, gráficos, autenticação ou estado adicional.
- Node.js 22.12 ou superior compatível com Vite 7; npm.

A configuração de compilação usa Java 17, preservando o projeto anterior; funciona também com o JDK 21 utilizado na apostila. O backend não utiliza banco em memória, H2 ou armazenamento alternativo.

Pastas:

- `backend/`: aplicação Maven; `model`, `repository`, `service`, `controller`, `dto` e `config`.
- `frontend/`: aplicação React com componentes, contexto de autenticação, páginas e serviço HTTP.
- `database/`: scripts Oracle de tabelas/sequências e usuário de teste.
- `testes/`: teste de integração real via HTTP, utilizando apenas a biblioteca padrão do Python.

## 2. Preparar o Oracle da FIAP

Abra o Oracle SQL Developer e conecte ao **seu usuário da instância FIAP**:

- Host: `oracle.fiap.com.br`
- Porta: `1521`
- SID: `ORCL`
- Usuário e senha: fornecidos pela FIAP.

Confirme esses dados no portal da instituição se houver mudança de infraestrutura. Execute como script (F5), nesta ordem:

1. `database/01_schema.sql`
2. `database/02_usuario_teste.sql`

O primeiro script é de instalação única. DDL Oracle faz commits implícitos: não tente executar repetidamente nem apagar tabelas sem conferir os dados. O segundo usa MERGE e não substitui um usuário de teste já existente.

As tabelas são `FC7_USUARIO`, `FC7_CONTA` e `FC7_INVESTIMENTO`, com sequências próprias. As tabelas antigas `T_FTC_*` **não são alteradas e seus registros não são migrados automaticamente**. A nova modelagem mantém as relações usuário → contas → investimentos, substitui as chaves compostas antigas por IDs numéricos com sequência, conforme o estudo de caso, e usa `NUMBER(15,2)`/`BigDecimal` para valores monetários. O titular do investimento é obtido pela conta, sem duplicação de `id_usuario`.

O backend utiliza `ddl-auto=validate`: exige que o script seja executado e não cria nem apaga tabelas automaticamente. A senha do aplicativo é armazenada em hash PBKDF2 com salt, separado da senha Oracle.

## 3. Iniciar o backend

Abra `backend/pom.xml` como projeto Maven no IntelliJ. Configure o JDK e as variáveis de ambiente da configuração de execução, ou execute pelo terminal.

### Windows / PowerShell

Na pasta `backend`:

```powershell
$env:ORACLE_USER = "SEU_RM"
$credencial = Read-Host "Senha Oracle FIAP" -AsSecureString
$env:ORACLE_PASSWORD = [System.Net.NetworkCredential]::new("", $credencial).Password
mvn spring-boot:run
```

### Linux / macOS / Bash

```bash
cd backend
export ORACLE_USER='SEU_RM'
read -rsp 'Senha Oracle FIAP: ' ORACLE_PASSWORD
export ORACLE_PASSWORD
mvn spring-boot:run
```

`ORACLE_URL` é opcional: o padrão já aponta para a FIAP. O `.env.example` apenas documenta os nomes; Spring Boot não carrega esse arquivo automaticamente. Nunca coloque credenciais Oracle no React ou no ZIP.

A API inicia em `http://localhost:8080/api`. Acessar recursos sem sessão retorna **401**, comportamento esperado. O servidor depende da conexão Oracle e das tabelas válidas para inicializar.

Para testes unitários e geração do JAR:

```bash
mvn clean test
mvn package
java -jar target/fincontrol-1.0.0.jar
```

Os testes unitários usam mocks dos repositórios e não exigem credenciais Oracle. Eles não substituem o teste de integração real.

## 4. Iniciar o frontend

Em outro terminal, na pasta `frontend`:

```bash
npm install
npm run dev
```

Abra **http://localhost:5173**. Mantenha backend e frontend em execução. O Vite encaminha `/api` ao backend na porta 8080, evitando CORS durante o desenvolvimento.

A porta 5173 está fixa para manter a configuração de origem e sessão. Se estiver ocupada, encerre a outra aplicação. Acesse por `localhost`, não por endereço de rede. Se alterar a origem, ajuste `FRONTEND_ORIGIN` no backend.

Para verificar a compilação:

```bash
npm run build
npm run preview
```

`preview` também utiliza porta 5173 e proxy local. Não execute `dev` e `preview` ao mesmo tempo. O projeto é de execução local; não inclui configuração de publicação em produção.

## 5. Login de teste

Após executar os scripts SQL:

- **E-mail:** `teste@fincontrol.com`
- **Senha:** `FinControl@123`

São credenciais fictícias do aplicativo, não credenciais Oracle. O primeiro acesso mostra zero contas e investimentos: os números do painel são calculados com os registros reais cadastrados. Crie contas e investimentos para popular o dashboard.

O escopo é um **painel acadêmico de gestão**: os usuários autenticados têm acesso aos três cadastros, incluindo os registros de outros usuários. Não é um aplicativo bancário multiusuário com isolamento por titular ou perfis de autorização. Não utilize dados pessoais ou financeiros reais na demonstração.

A sessão usa cookie HttpOnly e SameSite Strict, com expiração por inatividade de 30 minutos. O login é verificado no backend; esconder a página no React não é a única proteção. O interceptor exige sessão nas rotas privadas e um cabeçalho próprio nas operações de escrita. Não há Spring Security, JWT, OAuth, recuperação de senha ou autenticação multifator neste escopo.

## 6. Funcionalidades e regras

### Usuários

CRUD de nome, e-mail, CPF e telefone. Senha obrigatória no cadastro (8 a 128 caracteres), opcional na edição. E-mail e CPF únicos. CPF validado quanto ao formato de 11 dígitos, não quanto aos dígitos verificadores. O hash nunca integra a resposta JSON. Não é permitido excluir o usuário da sessão atual ou um usuário com contas vinculadas.

### Contas

CRUD de titular, tipo, saldo informado, status e data de abertura. Saldo não negativo, até duas casas decimais. Uma conta com investimentos não pode ser excluída nem mudar de titular. Uma conta com investimentos ativos não pode ser inativada. A abertura não pode ser posterior às aplicações vinculadas.

### Investimentos

CRUD de conta, nome, tipo, valor aplicado, rentabilidade de referência anual, data, prazo em meses e status. Valor aplicado positivo; prazo de 0 a 1200 meses; datas não futuras; aplicação não pode anteceder a abertura da conta. Aplicações ativas exigem conta ativa.

Os registros são um inventário financeiro: cadastrar, editar, resgatar (alterar status) ou excluir um investimento **não movimenta o saldo da conta**. A rentabilidade é informativa, sem capitalização automática. O painel não soma saldo e aplicações como patrimônio, evitando dupla contagem.

Receitas, despesas, Pix, cartões, integração bancária e limites por categoria dos protótipos iniciais ficam fora deste recorte. O requisito de três entidades é atendido por Usuário, Conta e Investimento.

## 7. API REST

Para cada recurso `usuarios`, `contas` e `investimentos`:

| Método | Rota | Sucesso |
|---|---|---|
| GET | `/api/{recurso}` | 200, lista JSON |
| GET | `/api/{recurso}/{id}` | 200, registro JSON |
| POST | `/api/{recurso}` | 201, registro e cabeçalho Location |
| PUT | `/api/{recurso}/{id}` | 200, registro atualizado |
| DELETE | `/api/{recurso}/{id}` | 204, corpo vazio |

Erros: 400 para dados inválidos, 401 sem sessão/credenciais incorretas, 403 origem/cabeçalho não permitido, 404 registro inexistente, 409 duplicação ou conflito de vínculo. Na atualização, o ID utilizado é o da URL.

Autenticação:

- `POST /api/auth/login` — JSON com `email` e `senha`.
- `GET /api/auth/me` — usuário autenticado.
- `POST /api/auth/logout` — encerra sessão, retorna 204.

No Insomnia, habilite o cookie jar e inclua `X-FinControl-Request: FinControl` em POST/PUT/DELETE, além de `Content-Type: application/json`. Faça o login antes das demais requisições.

Exemplos de corpos para POST e PUT (troque os IDs pelos retornados pela API):

```json
{"nome":"Pessoa de teste","email":"pessoa@example.com","cpf":"12345678901","telefone":"12999990000","senha":"Teste@123"}
```

```json
{"usuarioId":1,"saldo":1000.00,"tipoConta":"CORRENTE","statusConta":"ATIVA","dataAbertura":"2026-01-01"}
```

```json
{"contaId":1,"nomeInvestimento":"Reserva","tipoInvestimento":"CDB","valorAplicado":500.00,"rentabilidade":10.50,"dataAplicacao":"2026-01-02","prazo":12,"statusInvestimento":"ATIVO"}
```

## 8. Teste final obrigatório com Oracle FIAP

1. Execute os scripts no banco e inicie o backend sem erros.
2. Execute `npm install` e `npm run build` no frontend.
3. Faça login com o usuário de teste.
4. Crie um segundo usuário, uma conta desse usuário e um investimento dessa conta.
5. Edite os três e consulte os registros. Recarregue o navegador para confirmar persistência.
6. Confira no SQL Developer: `SELECT * FROM FC7_CONTA;` e `SELECT * FROM FC7_INVESTIMENTO;`.
7. Confira saldo e aplicações no dashboard.
8. Teste campos inválidos, e-mail duplicado e exclusão de registros com vínculos.
9. Exclua investimento → conta → segundo usuário. Confirme as remoções no Oracle.
10. Acesse uma URL inexistente; confira a página 404. Saia e confirme o bloqueio de páginas privadas.
11. Reinicie backend e frontend e confirme que o login e os dados persistentes continuam funcionando.

Opcionalmente, com o backend já conectado ao Oracle e Python 3 instalado, execute a partir da raiz:

```bash
python testes/teste_integracao.py
```

O teste solicita a senha, cria dados fictícios e tenta limpar somente os IDs criados por ele. Se houver interrupção, confira a mensagem de limpeza pendente. Não modifica registros antigos. Esse script valida a API real; faça também os passos visuais no navegador.

## 9. Montar o ZIP

O ZIP desta entrega contém fontes, testes, scripts SQL, README, `package.json` e `package-lock.json`. Não contém `node_modules`, `target`, `dist`, senhas Oracle ou PDFs das aulas. As dependências são recriadas por Maven e npm.

Se alterar o projeto, gere novamente o ZIP sem incluir essas pastas. No PowerShell, uma opção é copiar a pasta para uma área de entrega, retirar `frontend/node_modules`, `frontend/dist` e `backend/target` **dessa cópia**, conferir os arquivos e compactá-la. Preserve os dois arquivos de dependências do frontend.

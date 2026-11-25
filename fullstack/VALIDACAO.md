# Registro de validação

## Verificações executadas neste ambiente

- `npm install`: concluído, package-lock.json gerado.
- `npm run build`: TypeScript e build Vite concluídos sem erros.
- Maven `package`: compilação, oito testes e geração de JAR concluídos.
- Resultado JUnit: **8 testes, 0 falhas, 0 erros, 0 ignorados**.
- Regras verificadas: senha PBKDF2/salt; bloqueio de exclusão de usuário com conta; bloqueio de autoexclusão; e-mail duplicado; aplicação ativa em conta inativa; bloqueio de inativação de conta com investimento ativo; respostas HTTP 201/200/204/404/400; login correto/incorreto e ausência de hash nas respostas.
- Interface exercitada em Chromium automatizado, com **API simulada**: login incorreto e correto; criação, consulta, edição e exclusão nas três entidades; dashboard; página 404; logout.
- Inspeção visual de login, dashboard desktop e dashboard mobile de 390 px. Sem transbordamento horizontal da página no cenário testado.
- Sintaxe do script Python de integração verificada.
- ZIP inspecionado: não contém node_modules, dist, target, credenciais Oracle ou material didático original; inclui package.json e package-lock.json.

## Pendências obrigatórias antes da entrega acadêmica

**A instância Oracle FIAP não foi acessada.** A tentativa de conexão à porta 1521 falhou na resolução DNS do host neste ambiente. Não houve criação de tabelas, execução dos scripts SQL, validação JPA contra o Oracle, nem demonstração de persistência real.

Os testes do backend usam mocks; o teste da interface também usa respostas simuladas. Nenhuma dessas verificações substitui a integração real React → Spring Boot → Oracle FIAP.

Execute o passo a passo da seção 8 do README na máquina do aluno. Confirme a criação das tabelas, a inicialização do backend com `ddl-auto=validate`, todos os CRUDs na interface, os registros no SQL Developer e a persistência após reinício. O arquivo `testes/teste_integracao.py` ajuda a verificar a API real depois que o backend estiver conectado.

## Limites do recorte

- Painel de gestão acadêmico, sem isolamento de registros por titular: usuários autenticados podem gerir todos os cadastros.
- Saldo manual, investimentos informativos; não há movimentação bancária nem cálculo automático de rendimentos.
- São implementadas três entidades (Usuário, Conta, Investimento). As demais funcionalidades dos protótipos antigos não integram esta versão.
- Java 17 na compilação, compatível com o Java 21 da apostila. Dependências limitadas à pilha ensinada; ferramentas de inspeção do ambiente não foram adicionadas ao projeto.

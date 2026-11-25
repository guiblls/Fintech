# FinControl

Painel acadêmico de usuários, contas e investimentos desenvolvido na FIAP.

**[Experimentar o FinControl](https://guiblls.github.io/Fintech/fincontrol/)** · [Código e execução full stack](fullstack/README.md) · [Validações](fullstack/VALIDACAO.md)

Entre como visitante, cadastre um titular fictício, abra uma conta e registre uma aplicação. Use as listas para pesquisar, editar e excluir os registros. O painel totaliza os saldos informados e os investimentos ativos.

## Como executar

A demonstração pública usa as telas React da entrega e armazena dados de exemplo na sessão da aba. Em `fullstack/frontend`, execute `npm ci` e `npm run dev -- --mode portfolio` para experimentar essa versão localmente. A compilação pública usa `npm run build -- --mode portfolio --base ./`. Seus arquivos compilados ficam publicados em `fincontrol/`.

Para executar o sistema com a API Spring Boot, a autenticação e o Oracle, siga o [guia da entrega](fullstack/README.md). A validação de ponta a ponta com a instância Oracle da FIAP está pendente.

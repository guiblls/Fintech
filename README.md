# FinControl

Painel de finanças pessoais desenvolvido para a disciplina de Front-End do curso de Análise e Desenvolvimento de Sistemas da FIAP. A interface reúne saldo, receitas, despesas e contas a pagar para facilitar a leitura da situação financeira do mês.

[Visualizar o painel no GitHub Pages](https://guiblls.github.io/Fintech/)

## Aplicação full stack · Grand Finale

[Abrir o código e as instruções de execução](fullstack/README.md) · [Consultar o registro de validação](fullstack/VALIDACAO.md)

A entrega full stack reúne React e TypeScript na interface, uma API Java com Spring Boot e persistência prevista em Oracle via Spring Data JPA. O domínio relaciona usuários, contas e investimentos. Ao cadastrar uma conta, o formulário envia os dados à API, o serviço consulta o titular e o repositório salva a conta no banco configurado.

O link do GitHub Pages acima apresenta o painel estático original. Para experimentar o login e os cadastros da aplicação full stack, siga as instruções de execução local. A validação de ponta a ponta com o Oracle da FIAP está pendente.

## O que explorar no painel estático

- Os cartões superiores exibem o resumo financeiro do mês.
- A lista de transações distingue entradas e saídas e mostra o valor de cada movimentação.
- As seções de contas e vencimentos organizam os compromissos financeiros.
- Os gráficos ilustram a evolução mensal e a distribuição dos gastos por categoria.

## Como funciona esta versão

Os dados de exemplo estão escritos em `index.html`. O painel usa HTML, CSS e utilitários do Tailwind CSS carregados pela internet. A busca, os menus e os filtros fazem parte da composição visual. Esta versão apresenta dados fixos e não grava movimentações.

Em `style.css`, a classe `.cards` organiza os cartões com CSS Grid. As regras `@media` alteram a quantidade de colunas conforme a largura da tela. O gráfico de categorias usa `conic-gradient` para desenhar as fatias.

## Executar no computador

1. No GitHub, selecione **Code → Download ZIP** e extraia a pasta.
2. Abra `index.html` no navegador. Mantenha a conexão com a internet para carregar o Tailwind CSS.
3. Para experimentar o código, abra a pasta no VS Code, altere um valor de exemplo em `index.html`, salve e atualize o navegador.

## Prévia

<img width="1912" height="917" alt="image" src="https://github.com/user-attachments/assets/9e1247e5-25db-4489-a71d-c7662c053ee0" />

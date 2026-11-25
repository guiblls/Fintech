export interface Usuario { id: number; nome: string; email: string; cpf: string; telefone: string | null; dataCadastro: string }
export interface Conta { id: number; saldo: number; tipoConta: string; statusConta: string; dataAbertura: string; usuario: Usuario }
export interface Investimento { id: number; conta: Conta; nomeInvestimento: string; tipoInvestimento: string; valorAplicado: number; rentabilidade: number; dataAplicacao: string; prazo: number; statusInvestimento: string }
export type Entidade = Usuario | Conta | Investimento;
export type Recurso = 'usuarios' | 'contas' | 'investimentos';
export const moeda = (n: number) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(n);
export const dataBR = (s: string) => s?.split('-').reverse().join('/') || '—';
export const hoje = () => { const d=new Date(); return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`; };

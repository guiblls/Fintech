import type { Conta, Investimento, Usuario, Recurso } from '../types';
import { hoje } from '../types';

type AccountRow = Omit<Conta,'usuario'> & {usuarioId:number};
type InvestmentRow = Omit<Investimento,'conta'> & {contaId:number};
interface State { usuarios:Usuario[]; contas:AccountRow[]; investimentos:InvestmentRow[]; signedIn:boolean }
const storageKey = 'fincontrol-portfolio.v1';
function initial():State {
  return {
    signedIn:false,
    usuarios:[{id:1,nome:'Alex Demo',cpf:'00000000000',telefone:null,email:'alex@example.com',dataCadastro:'2025-01-01'}],
    contas:[{id:1,usuarioId:1,saldo:4800,tipoConta:'CORRENTE',statusConta:'ATIVA',dataAbertura:'2025-01-01'}],
    investimentos:[{id:1,contaId:1,nomeInvestimento:'Reserva de exemplo',tipoInvestimento:'CDB',valorAplicado:1200,rentabilidade:8,prazo:12,statusInvestimento:'ATIVO',dataAplicacao:'2025-02-01'}]
  };
}
function read():State {
  const saved=sessionStorage.getItem(storageKey);
  return saved ? JSON.parse(saved) : initial();
}
function save(state:State) { sessionStorage.setItem(storageKey,JSON.stringify(state)); }
function requireRule(condition:unknown,message:string):asserts condition {
  if (!condition) throw new Error(message);
}
export function demoRequest(path:string, options:RequestInit) {
  const state=read();
  const visitor=state.usuarios.find(user=>user.id===1)!;
  if(path==='/auth/login') {state.signedIn=true;save(state);return visitor;}
  if(path==='/auth/logout') {state.signedIn=false;save(state);return;}
  if(!state.signedIn) throw new Error('Entre na demonstração.');
  if(path==='/auth/me') return visitor;
  const [,resource,rawId]=path.split('/');
  const key=resource as Recurso;
  const id=Number(rawId);
  const method=options.method||'GET';
  const rows=state[key];
  requireRule(Array.isArray(rows),'Recurso desconhecido.');
  const accounts=state.contas.map(c=>({...c,usuario:state.usuarios.find(u=>u.id===c.usuarioId)!}));
  const view={usuarios:state.usuarios,contas:accounts,investimentos:state.investimentos.map(i=>({...i,conta:accounts.find(c=>c.id===i.contaId)!}))};
  if(rawId) requireRule(rows.some(row=>row.id===id),'Registro não encontrado.');
  if(method==='GET') return rawId ? view[key].find(row=>row.id===id) : view[key];
  if(method==='DELETE') {
    if(key==='usuarios') {
      requireRule(id!==1,'O visitante em uso não pode ser excluído.');
      requireRule(!state.contas.some(c=>c.usuarioId===id),'Exclua primeiro as contas deste usuário.');
    }
    if(key==='contas') requireRule(!state.investimentos.some(i=>i.contaId===id),'Exclua primeiro os investimentos desta conta.');
    rows.splice(rows.findIndex(row=>row.id===id),1);
  } else {
    const body=JSON.parse(String(options.body));
    delete body.senha;
    const rowId=rawId?id:Math.max(0,...rows.map(row=>row.id))+1;
    if(key==='usuarios') {
      requireRule(!state.usuarios.some(u=>u.id!==id&&(u.email.toLowerCase()===body.email.trim().toLowerCase()||u.cpf===body.cpf)),'E-mail ou CPF já cadastrado.');
      body.dataCadastro=state.usuarios.find(u=>u.id===id)?.dataCadastro||hoje();
    }
    if(key==='contas') {
      requireRule(state.usuarios.some(u=>u.id===body.usuarioId),'Selecione um titular existente.');
      const linked=state.investimentos.filter(i=>i.contaId===id);
      requireRule(!linked.length||state.contas.find(c=>c.id===id)?.usuarioId===body.usuarioId,'Conta com investimentos não pode mudar de titular.');
      requireRule(body.statusConta==='ATIVA'||!linked.some(i=>i.statusInvestimento==='ATIVO'),'Resgate os investimentos ativos antes de inativar a conta.');
      requireRule(!linked.some(i=>i.dataAplicacao<body.dataAbertura),'A abertura precisa anteceder as aplicações.');
    }
    if(key==='investimentos') {
      const account=state.contas.find(c=>c.id===body.contaId);
      requireRule(account,'Selecione uma conta existente.');
      requireRule(account.statusConta==='ATIVA'||body.statusInvestimento!=='ATIVO','Investimento ativo exige uma conta ativa.');
      requireRule(body.dataAplicacao>=account.dataAbertura,'Aplicação anterior à abertura da conta.');
    }
    const row={...body,id:rowId};
    if(rawId) rows.splice(rows.findIndex(r=>r.id===id),1,row);
    else rows.push(row);
  }
  save(state);
}

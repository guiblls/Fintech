import { useEffect,useState } from 'react';
import type { FormEvent,ReactNode } from 'react';
import { Link,useNavigate,useParams } from 'react-router-dom';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import type { Recurso,Entidade,Usuario,Conta,Investimento } from '../types';
import { hoje } from '../types';
type Valores=Record<string,string>;
const nomes={usuarios:'usuário',contas:'conta',investimentos:'investimento'};
function iniciais(recurso:Recurso):Valores {
 if(recurso==='usuarios')return {nome:'',email:'',senha:'',cpf:'',telefone:''};
 if(recurso==='contas')return {usuarioId:'',saldo:'0.00',tipoConta:'CORRENTE',statusConta:'ATIVA',dataAbertura:hoje()};
 return {contaId:'',nomeInvestimento:'',tipoInvestimento:'CDB',valorAplicado:'',rentabilidade:'0',dataAplicacao:hoje(),prazo:'12',statusInvestimento:'ATIVO'};
}
export function EntityForm({recurso}:{recurso:Recurso}) {
 const {id}=useParams();const navigate=useNavigate();const {usuario,atualizar}=useAuth();
 const [v,setV]=useState<Valores>(iniciais(recurso));const [usuarios,setUsuarios]=useState<Usuario[]>([]);const [contas,setContas]=useState<Conta[]>([]);const [erro,setErro]=useState('');const [loading,setLoading]=useState(true);const [falhaCarga,setFalhaCarga]=useState(false);const [busy,setBusy]=useState(false);
 useEffect(()=>{let active=true;setLoading(true);setErro('');setFalhaCarga(false);
 async function carregar() {
  try {
   const [item,us,cs]=await Promise.all([id?api<Entidade>(`/${recurso}/${id}`):Promise.resolve(null),recurso==='contas'?api<Usuario[]>('/usuarios'):Promise.resolve([]),recurso==='investimentos'?api<Conta[]>('/contas'):Promise.resolve([])]);
   if(!active)return;setUsuarios(us);setContas(cs);
   if(item){let raw:Record<string,unknown>={...item};if(recurso==='contas')raw={...raw,usuarioId:(item as Conta).usuario.id};if(recurso==='investimentos')raw={...raw,contaId:(item as Investimento).conta.id};const val=iniciais(recurso);Object.keys(val).forEach(k=>{if(k!=='senha')val[k]=raw[k]==null?'':String(raw[k]);});setV(val);}else setV(iniciais(recurso));
  }catch(e){if(active){setErro((e as Error).message);setFalhaCarga(true);}}finally{if(active)setLoading(false);}
 }void carregar();return()=>{active=false;};},[id,recurso]);
 const change=(k:string,value:string)=>setV(prev=>({...prev,[k]:value}));
 function input(k:string,label:string,type='text',extra:Record<string,unknown>={}) {return <label>{label}<input name={k} type={type} value={v[k]??''} onChange={e=>change(k,e.target.value)} required {...extra}/></label>;}
 function select(k:string,label:string,options:{value:string;label:string}[]) {return <label>{label}<select name={k} required value={v[k]??''} onChange={e=>change(k,e.target.value)}><option value="" disabled>Selecione…</option>{options.map(o=><option value={o.value} key={o.value}>{o.label}</option>)}</select></label>;}
 const opts=(values:string[])=>values.map(value=>({value,label:value.replaceAll('_',' ')}));
 async function submit(e:FormEvent) {
  e.preventDefault();setBusy(true);setErro('');
  const body:Record<string,unknown>={...v};for(const k of ['usuarioId','contaId','saldo','valorAplicado','rentabilidade','prazo'])if(k in body)body[k]=Number(body[k]);
  if(recurso==='usuarios'&&id&&!v.senha)delete body.senha;
  try{await api(`/${recurso}${id?'/'+id:''}`,{method:id?'PUT':'POST',body:JSON.stringify(body)});if(recurso==='usuarios'&&Number(id)===usuario?.id)await atualizar();navigate(`/${recurso}`);}catch(e){setErro((e as Error).message);}finally{setBusy(false);}
 }
 let fields:ReactNode;
 if(recurso==='usuarios')fields=<>{input('nome','Nome completo','text',{maxLength:120})}{input('email','E-mail','email',{maxLength:180})}{input('cpf','CPF (11 dígitos)','text',{pattern:'[0-9]{11}',maxLength:11,inputMode:'numeric'})}{input('telefone','Telefone','tel',{required:false,maxLength:20})}{input('senha',id?'Nova senha (deixe vazio para manter)':'Senha','password',{required:!id,minLength:8,maxLength:128,autoComplete:'new-password'})}</>;
 else if(recurso==='contas')fields=<>{select('usuarioId','Titular da conta',usuarios.map(u=>({value:String(u.id),label:u.nome+' · '+u.email})))}{select('tipoConta','Tipo de conta',opts(['CORRENTE','POUPANCA','DINHEIRO']))}{input('saldo','Saldo informado (R$)','number',{min:0,max:'9999999999999.99',step:'0.01'})}{input('dataAbertura','Data de abertura','date',{max:hoje()})}{select('statusConta','Status',opts(['ATIVA','INATIVA']))}</>;
 else fields=<>{input('nomeInvestimento','Nome do investimento','text',{maxLength:120})}{select('contaId','Conta vinculada',contas.map(c=>({value:String(c.id),label:`#${c.id} · ${c.usuario.nome} · ${c.tipoConta} (${c.statusConta})`})))}{select('tipoInvestimento','Tipo',opts(['CDB','TESOURO DIRETO','POUPANCA','FUNDO','ACOES','OUTROS']))}{input('valorAplicado','Valor aplicado (R$)','number',{min:'0.01',max:'9999999999999.99',step:'0.01'})}{input('rentabilidade','Rentabilidade de referência (% ao ano)','number',{min:-100,max:'99999.99',step:'0.01'})}{input('prazo','Prazo (meses)','number',{min:0,max:1200,step:1})}{input('dataAplicacao','Data de aplicação','date',{max:hoje()})}{select('statusInvestimento','Status',opts(['ATIVO','RESGATADO']))}</>;
 const semVinculo=(recurso==='contas'&&!usuarios.length)||(recurso==='investimentos'&&!contas.length);
 return <><Link className="back-link" to={`/${recurso}`}>← Voltar para {recurso}</Link><div className="page-heading"><div><span className="eyebrow">CADASTRO</span><h1>{id?'Editar':'Cadastrar'} {nomes[recurso]}</h1><p>{id?`Atualize as informações do registro #${id}.`:'Preencha os campos para adicionar um novo registro.'}</p></div></div>{erro&&<div role="alert" className="error">{erro}</div>}{loading?<p role="status">Carregando formulário…</p>:falhaCarga?<Link to={`/${recurso}`}>Voltar à lista</Link>:<form className="panel form-panel" onSubmit={submit}>{semVinculo&&<p className="error">Cadastre primeiro {recurso==='contas'?<Link to="/usuarios/novo">um usuário</Link>:<Link to="/contas/novo">uma conta</Link>}.</p>}<fieldset disabled={busy}><div className="form-grid">{fields}</div></fieldset>{recurso==='investimentos'&&<p className="form-note">A rentabilidade é uma referência informada, sem cálculo automático de rendimentos. O cadastro não altera o saldo da conta.</p>}<div className="form-actions"><Link className="button secondary" to={`/${recurso}`}>Cancelar</Link><button className="button" disabled={busy||semVinculo}>{busy?'Salvando…':'Salvar cadastro'}</button></div></form>}</>;
}

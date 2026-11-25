import { useState } from 'react';
import type { FormEvent } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
export function Login() {
 const {usuario,entrar,erro:erroSessao}=useAuth();const [email,setEmail]=useState('');const [senha,setSenha]=useState('');const [erro,setErro]=useState('');const [busy,setBusy]=useState(false);
 if(usuario)return <Navigate to="/" replace/>;
 async function submit(e:FormEvent) {e.preventDefault();setBusy(true);setErro('');try{await entrar(email,senha);}catch(e){setErro((e as Error).message);}finally{setBusy(false);}}
 return <div className="login-page"><section className="login-story"><div className="brand">f<span>FinControl</span></div><div><span className="eyebrow">SUA VIDA FINANCEIRA, MAIS CLARA</span><h1>Seus números.<br/>Suas próximas<br/><em>conquistas.</em></h1><p>Um espaço para organizar contas, acompanhar investimentos e enxergar novas possibilidades.</p><div className="decor-bars" aria-hidden="true"><i/><i/><i/><i/><i/><i/></div></div><small>Projeto acadêmico · FIAP</small></section><section className="login-panel"><form onSubmit={submit}><span className="eyebrow">BEM-VINDO DE VOLTA</span><h2>Acesse seu painel</h2><p>Entre com seu e-mail e senha para continuar.</p>{(erro||erroSessao)&&<div role="alert" className="error">{erro||erroSessao}</div>}<label>E-mail<input autoComplete="username" type="email" required maxLength={180} value={email} onChange={e=>setEmail(e.target.value)} placeholder="seu@email.com"/></label><label>Senha<input autoComplete="current-password" type="password" required maxLength={128} value={senha} onChange={e=>setSenha(e.target.value)} placeholder="Digite sua senha"/></label><button className="button full" disabled={busy}>{busy?'Entrando…':'Entrar no FinControl →'}</button><p className="form-note">Acesso ao painel acadêmico de gestão de usuários, contas e investimentos.</p></form></section></div>;
}

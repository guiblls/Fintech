import { createContext, useContext, useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import { api, ApiError } from '../services/api';
import type { Usuario } from '../types';
interface Auth { usuario: Usuario | null; carregando: boolean; erro: string; entrar: (email:string,senha:string)=>Promise<void>; sair:()=>Promise<void>; atualizar:()=>Promise<void> }
const AuthContext=createContext<Auth | null>(null);
export function AuthProvider({children}:{children:ReactNode}) {
 const [usuario,setUsuario]=useState<Usuario | null>(null);
 const [carregando,setCarregando]=useState(true);
 const [erro,setErro]=useState('');
 async function atualizar() {setUsuario(await api<Usuario>('/auth/me'));}
 useEffect(()=>{
  let active=true;
  api<Usuario>('/auth/me').then(u=>{if(active)setUsuario(u);}).catch(e=>{if(active && (!(e instanceof ApiError)||e.status!==401))setErro(e.message);}).finally(()=>{if(active)setCarregando(false);});
  const expired=()=>{setUsuario(null);setErro('Sua sessão expirou. Entre novamente.');};
  window.addEventListener('sessao-expirada',expired);
  return ()=>{active=false;window.removeEventListener('sessao-expirada',expired);};
 },[]);
 async function entrar(email:string,senha:string) {const u=await api<Usuario>('/auth/login',{method:'POST',body:JSON.stringify({email,senha})});setUsuario(u);setErro('');}
 async function sair() {try {await api('/auth/logout',{method:'POST'});} catch(e) {if(!(e instanceof ApiError)||e.status!==401)throw e;}setUsuario(null);}
 return <AuthContext.Provider value={{usuario,carregando,erro,entrar,sair,atualizar}}>{children}</AuthContext.Provider>;
}
export function useAuth() {const c=useContext(AuthContext);if(!c)throw new Error('AuthProvider ausente');return c;}

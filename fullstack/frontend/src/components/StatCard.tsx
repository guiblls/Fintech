import type { ReactNode } from 'react';
export function StatCard({titulo,valor,detalhe,destaque=false}:{titulo:string;valor:ReactNode;detalhe:string;destaque?:boolean}) {
 return <article className={`stat ${destaque?'primary':''}`}><span>{titulo}</span><strong>{valor}</strong><small>{detalhe}</small></article>;
}

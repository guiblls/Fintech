import { Link } from 'react-router-dom';
export function NotFound(){return <section className="not-found"><strong>404</strong><h1>Esta página saiu do caminho.</h1><p>O endereço não existe. Volte ao painel para continuar.</p><Link className="button" to="/">Voltar ao início</Link></section>;}

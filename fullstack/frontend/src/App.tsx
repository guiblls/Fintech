import { Routes,Route,Navigate,Outlet } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import { Layout } from './components/Layout';
import { Login } from './pages/Login';
import { Dashboard } from './pages/Dashboard';
import { EntityList } from './pages/EntityList';
import { EntityForm } from './pages/EntityForm';
import { NotFound } from './pages/NotFound';
import type { Recurso } from './types';
function Protegido(){const {usuario}=useAuth();return usuario?<Outlet/>:<Navigate to="/login" replace/>;}
export default function App(){const {carregando}=useAuth();if(carregando)return <div className="initial-loading" role="status">Carregando FinControl…</div>;return <Routes><Route path="/login" element={<Login/>}/><Route element={<Protegido/>}><Route element={<Layout/>}><Route index element={<Dashboard/>}/>{(['usuarios','contas','investimentos'] as Recurso[]).map(recurso=><Route path={recurso} key={recurso}><Route index element={<EntityList key={recurso} recurso={recurso}/>}/><Route path="novo" element={<EntityForm key={recurso+'novo'} recurso={recurso}/>}/><Route path=":id/editar" element={<EntityForm key={recurso+'editar'} recurso={recurso}/>}/></Route>)}<Route path="*" element={<NotFound/>}/></Route></Route><Route path="*" element={<NotFound/>}/></Routes>;}

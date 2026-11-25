import React from 'react';
import ReactDOM from 'react-dom/client';
import { demoMode } from './services/demoMode';
import { BrowserRouter,HashRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import App from './App';
import './styles.css';
const Router=demoMode?HashRouter:BrowserRouter;
ReactDOM.createRoot(document.getElementById('root')!).render(<React.StrictMode><Router><AuthProvider><App/></AuthProvider></Router></React.StrictMode>);

export class ApiError extends Error { constructor(message: string, public status: number) { super(message); } }
export async function api<T>(path: string, options: RequestInit = {}): Promise<T> {
 let response: Response;
 try { response=await fetch(`/api${path}`, { ...options, credentials: 'include', headers: { 'Content-Type': 'application/json', 'X-FinControl-Request': 'FinControl', ...options.headers } }); }
 catch { throw new ApiError('Não foi possível acessar o servidor. Confira se o backend está em execução.',0); }
 if (!response.ok) {
  const error=await response.json().catch(() => ({}));
  if(response.status===401 && path!=='/auth/login' && path!=='/auth/me') window.dispatchEvent(new Event('sessao-expirada'));
  throw new ApiError([error.message || 'Não foi possível concluir a operação.', ...(error.errors || [])].join(' • '), response.status);
 }
 if(response.status===204) return undefined as T;
 return response.json() as Promise<T>;
}

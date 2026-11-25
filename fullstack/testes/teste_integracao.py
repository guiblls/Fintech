"""Teste real e opcional da API em execução com Oracle FIAP. Usa apenas Python padrão.
Cria registros fictícios identificados por timestamp e tenta removê-los ao terminar.
"""
import argparse, json, time, http.cookiejar, urllib.request, urllib.error
from datetime import date
p=argparse.ArgumentParser();p.add_argument('--url',default='http://localhost:8080/api');p.add_argument('--email',default='teste@fincontrol.com');args=p.parse_args()
import getpass
senha=getpass.getpass('Senha do usuário de teste (README): ')
client=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))
def request(method,path,body=None,expected=200):
 data=None if body is None else json.dumps(body).encode()
 req=urllib.request.Request(args.url+path,data=data,method=method,headers={'Content-Type':'application/json','X-FinControl-Request':'FinControl'})
 try:
  with client.open(req,timeout=20) as response: status=response.status; raw=response.read()
 except urllib.error.HTTPError as e:status=e.code;raw=e.read()
 assert status==expected,f'{method} {path}: esperado {expected}, recebido {status}: {raw.decode()}'
 print(f'OK {method} {path}: {status}')
 return json.loads(raw) if raw else None
ids={};logged=False
try:
 request('GET','/contas',expected=401)
 request('POST','/auth/login',{'email':args.email,'senha':'senha-incorreta'},401)
 request('POST','/auth/login',{'email':args.email,'senha':senha});logged=True
 suffix=str(time.time_ns())[-11:]
 u={'nome':'Teste automatizado '+suffix,'email':f'teste.{suffix}@example.com','cpf':suffix,'telefone':'','senha':'Temporaria@123'}
 result=request('POST','/usuarios',u,201);ids['usuarios']=result['id'];assert 'senhaHash' not in result
 u['nome']='Usuário atualizado';request('PUT',f"/usuarios/{ids['usuarios']}",u)
 assert request('GET',f"/usuarios/{ids['usuarios']}")['nome']==u['nome']
 c={'usuarioId':ids['usuarios'],'saldo':1000,'tipoConta':'CORRENTE','statusConta':'ATIVA','dataAbertura':date.today().isoformat()}
 ids['contas']=request('POST','/contas',c,201)['id']
 c['saldo']=1250;request('PUT',f"/contas/{ids['contas']}",c)
 assert request('GET',f"/contas/{ids['contas']}")['saldo']==1250
 request('DELETE',f"/usuarios/{ids['usuarios']}",expected=409)
 i={'contaId':ids['contas'],'nomeInvestimento':'Aplicação teste','tipoInvestimento':'CDB','valorAplicado':500,'rentabilidade':10,'dataAplicacao':date.today().isoformat(),'prazo':12,'statusInvestimento':'ATIVO'}
 ids['investimentos']=request('POST','/investimentos',i,201)['id']
 i['valorAplicado']=600;request('PUT',f"/investimentos/{ids['investimentos']}",i)
 assert request('GET',f"/investimentos/{ids['investimentos']}")['valorAplicado']==600
 request('DELETE',f"/contas/{ids['contas']}",expected=409)
 request('POST','/investimentos',{**i,'valorAplicado':-10},400)
 for resource in ids:request('GET','/'+resource)
 print('CRUD e regras de negócio validados na API configurada. Confira também o frontend e o SQL Developer.')
finally:
 for resource in ['investimentos','contas','usuarios']:
  if resource in ids:
   try:request('DELETE',f'/{resource}/{ids[resource]}',expected=204);request('GET',f'/{resource}/{ids[resource]}',expected=404)
   except Exception as e:print('Limpeza pendente:',resource,ids[resource],e)
 if logged:
  request('POST','/auth/logout',expected=204);request('GET','/contas',expected=401)

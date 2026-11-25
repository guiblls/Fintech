-- Executar após 01_schema.sql. Idempotente por e-mail.
-- Usuário fictício para o painel acadêmico de gestão.
MERGE INTO FC7_USUARIO u
USING (SELECT 'teste@fincontrol.com' email FROM dual) s ON (u.email=s.email)
WHEN NOT MATCHED THEN INSERT (id,nome,email,senha_hash,cpf,telefone,data_cadastro)
VALUES (FC7_USUARIO_SEQ.NEXTVAL,'Usuário de Teste',s.email,'RmluQ29udHJvbFRlc3QyNiE=:OZV9CBTKRQJZ9NrQlD44rw0vXMYhJpv69HSxf2rWfQQ=','00000000000','',TRUNC(SYSDATE));
COMMIT;

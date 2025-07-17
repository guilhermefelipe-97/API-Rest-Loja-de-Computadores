-- Script para atualizar a senha do admin com um novo hash BCrypt
-- Senha: 123456
-- Hash BCrypt gerado: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa

-- Verificar o admin atual
SELECT id, email, nome, senha FROM admin WHERE email = 'adm';

-- Atualizar com o novo hash BCrypt
UPDATE admin 
SET senha = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa'
WHERE email = 'adm';

-- Verificar se foi atualizado
SELECT id, email, nome, senha FROM admin WHERE email = 'adm';

-- Verificar o tamanho (deve ser 60)
SELECT LENGTH(senha), senha FROM admin WHERE email = 'adm';

-- Se não existir o admin, criar
INSERT INTO admin (id, email, nome, senha, role, created_at, updated_at, deleted_at)
SELECT 
    nextval('admin_id_seq'), 
    'adm', 
    'Administrador', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa',
    'ADMIN',
    NOW(),
    NOW(),
    NULL
WHERE NOT EXISTS (SELECT 1 FROM admin WHERE email = 'adm'); 
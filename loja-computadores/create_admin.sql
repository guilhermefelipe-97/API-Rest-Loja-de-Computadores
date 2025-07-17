-- Script para excluir admin atual e criar um novo com credenciais corretas
-- Senha: 123456
-- Hash BCrypt: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa

-- 1. Excluir admin atual (soft delete)
UPDATE admin 
SET deleted_at = NOW() 
WHERE email = 'adm';

-- 2. Verificar se foi excluído
SELECT id, email, nome, senha, deleted_at FROM admin WHERE email = 'adm';

-- 3. Criar novo admin com credenciais corretas
INSERT INTO admin (id, email, nome, senha, role, created_at, updated_at, deleted_at)
VALUES (
    nextval('admin_id_seq'), 
    'adm', 
    'Administrador', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa',
    'ADMIN',
    NOW(),
    NOW(),
    NULL
);

-- 4. Verificar se foi criado corretamente
SELECT id, email, nome, senha, role, created_at, updated_at, deleted_at 
FROM admin 
WHERE email = 'adm' AND deleted_at IS NULL;

-- 5. Verificar o tamanho da senha (deve ser 60 caracteres)
SELECT LENGTH(senha), senha 
FROM admin 
WHERE email = 'adm' AND deleted_at IS NULL; 
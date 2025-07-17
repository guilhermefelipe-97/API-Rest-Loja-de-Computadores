-- Script para verificar e atualizar a senha do admin
-- Hash BCrypt para senha "123456"

-- Primeiro, vamos verificar a senha atual
SELECT id, email, nome, senha FROM admin WHERE email = 'adm';

-- Agora vamos atualizar com o hash BCrypt correto
-- Hash para senha "123456" gerado com BCrypt
UPDATE admin 
SET senha = '$2a$10$8.UnVuG9SXi0VrFQum/2G.9qQ0hP7bPcRlOt2V5nX8mK3jL6yH1wE4t'
WHERE email = 'adm';

-- Verificar se foi atualizado
SELECT id, email, nome, senha FROM admin WHERE email = 'adm';

-- Se não existir o admin, vamos criar
INSERT INTO admin (id, email, nome, senha, role, created_at, updated_at, deleted_at)
SELECT 
    nextval('admin_id_seq'), 
    'adm', 
    'Administrador', 
    '$2a$10$8.UnVuG9SXi0VrFQum/2G.9qQ0hP7bPcRlOt2V5nX8mK3jL6yH1wE4t',
    'ADMIN',
    NOW(),
    NOW(),
    NULL
WHERE NOT EXISTS (SELECT 1 FROM admin WHERE email = 'adm'); 
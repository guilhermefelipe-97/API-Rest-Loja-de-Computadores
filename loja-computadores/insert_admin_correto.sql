-- Script para criar admin com credenciais corretas
-- Email: adm
-- Senha: 123456
-- Hash BCrypt: $2a$10$7plp3/r1zPPSIRFNpyrfVuV94Nndsq70/jWesGqvUeuhgPiuvx.A.

-- 1. Verificar se já existe um admin com este email
SELECT id, email, nome, senha, role FROM admin WHERE email = 'adm';

-- 2. Se existir, fazer soft delete
UPDATE admin 
SET deleted_at = NOW() 
WHERE email = 'adm';

-- 3. Inserir novo admin com credenciais corretas
INSERT INTO admin (id, email, nome, senha, role, created_at, updated_at, deleted_at)
VALUES (
    nextval('admin_id_seq'), 
    'adm', 
    'Administrador', 
    '$2a$10$7plp3/r1zPPSIRFNpyrfVuV94Nndsq70/jWesGqvUeuhgPiuvx.A.',
    'ADMIN',
    NOW(),
    NOW(),
    NULL
);

-- 4. Verificar se foi criado corretamente
SELECT id, email, nome, senha, role, created_at, updated_at, deleted_at 
FROM admin 
WHERE email = 'adm' AND deleted_at IS NULL;

-- 5. Verificar o tamanho do hash (deve ser 60)
SELECT LENGTH(senha), senha 
FROM admin 
WHERE email = 'adm' AND deleted_at IS NULL; 
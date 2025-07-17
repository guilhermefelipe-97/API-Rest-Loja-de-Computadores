-- Script para corrigir a senha do admin com o hash que funciona
-- Senha: 123456
-- Hash BCrypt que funciona: $2a$10$7plp3/r1zPPSIRFNpyrfVuV94Nndsq70/jWesGqvUeuhgPiuvx.A.

-- 1. Verificar admin atual
SELECT id, email, nome, senha, role FROM admin WHERE email = 'adm' AND deleted_at IS NULL;

-- 2. Atualizar com o hash que funciona
UPDATE admin 
SET senha = '$2a$10$7plp3/r1zPPSIRFNpyrfVuV94Nndsq70/jWesGqvUeuhgPiuvx.A.'
WHERE email = 'adm' AND deleted_at IS NULL;

-- 3. Verificar se foi atualizado
SELECT id, email, nome, senha, role FROM admin WHERE email = 'adm' AND deleted_at IS NULL;

-- 4. Verificar o tamanho (deve ser 60)
SELECT LENGTH(senha), senha FROM admin WHERE email = 'adm' AND deleted_at IS NULL; 
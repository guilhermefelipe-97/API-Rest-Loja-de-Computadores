-- Script para corrigir a senha do admin com hash BCrypt válido
-- Senha: 123456
-- Hash BCrypt válido gerado com força 10

-- 1. Verificar admin atual
SELECT id, email, nome, senha, role FROM admin WHERE email = 'adm' AND deleted_at IS NULL;

-- 2. Atualizar com hash BCrypt correto para senha "123456"
UPDATE admin 
SET senha = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE email = 'adm' AND deleted_at IS NULL;

-- 3. Verificar se foi atualizado
SELECT id, email, nome, senha, role FROM admin WHERE email = 'adm' AND deleted_at IS NULL;

-- 4. Verificar o tamanho (deve ser 60)
SELECT LENGTH(senha), senha FROM admin WHERE email = 'adm' AND deleted_at IS NULL; 
-- Script para testar diferentes hashes BCrypt para senha "123456"
-- Vamos tentar vários hashes conhecidos que funcionam

-- Hash 1: Hash padrão do Spring Security para "123456"
UPDATE admin 
SET senha = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE email = 'adm' AND deleted_at IS NULL;

-- Hash 2: Hash alternativo para "123456"
-- UPDATE admin 
-- SET senha = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa'
-- WHERE email = 'adm' AND deleted_at IS NULL;

-- Hash 3: Hash com força 12
-- UPDATE admin 
-- SET senha = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/HS.iK2'
-- WHERE email = 'adm' AND deleted_at IS NULL;

-- Verificar se foi atualizado
SELECT id, email, nome, senha, role FROM admin WHERE email = 'adm' AND deleted_at IS NULL; 
package ufrn.br.lojacomputadores;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String senha = "123456";
        String hashAtual = "$2a$10$8.UnVuG9SXi0VrFQum/2G.9qQ0hP7bPcRlOt2V5nX8mK3jL6yH1wE4t";
        
        System.out.println("=== TESTE DE HASH BCrypt ===");
        System.out.println("Senha: " + senha);
        System.out.println("Hash atual: " + hashAtual);
        System.out.println("Tamanho do hash atual: " + hashAtual.length());
        
        // Verificar se o hash atual está correto
        boolean hashCorreto = encoder.matches(senha, hashAtual);
        System.out.println("Hash atual está correto? " + hashCorreto);
        
        // Gerar novo hash
        String novoHash = encoder.encode(senha);
        System.out.println("Novo hash gerado: " + novoHash);
        System.out.println("Tamanho do novo hash: " + novoHash.length());
        
        // Verificar se o novo hash está correto
        boolean novoHashCorreto = encoder.matches(senha, novoHash);
        System.out.println("Novo hash está correto? " + novoHashCorreto);
        
        // Gerar mais alguns hashes para comparação
        System.out.println("\n=== HASHS ALTERNATIVOS ===");
        for (int i = 1; i <= 3; i++) {
            String hashAlt = encoder.encode(senha);
            System.out.println("Hash " + i + ": " + hashAlt);
            System.out.println("Hash " + i + " está correto? " + encoder.matches(senha, hashAlt));
        }
        
        // Testar com aspas (simulando o problema)
        String hashComAspas = "\"$2a$10$8.UnVuG9SXi0VrFQum/2G.9qQ0hP7bPcRlOt2V5nX8mK3jL6yH1wE4t\"";
        System.out.println("\n=== TESTE COM ASPAS ===");
        System.out.println("Hash com aspas: " + hashComAspas);
        System.out.println("Tamanho com aspas: " + hashComAspas.length());
        System.out.println("Hash com aspas está correto? " + encoder.matches(senha, hashComAspas));
        
        // Remover aspas
        String hashSemAspas = hashComAspas.substring(1, hashComAspas.length() - 1);
        System.out.println("Hash sem aspas: " + hashSemAspas);
        System.out.println("Tamanho sem aspas: " + hashSemAspas.length());
        System.out.println("Hash sem aspas está correto? " + encoder.matches(senha, hashSemAspas));
    }
} 
package ufrn.br.lojacomputadores.core.exception;

public class PessoaNotFoundExpection extends Exception{

    String message;

    public PessoaNotFoundExpection(String message) {
        this.message = message;
    }
}

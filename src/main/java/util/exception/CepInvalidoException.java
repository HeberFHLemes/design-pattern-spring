package util.exception;

public class CepInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CepInvalidoException() {
		super();
	}
	
	public CepInvalidoException(String mensagem) {
		super(mensagem);
	}
	
}

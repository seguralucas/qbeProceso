package exit.services.excepciones;

public class ExceptionLongitud extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ExceptionLongitud(String mensaje) {
		super(mensaje);
	}
	public ExceptionLongitud() {
	}

}

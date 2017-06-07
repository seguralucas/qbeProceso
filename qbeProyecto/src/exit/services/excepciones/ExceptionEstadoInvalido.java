package exit.services.excepciones;

public class ExceptionEstadoInvalido  extends Exception{

	private static final long serialVersionUID = 1L;
	
		public ExceptionEstadoInvalido(String mensaje) {
			super(mensaje);
		}
}

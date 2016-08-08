package exit.services.excepciones;

public class ExceptionTipoIncidenteInvalido extends Exception {

	private static final long serialVersionUID = 1L;
	
		public ExceptionTipoIncidenteInvalido(String mensaje) {
			super(mensaje);
		}
}

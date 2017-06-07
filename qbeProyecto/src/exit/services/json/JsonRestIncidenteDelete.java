package exit.services.json;

import exit.services.excepciones.ExceptionEstadoInvalido;
import exit.services.excepciones.ExceptionFormatoFecha;
import exit.services.excepciones.ExceptionIDNoNumerico;
import exit.services.excepciones.ExceptionIDNullIncidente;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.excepciones.ExceptionModoContactoInvalido;
import exit.services.excepciones.ExceptionTipoIncidenteInvalido;

public class JsonRestIncidenteDelete implements IJsonRestEstructura {
	
	private String line;
	private String pathError;
	/*************************/
	private String id_rightnow;
	
	public JsonRestIncidenteDelete(String pathError) {
		this.pathError=pathError;
	}

	@Override
	public JSONHandler createJson(TipoTarea tarea)
			throws ExceptionLongitud, ExceptionEstadoInvalido, ExceptionTipoIncidenteInvalido, ExceptionIDNullIncidente,
			ExceptionModoContactoInvalido, ExceptionIDNoNumerico, ExceptionFormatoFecha {
		if(tarea!=TipoTarea.DELETE)
			return null;
		JSONHandler jhandler= new JSONHandler();
		jhandler.setJsonRestIncidentesDelete(this);
		return jhandler;
	}
	
	

	public String getId_rightnow() {
		return id_rightnow;
	}

	public void setId_rightnow(String id_rightnow) {
		this.id_rightnow = id_rightnow;
	}

	@Override
	public String getPathError() {
		return this.pathError;
	}

	@Override
	public String getLine() {
		return this.line;
	}

	@Override
	public void setLine(String line) {
		this.line=line;
		
	}

}

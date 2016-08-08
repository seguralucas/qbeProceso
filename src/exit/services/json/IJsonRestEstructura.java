package exit.services.json;

import java.io.IOException;

import exit.services.excepciones.ExceptionEstadoInvalido;
import exit.services.excepciones.ExceptionIDNullIncidente;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.excepciones.ExceptionTipoIncidenteInvalido;
import exit.services.fileHandler.CSVHandlerUpdate;

public interface IJsonRestEstructura {
	public JSONHandler createJson(TipoTarea tarea) throws ExceptionLongitud, ExceptionEstadoInvalido, ExceptionTipoIncidenteInvalido, ExceptionIDNullIncidente;
	
	
	default Boolean insertarTrueOFalse(String valor){
		if(valor == null)
			return null;
		if (valor.equalsIgnoreCase("si") || valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("verdadero"))
			return true;
		else if(valor.equalsIgnoreCase("no") || valor.equalsIgnoreCase("false") || valor.equalsIgnoreCase("false"))
			return false;
		else 
			return null;
	}
	
	default String insertarFecha(String valor) throws ExceptionLongitud{
		if(valor==null || valor.length()==0)
			return null;
		String[] fecha=valor.split("/");
		try{
			return fecha[2]+"-"+fecha[1]+"-"+fecha[0];
		}
		catch(Exception e){
			CSVHandlerUpdate csv= new CSVHandlerUpdate();
			try {
				csv.escribirCSV(this.getPathError(), this.getLine());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new ExceptionLongitud();
		}
	}
	default String insertarString(String valor){
		
		if(valor == null || valor.length()==0)
			return null;
		return valor;
	}
	
	public String getPathError();
	public String getLine();
	public void setLine(String line);
}

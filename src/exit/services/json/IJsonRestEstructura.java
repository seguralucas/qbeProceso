package exit.services.json;

import java.io.IOException;

import exit.services.excepciones.ExceptionAnioInvalido;
import exit.services.excepciones.ExceptionEstadoInvalido;
import exit.services.excepciones.ExceptionFormatoFecha;
import exit.services.excepciones.ExceptionIDNoNumerico;
import exit.services.excepciones.ExceptionIDNullIncidente;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.excepciones.ExceptionModoContactoInvalido;
import exit.services.excepciones.ExceptionTipoIncidenteInvalido;
import exit.services.fileHandler.CSVHandler;

public interface IJsonRestEstructura {
	public JSONHandler createJson(TipoTarea tarea) throws ExceptionLongitud, ExceptionEstadoInvalido, ExceptionTipoIncidenteInvalido, ExceptionIDNullIncidente, ExceptionModoContactoInvalido, ExceptionIDNoNumerico, ExceptionFormatoFecha, ExceptionAnioInvalido;
	
	
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
	
	default String insertarFecha(String valor) throws ExceptionFormatoFecha{
		final String PATH_ERROR="error_formato_fecha.csv";
		if(valor==null || valor.length()==0)
			return null;
		CSVHandler csv= new CSVHandler();
		String[] fecha=valor.split("/");
		if(fecha.length==3){
			try{
				return fecha[2]+"-"+fecha[1]+"-"+fecha[0];
			}
			catch(Exception e){
				try {
					csv.escribirCSV(PATH_ERROR, this.getLine());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				throw new ExceptionFormatoFecha("Error generico de fecha");
			}
		}
		fecha=valor.split("-");
		if(fecha.length!=3){
			try {
				csv.escribirCSV(PATH_ERROR, this.getLine());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new ExceptionFormatoFecha("El formato de la fecha es invalido. Deben ser tres digitos separados por guiones medios o barras");
		}
		else
			return valor;

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

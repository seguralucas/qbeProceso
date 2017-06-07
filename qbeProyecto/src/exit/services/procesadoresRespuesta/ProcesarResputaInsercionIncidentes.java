package exit.services.procesadoresRespuesta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.Separadores;

public class ProcesarResputaInsercionIncidentes implements IProcesarRespuestaREST{

	 private ParserXMLWSConnector parser;
	/***************************************************************************************************/
	/**************LOS SIGUIENTES 2 MÉTODOS DEBERÍAN FUCIONARSE EN ALGO MAS GENERICO********************/
	/***************************************************************************************************/
	public ProcesarResputaInsercionIncidentes() {
		this.parser=ParserXMLWSConnector.getInstance();
	}
	@Override
	public void procesarPeticionOK(BufferedReader in, JSONHandler json,int responseCode) throws Exception{
       String inputLine;
       boolean marcaId = true; //Recuperamos el ID
       boolean marcaLookupName = true; //Recuperamos el ID
       String id=null;
       String lookupName=null;
       while ((inputLine = in.readLine()) != null) {
       	if(marcaId && inputLine.contains("id")){
       		id=inputLine.replaceAll("\"id\": ", "").replaceAll(",", "");
       		marcaId=false;
       	}
       	if(marcaLookupName && inputLine.contains("lookupName")){
       		lookupName=inputLine.replaceAll("\"lookupName\": ", "").replaceAll(",", "");
       		marcaLookupName=false;
       	}
       	
       }
       CSVHandler csvHandler = new CSVHandler();
       csvHandler.escribirCSVInsercionIncidentes(parser.getFicheroCSVOK().replace(".csv", "_insertado_ok.csv"),json,id,lookupName);

	 }
	
	@Override
	public void procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode) throws Exception{
		String path=("error_insercion_servidor_codigo_"+responseCode+".txt");
	     	File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
           out.println(json.toString());
           String inputLine;
           while ((inputLine = in.readLine()) != null) {
           	out.println(inputLine);
           }
           CSVHandler csvHandler = new CSVHandler();
           csvHandler.escribirCSV("error_insercion_servidor_codigo_"+responseCode+".csv",json);            
           out.println(Separadores.SEPARADOR_ERROR_PETICION);
           out.close();
           path="json"+parser.getFicheroError();
           fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
           out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
           out.println(json.toString());
           out.println(Separadores.SEPARADOR_ERROR_PETICION);
           out.close();
		 }
	
}

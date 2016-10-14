package exit.services.procesadoresRespuesta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.Separadores;

public class ProcesarRespuestaInsercionContactos implements IProcesarRespuestaREST{
	 private ParserXMLWSConnector parser;
	/***************************************************************************************************/
	/**************LOS SIGUIENTES 2 MÉTODOS DEBERÍAN FUCIONARSE EN ALGO MAS GENERICO********************/
	/***************************************************************************************************/
	public ProcesarRespuestaInsercionContactos() {
		this.parser=ParserXMLWSConnector.getInstance();
	}
	@Override
	public void procesarPeticionOK(BufferedReader in, JSONHandler json,int responseCode) throws Exception{
     	//File fichero = new File(parser.getFicheroOk()); 
        //PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
        //out.println("Response code: "+responseCode);
        //out.println(json.toString());
        String inputLine;
        boolean marca = true; //Recuperamos el ID
        String id=null;
        while ((inputLine = in.readLine()) != null) {
        	//out.println(inputLine);
        	if(marca && inputLine.contains("id")){
        		id=inputLine.replaceAll("\"id\": ", "").replaceAll(",", "");
        		marca=false;
        	}
        }
        CSVHandler csvHandler = new CSVHandler();
        csvHandler.escribirCSVInsercionContacto(parser.getFicheroCSVOK().replace(".csv", "_insertado_ok.csv"),json,id);
        //out.println(SEPARADOR_ERROR_PETICION);
        //out.close();
	 }
	@Override
	public void procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode) throws Exception{
		String path=parser.getFicheroError().replace(".txt", "_error_insercion_"+responseCode+".txt");
	     	File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
            out.println(json.toString());
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	out.println(inputLine);
            }
            CSVHandler csvHandler = new CSVHandler();
            csvHandler.escribirCSV(parser.getFicheroCSVERRORPETICION().replace(".csv", "_insertado_error.csv"),json);            
            out.println(Separadores.SEPARADOR_ERROR_PETICION);
            out.close();
            path="json"+parser.getFicheroError();
            fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
            out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
            out.println(json.toString());
            out.println(Separadores.SEPARADOR_ERROR_PETICION);
            out.close();
		 }
	
/**************************************************************************************************************************/
}

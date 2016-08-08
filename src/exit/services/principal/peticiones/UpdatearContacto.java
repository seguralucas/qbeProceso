package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.Separadores;
import exit.services.principal.WSConector;
import exit.services.procesadoresRespuesta.IProcesarRespuestaREST;
import exit.services.procesadoresRespuesta.ProcesarRespuestaUPDATEContactos;

public class UpdatearContacto {
	IProcesarRespuestaREST iProcesarRespuesta;
	
	 public BufferedReader realizarPeticion(String clientSec, JSONHandler json){
	        try{
	        	
	        	WSConector ws = new WSConector("https://qbe.custhelp.com/services/rest/connect/v1.3/contacts?q=customFields.Qbe.IdAIS='"+clientSec+"'", /*ParserXMLWSConnector.getInstance().getUrl()+"/"+clientSec*/ "GET");
	        	iProcesarRespuesta= new ProcesarRespuestaUPDATEContactos();
	        	HttpURLConnection conn=ws.getConexion();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	ProcesarRespuestaUPDATEContactos a= new ProcesarRespuestaUPDATEContactos();
	            	a.borrarMetodo(in, json, responseCode, clientSec);
	            	//iProcesarRespuesta.procesarPeticionOK(in, json,responseCode);
	            	
	            }
	            else{
	    	     	System.out.println("Error: "+clientSec);
	            in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	            	iProcesarRespuesta.procesarPeticionError(in,json,responseCode);
	            }
	           
	            return in;	 
	            }	                
	            catch (Exception e) {
					e.printStackTrace();
					escrobirErrorAplicacion(json,e.getStackTrace());
					return null;
				}
	        }
	 
	 
	private void escrobirErrorAplicacion(JSONHandler json,StackTraceElement[] stackArray){
 	File fichero = new File(ParserXMLWSConnector.getInstance().getFicheroError()); 
    PrintWriter out;

		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
	        out.println(json.toString());
			for(StackTraceElement ste: stackArray){
				out.write("FileName: "+ste.getFileName()+" Metodo: "+ste.getMethodName()+"Clase "+ste.getClassName()+" Linea "+ste.getLineNumber());
			}		
        out.println(Separadores.SEPARADOR_ERROR_TRYCATCH);
			} catch (IOException e) {
			e.printStackTrace();
		}


	}
}

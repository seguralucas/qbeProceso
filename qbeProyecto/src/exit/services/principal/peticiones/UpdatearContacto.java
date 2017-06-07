package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import exit.services.excepciones.ExceptionServidorNoAlcanzado;
import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.Separadores;
import exit.services.principal.WSConector;
import exit.services.procesadoresRespuesta.IProcesarRespuestaREST;
import exit.services.procesadoresRespuesta.ProcesarRespuestaUPDATEContactos;

public class UpdatearContacto {
	IProcesarRespuestaREST iProcesarRespuesta;
	public static int x=0;
	 public BufferedReader realizarPeticion(String clientSec, JSONHandler json){
	        try{
	        	CSVHandler csv= new CSVHandler();
	        	WSConector ws = new WSConector("https://qbe.custhelp.com/services/rest/connect/v1.3/contacts?q=customFields.Qbe.IdAIS='"+clientSec+"'", /*ParserXMLWSConnector.getInstance().getUrl()+"/"+clientSec*/ "GET");
	        	iProcesarRespuesta= new ProcesarRespuestaUPDATEContactos();
	        	HttpURLConnection conn=ws.getConexion();
	        	BufferedReader in=null;
	        	int responseCode=-1;
	            responseCode = conn.getResponseCode();

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
	            catch (ConnectException e) {
					CSVHandler csv= new CSVHandler();
					try {
						csv.escribirCSV(CSVHandler.PATH_ERROR_SERVER_NO_ALCANZADO, json.getLine());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return null;
				}
	            catch (Exception e) {
					CSVHandler csv= new CSVHandler();
					csv.escribirErrorException(json,e.getStackTrace());
					return null;
				}
	        }
	 
	 

}

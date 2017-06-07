package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import exit.extras.GetIdQuery;
import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.Separadores;
import exit.services.principal.WSConector;
import exit.services.procesadoresRespuesta.IProcesarRespuestaREST;
import exit.services.procesadoresRespuesta.ProcesarRespuestaInsercionContactos;
import exit.services.procesadoresRespuesta.ProcesarResputaInsercionIncidentes;

public class VerificarSAC {
	IProcesarRespuestaREST iProcesarRespuesta;
	public static int x=0;
	 public BufferedReader realizarPeticion(JSONHandler json){
	        try{
	        	WSConector ws = new WSConector("GET","https://qbe.custhelp.com/services/rest/connect/v1.3/queryResults/?query=SELECT%20customFields.Qbe.NumeroQbe,ID%20FROM%20incidents%20where%20customFields.Qbe.NumeroQbe%20=%20%27"+json.getJsonRestIncidentes().getNro_sac()+"%27","application/json");
	        	HttpURLConnection conn=ws.getConexion();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	System.out.println("Ok: "+ ++x);	           
	            	String line;
	    			JSONParser parser = new JSONParser();
	    			StringBuilder builder = new StringBuilder();
	    	        while ((line = in.readLine()) != null) {
	    	            builder.append(line);
	    	        }
	    	        String jsonString = builder.toString();
	    			JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
	    			JSONArray items=(JSONArray) jsonObject.get("items");
	    			JSONObject jsonItem= (JSONObject) items.get(0);
	    			Long count= (Long)jsonItem.get("count");
	    			if(count<1){
	    				InsertarIncidente insertar= new InsertarIncidente();
	    				insertar.realizarPeticion(json);
	    			}
	    			else{
	    				CSVHandler csv= new CSVHandler();
						csv.escribirCSV(CSVHandler.PATH_SAC_EXISTENTE, json.getLine());
	    			}
	    			return in;
	            }
	            else{
//	            	in = new BufferedReader(
//		                    new InputStreamReader(conn.getErrorStream()));
//	            	String l;
//	            	while((l=in.readLine())!=null)
//	            		System.out.println(l);
//	            	System.out.println("error: "+ ++y);
	            	throw new Exception();

	            }
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

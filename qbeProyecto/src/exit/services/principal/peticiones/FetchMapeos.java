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
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import exit.services.excepciones.ExceptionServidorNoAlcanzado;
import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestIncidentes;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.Principal;
import exit.services.principal.Separadores;
import exit.services.principal.WSConector;
import exit.services.procesadoresRespuesta.IProcesarRespuestaREST;
import exit.services.procesadoresRespuesta.ProcesarRespuestaInsercionContactos;

public class FetchMapeos {
	IProcesarRespuestaREST iProcesarRespuesta;
	
	public void mapear(String accion) throws ConnectException, Exception{
		if(accion.equalsIgnoreCase(Principal.INSERTAR_INCIDENTES)){
			realizarPeticion("https://qbe.custhelp.com/services/rest/connect/v1.3/Qbe.TipoIncidente", JsonRestIncidentes.mapTipoIncidente);
			realizarPeticion("https://qbe.custhelp.com/services/rest/connect/v1.3/Qbe.ModoContacto", JsonRestIncidentes.mapModoContacto);
		}
	}
	
	 private BufferedReader realizarPeticion(String url, HashMap<String, Long> map) throws ConnectException, Exception{
	        	WSConector ws = new WSConector("GET",url,"application/json");
	        	iProcesarRespuesta= new ProcesarRespuestaInsercionContactos();
	        	HttpURLConnection conn=ws.getConexion();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	        		String line;
	        		JSONParser parser = new JSONParser();
	        		StringBuilder builder = new StringBuilder();
	                while ((line = in.readLine()) != null) {
	                    builder.append(line);
	                }
	                String jsonString = builder.toString();
	    			JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
	    			JSONArray jsonArrayItems= (JSONArray) jsonObject.get(("items"));
	    			for(int i=0;i<jsonArrayItems.size();i++){
		    			JSONObject jsonItems=(JSONObject)jsonArrayItems.get(i);
		    			String lookupName=(String)jsonItems.get("lookupName");
		    			Long id=(Long)jsonItems.get("id");
		    			map.put(lookupName.toUpperCase(),id);
	    			}
	    		}
	            else{
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	        		StringBuilder builder = new StringBuilder();
	        		String inputLine;
	                while ((inputLine = in.readLine()) != null) {
	                    builder.append(inputLine);
	                }
	                String error = builder.toString();
	            	throw new ConnectException(error);
	            }
          
	            return in;	 

       }
	        }
	 
	 


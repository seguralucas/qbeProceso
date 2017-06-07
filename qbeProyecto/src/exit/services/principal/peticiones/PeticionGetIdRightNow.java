package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import exit.services.fileHandler.CSVHandler;
import exit.services.json.EstructuraGetIdRightNow;
import exit.services.json.JSONHandler;
import exit.services.principal.WSConector;
import exit.services.procesadoresRespuesta.IProcesarRespuestaREST;
import exit.services.procesadoresRespuesta.ProcesarRespuestaUPDATEContactos;

public class PeticionGetIdRightNow {
	IProcesarRespuestaREST iProcesarRespuesta;
	 public BufferedReader realizarPeticion(EstructuraGetIdRightNow estructuraRightNow){
	        try{
	        	CSVHandler csv= new CSVHandler();
	        	WSConector ws = new WSConector("https://qbe.custhelp.com/services/rest/connect/v1.3/queryResults/?query=select%20ID%20from%20contacts%20where%20customFields.Qbe.idais='"+estructuraRightNow.getIdDeAIS()+"'", /*ParserXMLWSConnector.getInstance().getUrl()+"/"+clientSec*/ "GET");
	        	iProcesarRespuesta= new ProcesarRespuestaUPDATEContactos();
	        	HttpURLConnection conn=ws.getConexion();
	        	BufferedReader in=null;
	        	int responseCode=-1;
	            responseCode = conn.getResponseCode();
            	String line;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	    			StringBuilder builder = new StringBuilder();
	    	        while ((line = in.readLine()) != null) {
	    	            builder.append(line);
	    	        }
	    	        String jsonString = builder.toString();
	    			JSONParser parser = new JSONParser();
	    			JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
	    			JSONArray jArray= (JSONArray)jsonObject.get("items");
	    			jsonObject=(JSONObject) jArray.get(0);
	    			Long count=(Long)jsonObject.get("count");
	    			if(count>=1){
	    				jArray=(JSONArray)jsonObject.get("rows");
	    				jArray= (JSONArray)jArray.get(0);
	    				String id= (String)jArray.get(0);
	    				estructuraRightNow.setId(id);
	    				csv.escribirCSVGetIdRightNow(estructuraRightNow);
	    			}
	    			else
	    				csv.escribirCSV(CSVHandler.PATH_ID_NO_ENCONTRADO, estructuraRightNow.getLine());

	    				
	            }
	            else{
	            	System.out.println("error");
	            in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
				csv.escribirCSV(CSVHandler.PATH_ERROR_EXCEPTION, estructuraRightNow.getLine());


	            }
	           
	            return in;	 
	            }	              
	            catch (ConnectException e) {
					CSVHandler csv= new CSVHandler();
					try {
						csv.escribirCSV(CSVHandler.PATH_ERROR_SERVER_NO_ALCANZADO, estructuraRightNow.getLine());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return null;
				}
	            catch (Exception e) {
					CSVHandler csv= new CSVHandler();
					e.printStackTrace();
					csv.escribirErrorException(e.getStackTrace());
					return null;
				}
	        }
}

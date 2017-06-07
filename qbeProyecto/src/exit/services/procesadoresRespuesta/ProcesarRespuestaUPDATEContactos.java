package exit.services.procesadoresRespuesta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import exit.services.excepciones.ExceptionFormatoFecha;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.Separadores;
import exit.services.principal.WSConector;
import exit.services.principal.peticiones.InsertarContacto;

public class ProcesarRespuestaUPDATEContactos implements IProcesarRespuestaREST{
	 private ParserXMLWSConnector parser;
	 
	 public ProcesarRespuestaUPDATEContactos(){
		 this.parser=ParserXMLWSConnector.getInstance();
	 }
	 
		public void borrarMetodo(BufferedReader in, JSONHandler json, int responseCode,String clientSec) throws Exception {

			String line;
			JSONParser parser = new JSONParser();
			StringBuilder builder = new StringBuilder();
	        while ((line = in.readLine()) != null) {
	            builder.append(line);
	        }
	        String jsonString = builder.toString();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
			JSONArray jsonArrayItems= (JSONArray) jsonObject.get(("items"));
			JSONObject jsonItems;
			try{
				jsonItems=(JSONObject)jsonArrayItems.get(0);
			}
			catch(Exception e){
				System.out.println("ClientSec no encontrado, se procederá a insertar "+clientSec );
				insertarContacto(json);
				return;
			}
			Long id= (Long)jsonItems.get("id");
			System.out.println(id+";"+clientSec);
			realizarPeticion(json,String.valueOf(id));
		}
	
	@Override
	public void procesarPeticionOK(BufferedReader in, JSONHandler json, int responseCode) throws Exception {
		System.out.println("ok");

		String line;
		JSONParser parser = new JSONParser();
		StringBuilder builder = new StringBuilder();
        while ((line = in.readLine()) != null) {
            builder.append(line);
        }
        String jsonString = builder.toString();
		JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
		JSONArray jsonArrayItems= (JSONArray) jsonObject.get(("items"));
		JSONObject jsonItems;
		try{
		jsonItems=(JSONObject)jsonArrayItems.get(0);
		}
		catch(Exception e){
			System.out.println("ClientSec no encontrado, se procederá a insertar");
			insertarContacto(json);
			return;
		}
		Long id= (Long)jsonItems.get("id");
		JSONHandler qbe = (JSONHandler)((JSONHandler)json.get("customFields")).get("Qbe");
		 String clientSec=(String)qbe.get("IdBI");
		System.out.println(id+"");
		realizarPeticion(json,String.valueOf(id));
	}

	@Override
	public void procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode) throws Exception{
			File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(parser.getFicheroError().replace(".txt", "_"+responseCode+".txt")); 
	        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
            out.println(json.toString());
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	out.println(inputLine);
            }
            CSVHandler csvHandler = new CSVHandler();
            csvHandler.escribirCSV(parser.getFicheroCSVERRORPETICION(),json);            
            out.println(Separadores.SEPARADOR_ERROR_PETICION);
            out.close();
            fichero = DirectorioManager.getDirectorioFechaYHoraInicio("json"+parser.getFicheroError()); 
	        out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
            out.println(json.toString());
            out.println(Separadores.SEPARADOR_ERROR_PETICION);
            out.close();
		 }
	
	private void insertarContacto(JSONHandler jsonHandlerUpdate){
		boolean excepcion= false;
		InsertarContacto insertarContacto= new InsertarContacto();
		JSONHandler jsonH=null;
		try{
				jsonH=jsonHandlerUpdate.getJsonRestEstructura().createJson(TipoTarea.INSERTAR);
			//	System.out.println(jsonH);
		}
		catch(ExceptionLongitud e){
			excepcion=true;
		}
		catch(ExceptionFormatoFecha e){
			excepcion=true;
		}
		if(!excepcion){
			try{
			insertarContacto.realizarPeticion(jsonH);
			}
			catch(Exception e){
				CSVHandler csv= new CSVHandler();
				try {
					csv.escribirCSV(ParserXMLWSConnector.getInstance().getFicheroCSVERROREJECUCION().replace(".csv", "_clientsec_no_insertado.csv"), jsonH.getLine());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		/*********************************************/

	} 
	
	
	public void procesarPeticionTerminada(BufferedReader in, JSONHandler json, int responseCode) throws Exception{
            CSVHandler csvHandler = new CSVHandler();
            csvHandler.escribirCSV(parser.getFicheroCSVOK(),json);            
		 }
	
	 public BufferedReader realizarPeticion(JSONHandler json,String idRN){
	        try{
	        	
	        	WSConector ws = new WSConector("UPDATE","https://qbe.custhelp.com/services/rest/connect/v1.3/contacts/"+idRN,"application/json");
	        	HttpURLConnection conn=ws.getConexion();
	        	DataOutputStream wr = new DataOutputStream(
	        			conn.getOutputStream());
	        	wr.write(json.toStringSinEnter().getBytes("UTF-8"));
	        	wr.flush();
	        	wr.flush();
	        	wr.close();
	            int responseCode = conn.getResponseCode();
	            if(responseCode!= 200){
	            	BufferedReader in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	            	this.procesarPeticionError(in,json,responseCode);
	            }
	            else{
	            	BufferedReader in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	this.procesarPeticionTerminada(in,json,responseCode);
	            }
            	return null;	 
	            }	                
	            catch (Exception e) {
					e.printStackTrace();
					return null;
				}
	        }

}

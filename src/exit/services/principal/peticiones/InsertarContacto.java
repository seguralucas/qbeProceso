package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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
import exit.services.procesadoresRespuesta.ProcesarRespuestaInsercionContactos;

public class InsertarContacto {
	
	IProcesarRespuestaREST iProcesarRespuesta;
	
	 public BufferedReader realizarPeticion(JSONHandler json){
	        try{
	        	WSConector ws = new WSConector("POST",ParserXMLWSConnector.getInstance().getUrl(),"application/json");
	        	iProcesarRespuesta= new ProcesarRespuestaInsercionContactos();
	        	HttpURLConnection conn=ws.getConexion();
	        	DataOutputStream wr = new DataOutputStream(
	        			conn.getOutputStream());
	        	wr.write(json.toStringSinEnter().getBytes("UTF-8"));
	        	wr.flush();
	        	wr.flush();
	        	wr.close();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            if(responseCode == 201){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	iProcesarRespuesta.procesarPeticionOK(in, json,responseCode);
	            	
	            }
	            else{
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

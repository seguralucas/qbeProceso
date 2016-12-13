package exit.extras;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.WSConector;
import exit.services.procesadoresRespuesta.IProcesarRespuestaREST;
import exit.services.procesadoresRespuesta.ProcesarRespuestaInsercionContactos;

public class GetIdQuery {
	public static int x;
	public static int y;
	 public static void realizarPeticion(String registro){
	        try{
	        	String[] separado=registro.split(",");
	        	WSConector ws = new WSConector("GET","https://qbe.custhelp.com/services/rest/connect/v1.3/queryResults/?query=SELECT%20customFields.Qbe.NumeroQbe,ID%20FROM%20incidents%20where%20referenceNumber%20=%20%27"+separado[1]+"%27","application/json");
	        	HttpURLConnection conn=ws.getConexion();
//	        	DataOutputStream wr = new DataOutputStream(
	//        			conn.getOutputStream());
	      //  	wr.close();
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
	               		GetIdQuery.escribir("noExisteSacEnServices.csv", registro);
	               		return;
	    			}
	    			JSONArray rows= (JSONArray)jsonItem.get("rows");
	    			JSONArray rows2= (JSONArray)rows.get(0);
	    			String id= (String)rows2.get(1);
	    			registro=id+","+registro;
	    			escribir("correctos.csv",registro);

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
         
           catch (Exception e) {
           	System.out.println("error: "+ ++y);
           		GetIdQuery.escribir("errorGenerico.csv", registro);
        	   e.printStackTrace();
        	   
        	   }
	        finally{
	            System.out.println("Total: "+(x+y));
	        }
       }
	 
	 private static void escribir(String nombre,String registro){
		 try(FileWriter fw = new FileWriter("H:/Users/GAS/Desktop/borrar/procesado/"+nombre, true);
 			    BufferedWriter bw = new BufferedWriter(fw);
 			    PrintWriter out = new PrintWriter(bw))
 			{
 			    out.println(registro);
 			} catch (IOException e2) {
 				e2.printStackTrace();
 			}			
	 }
}

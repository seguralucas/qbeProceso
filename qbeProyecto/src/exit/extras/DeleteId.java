package exit.extras;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import exit.services.principal.WSConector;

public class DeleteId {
	public static int x;
	public static int y;
	 public static void realizarPeticion(String registro){
	        try{
	        	String[] separado=registro.split(",");
	        	WSConector ws = new WSConector("DELETE","https://qbe.custhelp.com/services/rest/connect/v1.3/incidents/"+separado[0],"application/json");
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
	    			escribir("eliminado.csv",registro);

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
           		DeleteId.escribir("errorGenerico.csv", registro);
        	   e.printStackTrace();
        	   
        	   }
	        finally{
	            System.out.println("Total: "+(x+y));
	        }
       }
	 
	 private static void escribir(String nombre,String registro){
		 try(FileWriter fw = new FileWriter("H:/Users/GAS/Desktop/borrar/procesado/eliminados/"+nombre, true);
 			    BufferedWriter bw = new BufferedWriter(fw);
 			    PrintWriter out = new PrintWriter(bw))
 			{
 			    out.println(registro);
 			} catch (IOException e2) {
 				e2.printStackTrace();
 			}			
	 }
}



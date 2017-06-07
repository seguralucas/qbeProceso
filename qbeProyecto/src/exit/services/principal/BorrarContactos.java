package exit.services.principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.parser.ParserXMLWSConnector;
import exit.services.util.Contador;

public class BorrarContactos {
//	public static void main(String[] args) throws Exception {
//		BorrarContactos bc = new BorrarContactos();
///*		bc.getAllContactos();
//		bc.separarIdContactosABorrar();*/
//		bc.ejecutarBorrado();
//		
//	}
//	
//	public void getAllContactos() throws Exception{
//		final ParserXMLWSConnector parser =  ParserXMLWSConnector.getInstance();
//		final WSConector wsCon = new WSConector();
//		wsCon.getAllContacts("Contactos");
//	}
//	
//	
//	public void separarIdContactosABorrar() throws Exception{
//	 	File fichero = new File("Contactos"); 
//	 	BufferedReader br = new BufferedReader(new FileReader(fichero));
//        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File("IdsAEliminar"), true)));
//        String inputLine;
//        while ((inputLine = br.readLine()) != null) {
//        	if(inputLine.contains("id")){
//	        	out.println(inputLine.replace("\"id\": ", "").replace(",", "").trim());
//        	}
//        }
//		out.close();
//		br.close();
//	}
//	
//	public void ejecutarBorrado() throws Exception{
//		final ParserXMLWSConnector parser = new ParserXMLWSConnector();
//		final WSConector wsCon = new WSConector(parser);
//		ArrayList<String> listaId = new ArrayList<String>();
//        try(BufferedReader br = new BufferedReader(new FileReader(new File("IdsAEliminar")))){
//        	String linea;
//        	while((linea=br.readLine())!=null)
//		 	listaId.add(linea);
//        }
//        catch(Exception e){
//        	e.printStackTrace();
//        }
//        
//        CountDownLatch latch = new CountDownLatch(parser.getNivelParalelismo());
//    	ExecutorService exec = Executors.newFixedThreadPool(parser.getNivelParalelismo());
//    	try {
//        	int i=0;
//
//			for(String id: listaId){
//    	        exec.submit(new Runnable() {
//    	            @Override
//    	            public void run() {	  
//						try {
//							System.out.println(Contador.x++);
//							wsCon.deleteContact(id);
//							if(Contador.x >=1000 && (Contador.x%1000)-parser.getNivelParalelismo()<0){
//								Thread.sleep(1000);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//			            	InputStream stream = new ByteArrayInputStream(id.getBytes(StandardCharsets.UTF_8));
//			            	try {
//								wsCon.escribirFichero("idError.txt",stream);
//							} catch (Exception e1) {
//								e1.printStackTrace();
//							}
//						}
//    	            }
//    	        });
//    	    }
//    	}
//        catch(Exception e){
//        	e.printStackTrace();
//    	} finally {
//    		System.out.println("Fin...");
//    	} 
//	}
//	
//	public void getAllContacts(String path) throws Exception{
//	 	URL url = new URL(ParserXMLWSConnector.getInstance().getUrl());
//	 	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		String userPassword = ParserXMLWSConnector.getInstance().getUser() + ":" + ParserXMLWSConnector.getInstance().getPassword();
//		String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
//		conn.setRequestProperty("Authorization", "Basic " + encoding);	 
//		
//	 	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//	 	File fichero = new File("Contactos"); 
//     PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
//     String inputLine;
//     while ((inputLine = br.readLine()) != null) {
//     	out.println(inputLine);
//
//     }
//	}
//	
//	
//	
//	
//	
//	
//	/**************************************************************************************************/
//	/***********************ESTO ES PARA LOS DELETE, ESTÁ MAL DISEÑADO*********************************/
//	/**************************************************************************************************/
//	public void deleteContact(String id) throws Exception{
//			URL url = new URL(ParserXMLWSConnector.getInstance().getUrl()+id);
//			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//			httpCon.setDoOutput(true);
//			String userPassword = ParserXMLWSConnector.getInstance().getUser() + ":" + ParserXMLWSConnector.getInstance().getPassword();
//			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
//			httpCon.setRequestProperty("Authorization", "Basic " + encoding);	 
//			httpCon.setRequestMethod("DELETE");
//         int responseCode = httpCon.getResponseCode();
//         if(responseCode == 200){
//         	escribirFichero("eliminoOK.txt",httpCon.getInputStream());
//         }
//         else{
//         	escribirFichero("eliminoERROR.txt",httpCon.getErrorStream());
//         	InputStream stream = new ByteArrayInputStream(id.getBytes(StandardCharsets.UTF_8));
//         	escribirFichero("idError.txt",stream);
//         	
//         }
//			httpCon.getInputStream();
//	}
//	/**************************************************************************************************/
//	/**************************************************************************************************/
//
//
//	
//	public void escribirFichero(String path, InputStream is) throws Exception{
//		escribirFichero(path,is,Separadores.SEPARADOR_ERROR_PETICION);
//
//	}
//	
//	private void escribirFichero(String path, InputStream is, String separador) throws Exception{
//		BufferedReader br = new BufferedReader(new InputStreamReader(is));
//	 	File fichero = new File(path); 
//     PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
//     String inputLine;
//     while ((inputLine = br.readLine()) != null) {
//     	out.println(inputLine);
//     }
//     out.println(separador);
//		out.close();
//	}
}

package exit.services.principal;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.fileHandler.CSVHandlerUpdate;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestEstructura;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.util.Contador;
import exit.services.util.Timer;

public class EjecutorInsercionContactos extends Thread{
	
	@Override
	    public void run(){
		/***********************************************************/
		final Timer t= new Timer();
		t.tomarTiempoUno();
		/**********************************************************/
	    	 	System.out.println("Nivel de paralelismo: "+ParserXMLWSConnector.getInstance().getNivelParalelismo());
	    		CSVHandlerUpdate csv = new CSVHandlerUpdate();
	    	 	ArrayList<File> pathsCSV= csv.getCSVAEjecutar(ParserXMLWSConnector.getInstance().getPathCSVRegistros());
	    		for(File path:pathsCSV){
	    			System.out.println( path.getName() );
	    		 	ConvertidosJSONCSV jsonHandler = new ConvertidosJSONCSV();
	    			jsonHandler.convertirCSVaArrayListJSON(path);
	    				//System.out.println(json.createJson());
	    	        	CountDownLatch latch = new CountDownLatch(ParserXMLWSConnector.getInstance().getNivelParalelismo());
	    	        	ExecutorService exec = Executors.newFixedThreadPool(ParserXMLWSConnector.getInstance().getNivelParalelismo());
	    	        	try {
		    	        	int i=0;
	    	    			for(JsonRestEstructura json: jsonHandler.getListaJson()){
	    	        	        exec.submit(new Runnable() {
	    	        	            @Override
	    	        	            public void run() {	  
	    	        	            	WSConector wsCon;
										try {
											System.out.println(Contador.x++);
											
											
											/**********************************************/
											boolean excepcion= false;
											InsertarContacto insertarContacto= new InsertarContacto();
											JSONHandler jsonH=null;
											try{
													jsonH=json.createJson(TipoTarea.INSERTAR);
												//	System.out.println(jsonH);
											}
											catch(ExceptionLongitud e){
												excepcion=true;
											}
											if(!excepcion){
												insertarContacto.realizarPeticion(jsonH);
											}
											/*********************************************/

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
	    	        	            }
	    	        	        });
	    	        	    }
	    	        	}
	    	            catch(Exception e){
	    	            	e.printStackTrace();
	    	        	} finally {
	    	        		System.out.println("Fin...");
	    	        	} 
	    		}
/*	        	for(JSONObject jsobj: json.getListaJson())
	        		System.out.println(jsobj);*/

   
	       
	        
	    }
}

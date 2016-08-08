package exit.services.principal.ejecutores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.excepciones.ExceptionLongitud;
import exit.services.fileHandler.CSVHandlerUpdate;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestClienteEstructura;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.peticiones.UpdatearContacto;
import exit.services.util.Contador;

public class EjecutorUpdateContactosDistintosFicheros {
	
	
	public void updatear() throws InterruptedException, IOException{
		CSVHandlerUpdate csv = new CSVHandlerUpdate();
 	ArrayList<File> pathsCSVEjecutar= csv.getCSVAEjecutar(ParserXMLWSConnector.getInstance().getPathCSVRegistros());
 	for(File path:pathsCSVEjecutar){
	 	try {
			DirectorioManager.SepararFicheros(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	ArrayList<File> filesCSVDivididos=csv.getAllCSV(DirectorioManager.getPathFechaYHoraInicioDivision());
    	ExecutorService workers = Executors.newFixedThreadPool(ParserXMLWSConnector.getInstance().getNivelParalelismo());      	
	    List<Callable<Void>> tasks = new ArrayList<>();
	    System.out.println("Nivel de paralelismo: "+ParserXMLWSConnector.getInstance().getNivelParalelismo());
		for(File file: filesCSVDivididos){
			tasks.add(new Callable<Void>() {
	        public Void call() {
	        	ConvertidosJSONCSV csvThread= new ConvertidosJSONCSV();
	        	try {
	        		JSONHandler jsonH=null;
	        		JsonRestClienteEstructura jsonEst=null;
	        		while(!csvThread.isFin()){
		        		boolean excepcion=false;
	        			jsonEst = csvThread.convertirCSVaArrayListJSONLineaALinea(file);
						UpdatearContacto update= new UpdatearContacto();
	        			if(jsonEst!=null){
	        				try{
	    						jsonH=jsonEst.createJson(TipoTarea.UPDATE);
	    				}
	    				catch(ExceptionLongitud e){
	    					excepcion=true;
	    				}
	    				if(!excepcion){
	    					try{
	    	    	        String clientSecAux= jsonEst.getCliensec();
	    	    	        //System.out.println(clientSecAux);
	    					update.realizarPeticion(clientSecAux,jsonH);
	    					}
	    					catch(Exception e){
	    						CSVHandlerUpdate csv= new CSVHandlerUpdate();
	    						try {
	    							csv.escribirCSV(ParserXMLWSConnector.getInstance().getFicheroCSVERROREJECUCION().replace(".csv", "_error_no_espeficado.csv"), jsonH.getLine());
	    						} catch (IOException e1) {
	    							e1.printStackTrace();
	    						}

	    					
	    				}
	    				Contador.x++;
	    				//System.out.println(Contador.x);
	    				if(Contador.x%1000==0){
	    			  		FileWriter fw = new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("cantidadProcesada.txt"));
	    		    		fw.write("el proceso lleva procesado un total de: "+Contador.x+" Registros");
	    		    		fw.close();
	    				}
	        			
	        			}
	        		}
				} 
	        	}
	        	catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
	        }
	      });
	    }
	    workers.invokeAll(tasks);
	    workers.shutdown();
	//    path.delete();
 		}
	}
}


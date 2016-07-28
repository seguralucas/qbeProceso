package exit.services.principal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.fileHandler.CSVHandlerUpdate;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestEstructura;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.util.Contador;

public class EjecutorUpdateContactos{

	public void updatear() throws InterruptedException{
   		CSVHandlerUpdate csv = new CSVHandlerUpdate();
	 	ArrayList<File> pathsCSV= csv.getCSVAEjecutar(ParserXMLWSConnector.getInstance().getPathCSVRegistros());
	 	for(File path:pathsCSV){
		 	ConvertidosJSONCSV jsonHandler = new ConvertidosJSONCSV();
			jsonHandler.convertirCSVaArrayListJSON(path);
        	ExecutorService workers = Executors.newFixedThreadPool(ParserXMLWSConnector.getInstance().getNivelParalelismo());      	
        	//System.out.println(ParserXMLWSConnector.getInstance().getNivelParalelismo());
    	    List<Callable<Void>> tasks = new ArrayList<>();
			for(JsonRestEstructura json: jsonHandler.getListaJson()){
				tasks.add(new Callable<Void>() {
    	        public Void call() {
    	        	String clientSecAux= json.getCliensec();
					boolean excepcion= false;
					UpdatearContacto update= new UpdatearContacto();
					JSONHandler jsonH=null;
					try{
							jsonH=json.createJson(TipoTarea.UPDATE);
					}
					catch(ExceptionLongitud e){
						excepcion=true;
					}
					if(!excepcion){
						try{
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
					}
					Contador.x++;
					System.out.println(Contador.x);
					return null;
    	        }
    	      });
    	    }
    	    workers.invokeAll(tasks);
    	    workers.shutdown();
    	    path.delete();
		}
	}
}
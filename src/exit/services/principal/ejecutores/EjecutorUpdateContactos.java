package exit.services.principal.ejecutores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.excepciones.ExceptionFormatoFecha;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.fileHandler.FilesAProcesarManager;
import exit.services.fileHandler.Tipo_Json;
import exit.services.json.IJsonRestEstructura;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestClienteEstructura;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.peticiones.UpdatearContacto;
import exit.services.util.Contador;

public class EjecutorUpdateContactos{

	public void updatear() throws InterruptedException{
   		CSVHandler csv = new CSVHandler();
	 	ArrayList<File> pathsCSV= FilesAProcesarManager.getInstance().getCSVAProcesar(ParserXMLWSConnector.getInstance().getPathCSVRegistros());
	 	for(File path:pathsCSV){
		 	ConvertidosJSONCSV jsonHandler = new ConvertidosJSONCSV();
			jsonHandler.convertirCSVaArrayListJSON(path,Tipo_Json.CLIENTE);
        	ExecutorService workers = Executors.newFixedThreadPool(ParserXMLWSConnector.getInstance().getNivelParalelismo());      	
        	//System.out.println(ParserXMLWSConnector.getInstance().getNivelParalelismo());
    	    List<Callable<Void>> tasks = new ArrayList<>();
			for(IJsonRestEstructura jsonI: jsonHandler.getListaJson()){
				tasks.add(new Callable<Void>() {
    	        public Void call() {
    	        	JsonRestClienteEstructura json=(JsonRestClienteEstructura)jsonI;
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
					catch(ExceptionFormatoFecha e){
						excepcion=true;
					}
					if(!excepcion){
						try{
						update.realizarPeticion(clientSecAux,jsonH);
						}
						catch(Exception e){
							CSVHandler csv= new CSVHandler();
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
		}
	}
}

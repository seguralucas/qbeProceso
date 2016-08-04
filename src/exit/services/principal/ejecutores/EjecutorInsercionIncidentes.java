package exit.services.principal.ejecutores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.fileHandler.CSVHandlerUpdate;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.fileHandler.Tipo_Json;
import exit.services.json.IJsonRestEstructura;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestClienteEstructura;
import exit.services.json.JsonRestIncidentes;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.ExceptionLongitud;
import exit.services.principal.peticiones.InsertarIncidente;
import exit.services.principal.peticiones.UpdatearContacto;
import exit.services.util.Contador;

public class EjecutorInsercionIncidentes {
	public void insertar() throws InterruptedException{
   		CSVHandlerUpdate csv = new CSVHandlerUpdate();
	 	ArrayList<File> pathsCSV= csv.getCSVAEjecutar(ParserXMLWSConnector.getInstance().getPathCSVRegistros());
	 	for(File path:pathsCSV){
		 	ConvertidosJSONCSV jsonHandler = new ConvertidosJSONCSV();
			jsonHandler.convertirCSVaArrayListJSON(path,Tipo_Json.INCIDENTE);
        	ExecutorService workers = Executors.newFixedThreadPool(ParserXMLWSConnector.getInstance().getNivelParalelismo());      	
        	//System.out.println(ParserXMLWSConnector.getInstance().getNivelParalelismo());
    	    List<Callable<Void>> tasks = new ArrayList<>();
			for(IJsonRestEstructura jsonI: jsonHandler.getListaJson()){
				tasks.add(new Callable<Void>() {
    	        public Void call() {
    	        	JsonRestIncidentes json=(JsonRestIncidentes)jsonI;
					boolean excepcion= false;
					InsertarIncidente insertar= new InsertarIncidente();
					JSONHandler jsonH=null;
					try{
							jsonH=json.createJson(TipoTarea.INSERTAR);
					}
					catch(Exception e){
						excepcion=true;
					}
					if(!excepcion){
						try{
							insertar.realizarPeticion(jsonH);
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
    	//    path.delete();
		}
	}
}

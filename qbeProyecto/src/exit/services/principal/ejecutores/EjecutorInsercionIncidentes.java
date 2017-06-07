package exit.services.principal.ejecutores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.excepciones.ExceptionEstadoInvalido;
import exit.services.excepciones.ExceptionIDNoNumerico;
import exit.services.excepciones.ExceptionIDNullIncidente;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.excepciones.ExceptionModoContactoInvalido;
import exit.services.excepciones.ExceptionTipoIncidenteInvalido;
import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.fileHandler.FilesAProcesarManager;
import exit.services.fileHandler.Tipo_Json;
import exit.services.json.IJsonRestEstructura;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestClienteEstructura;
import exit.services.json.JsonRestIncidentes;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.peticiones.InsertarIncidente;
import exit.services.principal.peticiones.UpdatearContacto;
import exit.services.util.Contador;

public class EjecutorInsercionIncidentes {
	public void insertar() throws InterruptedException{
   		CSVHandler csv = new CSVHandler();
	 	ArrayList<File> pathsCSV= FilesAProcesarManager.getInstance().getCSVAProcesar(ParserXMLWSConnector.getInstance().getPathCSVRegistros());
	 	for(File path:pathsCSV){
		 	ConvertidosJSONCSV jsonHandler = new ConvertidosJSONCSV();
			jsonHandler.convertirCSVaArrayListJSON(path,Tipo_Json.INCIDENTE);
        	ExecutorService workers = Executors.newFixedThreadPool(ParserXMLWSConnector.getInstance().getNivelParalelismo());      	
        	//System.out.println(ParserXMLWSConnector.getInstance().getNivelParalelismo());
    	    List<Callable<Void>> tasks = new ArrayList<>();
			for(IJsonRestEstructura jsonI: jsonHandler.getListaJson()){
				tasks.add(new Callable<Void>() {
    	        public Void call() throws IOException {
    	        	JsonRestIncidentes json=(JsonRestIncidentes)jsonI;
					boolean excepcion= false;
					InsertarIncidente insertar= new InsertarIncidente();
					JSONHandler jsonH=null;
					try{
							jsonH=json.createJson(TipoTarea.INSERTAR);
							System.out.println(jsonH);
					}
					catch(ExceptionEstadoInvalido e){
						excepcion=true;
						CSVHandler manejadorCSV = new CSVHandler();
						manejadorCSV.escribirCSVERRORLongitud("error_estado_invalido.csv", json.getLine());
					}
					catch(ExceptionTipoIncidenteInvalido e){
						excepcion=true;
						CSVHandler manejadorCSV = new CSVHandler();
						manejadorCSV.escribirCSVERRORLongitud("error_tipo_incidente_invalido.csv", json.getLine());
					}
					catch(ExceptionIDNullIncidente e){
						excepcion=true;
						CSVHandler manejadorCSV = new CSVHandler();
						manejadorCSV.escribirCSVERRORLongitud("error_id_incidente_null.csv", json.getLine());
					}
					catch(ExceptionModoContactoInvalido e){
						excepcion=true;
						CSVHandler manejadorCSV = new CSVHandler();
						manejadorCSV.escribirCSVERRORLongitud("error_modo_contacto_invalido.csv", json.getLine());
					}
					catch(ExceptionIDNoNumerico e){
						excepcion=true;
						CSVHandler manejadorCSV = new CSVHandler();
						manejadorCSV.escribirCSVERRORLongitud("error_id_no_numerico.csv", json.getLine());
					}					
					catch(Exception e){
						excepcion=true;
					}
					if(!excepcion){
						try{
							insertar.realizarPeticion(jsonH);
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
					else{
						CSVHandler manejadorCSV = new CSVHandler();
						manejadorCSV.escribirCSVERRORLongitud("error_generico.csv", json.getLine());
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

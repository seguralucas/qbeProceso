package exit.extras;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.excepciones.ExceptionEstadoInvalido;
import exit.services.excepciones.ExceptionIDNoNumerico;
import exit.services.excepciones.ExceptionIDNullIncidente;
import exit.services.excepciones.ExceptionModoContactoInvalido;
import exit.services.excepciones.ExceptionTipoIncidenteInvalido;
import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.fileHandler.FilesAProcesarManager;
import exit.services.json.JSONHandler;
import exit.services.json.JsonRestIncidentes;
import exit.services.json.TipoTarea;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.Principal;
import exit.services.principal.peticiones.FetchMapeos;
import exit.services.principal.peticiones.InsertarIncidente;
import exit.services.util.Contador;

public class Ejecutor {
	public static int y=0;
	public void insertar() throws InterruptedException, IOException{
		File f= new File("H:/Users/GAS/Desktop/borrar/repetidos.csv");
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DataInputStream dis = new DataInputStream(bis);
		ArrayList<String> list= new ArrayList<String>();
		while (dis.available() != 0) {
			list.add(dis.readLine());
		}


    	ExecutorService workers = Executors.newFixedThreadPool(ParserXMLWSConnector.getInstance().getNivelParalelismo());      	
		    List<Callable<Void>> tasks = new ArrayList<>();
			for(String v:list){
				tasks.add(new Callable<Void>() {
			        public Void call() {
			        	GetIdQuery.realizarPeticion(v);
			        	return null;
			        }
			});
		}
		    workers.invokeAll(tasks);
		    workers.shutdown();
	}
	}


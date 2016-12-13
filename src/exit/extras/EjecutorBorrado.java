package exit.extras;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.parser.ParserXMLWSConnector;

public class EjecutorBorrado {
	public static int y=0;
	public void insertar() throws InterruptedException, IOException{
		File f= new File("H:/Users/GAS/Desktop/borrar/procesado/correctos.csv");
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
			        	DeleteId.realizarPeticion(v);
			        	return null;
			        }
			});
		}
		    workers.invokeAll(tasks);
		    workers.shutdown();
	}
}

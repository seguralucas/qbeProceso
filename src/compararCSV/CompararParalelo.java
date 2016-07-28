package compararCSV;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.util.Contador;

public class CompararParalelo extends Thread{

	
	@Override
    public void run(){
		final HashMap<Integer, String> ficheroContenedor  = CompararCSV.getSecuencuas(new File("D:/Exit/QBE/EXCEL/vuelco7.csv"), 1);
		final HashMap<Integer, String> ficheroResta = CompararCSV.getSecuencuas(new File("D:/Exit/QBE/logs ejecucion/Ejecucion1_vuelco7_registros_TODOS/FICHERO_TEST.csv"), 2);
	 	final File fichero = new File("D:/Exit/QBE/logs ejecucion/idDiferencia.csv"); 
	 	try {
			HelpPW.out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int contador=0;
		//int i=0;
	    Iterator it = ficheroContenedor.entrySet().iterator();
	    Map.Entry<Integer, String> pair;
	    Map.Entry<Integer, String> pair2;
    	CountDownLatch latch = new CountDownLatch(10000);
    	ExecutorService exec = Executors.newFixedThreadPool(10000);
    	try {
    		
	    while (it.hasNext()) {
	    	boolean contiene=false;
	    	pair = (Map.Entry<Integer, String> )it.next();
		    Iterator it2 = ficheroResta.entrySet().iterator();
			while(it2.hasNext()){
				pair2 = (Map.Entry<Integer, String> )it2.next();
				if(pair.getKey().equals(pair2.getKey())){
					contiene= true;
					break;
				}
			}
			if(!contiene)
				System.out.println(pair.getValue());
			System.out.println(++Contador.x);
			HelpPW.out.close();
		}
    	}
		  catch(Exception e){
          	e.printStackTrace();
      	}
		
	}
}

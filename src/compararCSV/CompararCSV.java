package compararCSV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exit.services.parser.ParserXMLWSConnector;


public class CompararCSV {

	public static void main(String[] args) throws IOException{
		final HashMap<Integer, String> ficheroContenedor  = CompararCSV.getSecuencuas(new File("D:/Exit/QBE/logs ejecucion/vuelco1.csv"), 1);
		final HashMap<Integer, String> ficheroResta = CompararCSV.getSecuencuas(new File("D:/Exit/QBE/logs ejecucion/borrar.txt"), 1);
	 	final File fichero = new File("D:/Exit/QBE/logs ejecucion/idDiferencia.csv"); 
        final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
		int contador=0;
		int i=0;
	    Iterator it = ficheroContenedor.entrySet().iterator();
	    Map.Entry<Integer, String> pair;
	    Map.Entry<Integer, String> pair2;
	    System.out.println(ficheroContenedor.size()+"-"+ficheroResta.size()+"="+(ficheroContenedor.size()-ficheroResta.size()));
//	    if(true)
//    		return;

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
	        	out.println(pair.getValue());
			System.out.println(++i);
		}
		out.close();

    	

    
    	
	    	
	//        System.out.println(pair.getKey() + " = " + pair.getValue());
	 //       it.remove(); // avoids a ConcurrentModificationException
	    
		
		/*for(Integer s: ficheroContenedor){
			boolean contiene=false;
			for(Integer s2:ficheroResta){
				if(s2.equals(s)){
					contiene= true;
					break;
				}
			}
			if(!contiene)
	        	out.println(s);
			System.out.println(++i);
		}
		out.close();*/
	}
	
public static HashMap<Integer, String> getSecuencuas(File fileCSV, int columna){
		try(BufferedReader br = new BufferedReader(
		         new InputStreamReader(
		                 new FileInputStream(fileCSV), "UTF-8"))){
		boolean esPrimeraVez=true;
		String[] cabeceras=null;
		String line;
		HashMap<Integer, String> hm = new HashMap<Integer,String>();
		int i=0;
		while ((line = br.readLine()) != null) {
			if(esPrimeraVez){
//				cabeceras = line.split(ParserXMLWSConnector.getInstance().getSeparadorCSV());
				esPrimeraVez=false;
			}
			else{
				i++;

	    		String[] valoresCsv= line.replace("\"", "'").split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
   					hm.put(Integer.parseInt(valoresCsv[columna]), line);   				
   				}
			}
		System.out.println(i);
		return hm;
		}
   catch(IOException e){
   	e.printStackTrace();
   	return null;
   }	   
}
}





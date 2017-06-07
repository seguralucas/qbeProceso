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

import exit.services.parser.ParserXMLWSConnector;

public class CompararSiMismo {
	public static void main(String[] args) throws IOException{
		File fileCSV = new File("D:/Exit/QBE/logs ejecucion/REGISTRO/FICHERO_TEST.csv");
	 	File fichero = new File("D:/Exit/QBE/logs ejecucion/REGISTRO/idSecRepetidos.csv"); 
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
		try(BufferedReader br = new BufferedReader(
		         new InputStreamReader(
		                 new FileInputStream(fileCSV), "UTF-8"))){
		boolean esPrimeraVez=true;
		String[] cabeceras=null;
		String line;
		ArrayList<Integer> a = new ArrayList<Integer>();
		int i=0;
		while ((line = br.readLine()) != null) {
			if(esPrimeraVez){
//				cabeceras = line.split(ParserXMLWSConnector.getInstance().getSeparadorCSV());
				esPrimeraVez=false;
			}
			else{
	    			String[] valoresCsv= line.replace("\"", "'").split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
	    			if(!a.contains(Integer.parseInt(valoresCsv[2])))
	    				a.add(Integer.parseInt(valoresCsv[2]));
	    			else
	    	        	out.println(valoresCsv[2]);
	    				
  				}
			System.out.println(i++);
			}
			out.close();
		}
  catch(IOException e){
  	e.printStackTrace();
  	}	   
}
	
}

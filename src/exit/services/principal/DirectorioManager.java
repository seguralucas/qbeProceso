package exit.services.principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import exit.services.fileHandler.CSVHandler;
import exit.services.parser.ParserXMLWSConnector;

public class DirectorioManager {
	
	private static final String NOMBRE_TEMP="temp";
	public static void SepararFicheros(File archivo) throws IOException{
    				CSVHandler csv= new CSVHandler();
		    		String line="";
			    		try(BufferedReader br = new BufferedReader(
			    		         new InputStreamReader(
			    		                 new FileInputStream(archivo), "UTF-8"))){
			    		boolean esPrimeraVez=true;
			    		int i=0;
			    		while ((line = br.readLine()) != null) {
			    			if(esPrimeraVez){
			    				esPrimeraVez=false;
			    				CSVHandler.cabecera=line;			    									    		
			    			}
			    			else{
				    			csv.escribirCSV(DirectorioManager.getDirectorioFechaYHoraInicioDivision(NOMBRE_TEMP+i+".csv"),line);
			    				if(i>=ParserXMLWSConnector.getInstance().getNivelParalelismo()-1)
			    					i=0;
			    				else
			    					i++;
			    			}
			    		}
		    		}
			        catch(Exception e){
			        	FileWriter fw = new FileWriter(getDirectorioFechaYHoraInicio("errorLote.txt"));
			        	fw.write(e.getMessage());
			        	fw.close();
			        	throw e;
			        }
	}
	
	public static File getDirectorioFechaYHoraInicio(String nombreFichero) throws IOException{
		File file = new File(AlmacenadorFechaYHora.getFechaYHoraInicio());
		if(!file.exists())
			Files.createDirectories(Paths.get(AlmacenadorFechaYHora.getFechaYHoraInicio()));
		return new File(AlmacenadorFechaYHora.getFechaYHoraInicio()+"/"+nombreFichero);
	}
	public static String getPathFechaYHoraInicioDivision() throws IOException{
		File file = new File(AlmacenadorFechaYHora.getFechaYHoraInicio());
		if(!file.exists())
			Files.createDirectories(Paths.get(AlmacenadorFechaYHora.getFechaYHoraInicio()));
		file = new File(AlmacenadorFechaYHora.getFechaYHoraInicio()+"/division");
		if(!file.exists())
			Files.createDirectories(Paths.get(AlmacenadorFechaYHora.getFechaYHoraInicio()+"/division"));
		return AlmacenadorFechaYHora.getFechaYHoraInicio()+"/division";
	}
	private static File getDirectorioFechaYHoraInicioDivision(String nombreFichero) throws IOException{
		return new File(getPathFechaYHoraInicioDivision()+"/"+nombreFichero);
	}
}

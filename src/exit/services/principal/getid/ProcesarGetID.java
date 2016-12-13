package exit.services.principal.getid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.json.EstructuraGetIdRightNow;
import exit.services.parser.ParserXMLWSConnector;


public class ProcesarGetID {
	private CSVHandler csv;
	private BufferedReader br=null;
	private boolean fin=false;
	private boolean esPrimeraVez=true;
	private String[] cabeceras;
	public ProcesarGetID(){
		csv = new CSVHandler();
	}
	public boolean existFile() throws IOException{
		File f = new File(PropiedadesGetId.getInstance().getPath());
		return (f.exists() && !f.isDirectory());
	}
	
	public EstructuraGetIdRightNow procesarFichero() throws IOException{
			EstructuraGetIdRightNow getIdRightNow = new EstructuraGetIdRightNow();
			File fileCSV= PropiedadesGetId.getInstance().getF();
			String line;
			try{
			if(br==null)
				this.br = new BufferedReader(
		  		         new InputStreamReader(
		  		                 new FileInputStream(fileCSV), "ISO-8859-1"));
			
	  		while ((line = br.readLine()) != null) {
	  			if(esPrimeraVez){
	  				cabeceras = line./*substring(1).*/split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
	  				esPrimeraVez=false;
	  				String firstChar=String.valueOf(line.charAt(0));
	  				if(!firstChar.matches("[a-zA-Z]"))
	  					line=line.substring(1);//Ocasionalmente el primer caracter erra un signo raro y hay que eliminarlo.
	
	  				CSVHandler.cabecera=line/*.substring(1)*/;/*Esto es sólo en caso de que estemos haciendo update*/
	  			}
	  			else{
	  	    		String[] valoresCsv= line.replace("\"", "'").split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
	  	    		try{
		  	    		if(ColumnasMayorCabecera(valoresCsv))
	  						throw new Exception();
				  		for(int i=0;i<cabeceras.length;i++){
				  			switch(cabeceras[i]){
								case "ID": getIdRightNow.setId(valoresCsv[i]); break;				
				   				case "NRO_SAC": getIdRightNow.setNroSac(valoresCsv[i]); break;				
				   				case "MODO_CONTACTO": getIdRightNow.setModoContacto(valoresCsv[i]); break;				
				   				case "CAUSA": getIdRightNow.setCausa(valoresCsv[i]); break;				
				   				case "PRODUCTO": getIdRightNow.setProducto(valoresCsv[i]); break;				
				   				case "MOTIVO": getIdRightNow.setMotivo(valoresCsv[i]); break;				
				   				case "ESTADO": getIdRightNow.setEstado(valoresCsv[i]); break;				
				   				case "SECTOR_RESPONSABLE": getIdRightNow.setSector_responsable(valoresCsv[i]); break;				
				   				case "HILO1": getIdRightNow.setHilo1(valoresCsv[i]); break;				
				   				case "HILO2": getIdRightNow.setHilo2(valoresCsv[i]); break;		
				   				case "ID_DE_AIS": getIdRightNow.setIdDeAIS(valoresCsv[i]); break;
				  			}
				  			getIdRightNow.setLine(line);
				  		}
		  	    		return getIdRightNow;
	  	    		}
	  				catch(Exception e){
	  					csv.escribirCSV("error_parser.csv", line);
	  					return null;
	  				}
	  			}
	  		}
	  		br.close();
	  		this.fin=true;
		}
			catch(Exception e){
			return null;
			}
			return null;
	}
	
	   private boolean ColumnasMayorCabecera(String[] valoresCsv){
		   return CSVHandler.cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX()).length<valoresCsv.length;
	   }
	public boolean isFin() {
		return fin;
	}

	
}

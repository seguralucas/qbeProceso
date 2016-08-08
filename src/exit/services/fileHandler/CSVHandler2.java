package exit.services.fileHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.csvreader.CsvWriter;

import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;

public class CSVHandler2 {
	public static String[] cabeceras;
	public ArrayList<File> getAllCSV(String path){
	ArrayList<File> paths = new ArrayList<File>();
	File folder = new File(path);
	File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	    	String extension = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".") + 1, listOfFiles[i].getName().length());

	        if(extension.equalsIgnoreCase("csv"))
	        	paths.add(listOfFiles[i]);
	      } 
	    }
	    return paths;
	}


	
	private void crearCabecer(CsvWriter csvOutput, boolean id_rightNow) throws IOException{
		csvOutput.write("ID_RIGHTNOW");
        csvOutput.write("ID_CLIENTE");
        csvOutput.write("CLIENSEC");
        csvOutput.write("TIPOORGANIZACION");
        csvOutput.write("APELLIDO");
        csvOutput.write("NOMBRE");
        csvOutput.write("ID_TIPO_DOCUMENTO");
        csvOutput.write("TIPO_DOCUMENTO");
        csvOutput.write("NRO_DOCUMENTO");
        csvOutput.write("SEXO");
        csvOutput.write("FECHANACIMIENTO");
        csvOutput.write("DOMICILIO");
        csvOutput.write("NUMERO");
        csvOutput.write("PISO");
        csvOutput.write("PUERTA");
        csvOutput.write("LOCALIDAD");
        csvOutput.write("CP");
        csvOutput.write("PROVINCIA");
        csvOutput.write("SEGMENTACION");
        csvOutput.write("CONFLICTIVO");
        csvOutput.write("EMAIL_PERSONAL");
        csvOutput.write("EMAIL_LABORAL");
        csvOutput.write("EMAIL_ALTERNATIVO");
        csvOutput.write("REGISTRO");
        csvOutput.write("USER_NAME");
        csvOutput.write("FECHA_ALTA");
        csvOutput.endRecord();
	}
	
	 public void escribirCSVERRORLongitud(String path,String line) throws IOException{
		 	CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
            File aux = DirectorioManager.getDirectorioFechaYHoraInicio(path);
            if(!aux.exists() || aux.length() == 0){
            	for(int i=0;i<cabeceras.length;i++)
            		csvOutput.write(cabeceras[i]);
            	csvOutput.endRecord();		 
            }
            else
            	csvOutput.endRecord();		 
            String[] campos= line.split(ParserXMLWSConnector.getInstance().getSeparadorCSV());
            for(String c:campos){
                csvOutput.write(c);        
            }
            csvOutput.close();
		 
	 }
	 public void escribirCSV(String path,JSONHandler json, String id) throws IOException{
			 	CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
	            File aux = new File(path);
	            if(!aux.exists() || aux.length() == 0){
	            	crearCabecer(csvOutput,true);
	            }
	            else
	            	csvOutput.endRecord();
                JSONHandler qbe = (JSONHandler)((JSONHandler)json.get("customFields")).get("Qbe");
                JSONHandler contactType = ((JSONHandler)json.get("contactType"));
                JSONHandler name = ((JSONHandler)json.get("name"));
                csvOutput.write(id);
                csvOutput.write(insertarNoNull((String)qbe.get("IdBI")));        
                csvOutput.write(insertarNoNull((String)qbe.get("idAIS")));        
	            csvOutput.write(insertarNoNull((String)contactType.get("lookupName")));
	            csvOutput.write(insertarNoNull((String)name.get("last")));
	            csvOutput.write(insertarNoNull((String)name.get("first")));
	            csvOutput.write(insertarNoNull((String)qbe.get("IdTipoDocumento")));
	            csvOutput.write(insertarNoNull((String)qbe.get("TipoDocumento")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Dni")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Genero")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Nacimiento")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Calle"))); 
	            csvOutput.write(insertarNoNull((String)qbe.get("Domicilio")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Piso")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Departamento")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Localidad")));
	            csvOutput.write(insertarNoNull((String)qbe.get("CodigoPostal")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Provincia")));
	            csvOutput.write(insertarNoNull((String)qbe.get("Segmentacion")));
	            csvOutput.write(insertarConflictivo(qbe.get("Conflictivo")));
	            csvOutput.write(""); /*EMAIL_PERSONAL*/
	            csvOutput.write("");  /*EMAIL_LABORAL*/
	            csvOutput.write(""); /*EMAIL_ALTERNATIVO*/
	            csvOutput.write("");/*REGISTRO NO TIENE COLUMNA EN EL JSON???*/
	            csvOutput.write(insertarNoNull((String)json.get("login")));
	            csvOutput.write(insertarNoNull((String)qbe.get("FechaAlta")));
	             
	            csvOutput.close();
	         
	      
	 }
	 
	 private String insertarNoNull(String cadena){
		 if(cadena!=null)
			 return cadena;
		 return "";
	 }
	 
	 public String insertarConflictivo(Object conflictivo){
		 try{
			 if((Boolean)conflictivo)
				 return "SI";
			 else
				 return "NO";
		 }
		 catch(Exception e){
			 return null;
		 }
	 }
	
	
}

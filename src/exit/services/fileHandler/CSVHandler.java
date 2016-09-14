package exit.services.fileHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;

import com.csvreader.CsvWriter;

import exit.services.json.JSONHandler;
import exit.services.json.JsonRestIncidentes;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;

public class CSVHandler {
	
	public static String cabecera;

	
		private void crearCabecer(CsvWriter csvOutput) throws IOException{
			String[] campos= cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSV());
			for(int i=0;i<campos.length;i++)
				csvOutput.write(campos[i]);
        	csvOutput.endRecord();

		}
		
		 public void escribirCSV(File file,String line) throws IOException{
			 	CsvWriter csvOutput = new CsvWriter(new FileWriter(file, true), ';');
	            if(!file.exists() || file.length() == 0){
	            	crearCabecer(csvOutput);
	    	    }
	            else
	            	csvOutput.endRecord();		 
	            String[] campos= line.split(ParserXMLWSConnector.getInstance().getSeparadorCSV());
	            for(String c:campos){
	                csvOutput.write(c);        
	            }
	            csvOutput.close();
		 }
		
		 public void escribirCSV(String path,String line) throws IOException{
			 	escribirCSV(DirectorioManager.getDirectorioFechaYHoraInicio(path),line);
			 
		 }
		 public void escribirCSV(String path,JSONHandler json) throws IOException{
				 	CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
		            File aux = DirectorioManager.getDirectorioFechaYHoraInicio(path);
		            if(!aux.exists() || aux.length() == 0){
		            	crearCabecer(csvOutput);
		            }
					String[] campos= json.getLine().split(ParserXMLWSConnector.getInstance().getSeparadorCSV());
					for(int i=0;i<campos.length;i++)
						csvOutput.write(campos[i]);
	            	csvOutput.endRecord();

		            csvOutput.close();	      
		 }
		 
	 public void escribirCSVInsercionContacto(String path,JSONHandler json, String id) throws IOException{
	 	CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
        File aux = DirectorioManager.getDirectorioFechaYHoraInicio(path);
        if(!aux.exists() || aux.length() == 0){
        	crearCabecerInsercionContacto(csvOutput);
        }
         JSONHandler qbe = (JSONHandler)((JSONHandler)json.get("customFields")).get("Qbe");
         JSONHandler contactType = ((JSONHandler)json.get("contactType"));
         JSONHandler name = ((JSONHandler)json.get("name"));
         JSONArray phones = ((JSONArray)json.get("phones"));
         JSONArray emails = ((JSONArray)json.get("emails"));       
         csvOutput.write(id);
         csvOutput.write(insertarNoNull((String)qbe.get("IdBI")));        
         csvOutput.write(insertarNoNull((String)qbe.get("idAIS")));  
         if(contactType!=null)
        	 csvOutput.write(insertarNoNull((String)contactType.get("lookupName")));
         else
        	 insertarCampoVacio(csvOutput);
         if(name!=null){
	         csvOutput.write(insertarNoNull((String)name.get("last")));
	         csvOutput.write(insertarNoNull((String)name.get("first")));
         }
         else{
        	 insertarCampoVacio(csvOutput);
        	 insertarCampoVacio(csvOutput);
         }
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
         insertarEmails(emails,csvOutput);
         csvOutput.write("");/*REGISTRO NO TIENE COLUMNA EN EL JSON???*/
         csvOutput.write(insertarNoNull((String)json.get("login")));
         csvOutput.write(insertarNoNull((String)qbe.get("FechaAlta")));
         insertarTelefonos(phones,csvOutput);
         csvOutput.endRecord();
         csvOutput.close();	      
	 }
	 
	 public void escribirCSVInsercionIncidentes(String path,JSONHandler json, String id, String lookupName) throws IOException{
		 	CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
	        File aux = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	        if(!aux.exists() || aux.length() == 0)
	        	crearCabecerInsercionIncidente(csvOutput);
	         JsonRestIncidentes jsonRestIncidentes= json.getJsonRestIncidentes();
	         JSONHandler qbe = (JSONHandler)((JSONHandler)json.get("customFields")).get("Qbe");
	         csvOutput.write(id);
	         csvOutput.write(lookupName);
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getId()));        
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getNro_sac()));  
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getModo_contacto()));
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getCausa()));
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getProducto()));
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getMotivo()));
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getEstado()));
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getSector_responsable()));
	         csvOutput.write(insertarNoNull(jsonRestIncidentes.getHilo_conversacion_antiguo()));
	         csvOutput.endRecord();
	         csvOutput.close();	      
		 }
		 private void insertarEmails(JSONArray emails, CsvWriter csvOutput) throws IOException{
	    	 String email1=null;
	    	 String email2=null;
	    	 String email3=null;
	         if(emails!=null && emails.size()>0){
	        	 for(int i=0;i<emails.size();i++){
	        	 JSONHandler jsonEmail=(JSONHandler)emails.get(i);
	        	 if(jsonEmail!=null){
	        		  String casillaDeCorre= (String)jsonEmail.get("address");
	        		  JSONHandler emailType= (JSONHandler)jsonEmail.get("addressType");
	        		  if(emailType!=null){
	        			  String lookupNameMail=(String)emailType.get("lookupName");
	        			  if(lookupNameMail.equalsIgnoreCase("Correo electrónico - Principal"))
	        				  email1=casillaDeCorre;
	        			  else if(lookupNameMail.equalsIgnoreCase("Correo electrónico alternativo 1"))
	        				  email2=casillaDeCorre;
	        			  else if(lookupNameMail.equalsIgnoreCase("Correo electrónico alternativo 2"))
	        				  email3=casillaDeCorre;
	        		  	}
	        		  }
	        	 }
	         }
	         csvOutput.write(insertarNoNull(email1)); /*EMAIL_PERSONAL*/
	         csvOutput.write(insertarNoNull(email2));  /*EMAIL_LABORAL*/
	         csvOutput.write(insertarNoNull(email3)); /*EMAIL_ALTERNATIVO*/
	         
		 }
		 private void insertarTelefonos(JSONArray phones, CsvWriter csvOutput) throws IOException{
	    	 String numeroParticular=null;
	    	 String numeroOficina=null;
	         if(phones!=null && phones.size()>0){
	        	 JSONHandler jsonPhone=(JSONHandler)phones.get(0);
	        	 if(jsonPhone!=null){
	        		  String numero= (String)jsonPhone.get("number");
	        		  JSONHandler phoneType= (JSONHandler)jsonPhone.get("phoneType");
	        		  if(phoneType!=null){
	        			  String lookupNameTel=(String)phoneType.get("lookupName");
	        			  if(lookupNameTel.equalsIgnoreCase("Teléfono de oficina"))
	        				  numeroOficina=numero;
	        			  else if(lookupNameTel.equalsIgnoreCase("Teléfono particular"))
	        				  numeroParticular=numero;
	        		  }
	        	 }
	         }
	        	 if(phones!=null && phones.size()>1){
	        	 JSONHandler jsonPhone=(JSONHandler)phones.get(1);
	        	 String numero;
	        	 if(jsonPhone!=null){
	        		  numero= (String)jsonPhone.get("number");
	        		  JSONHandler phoneType= (JSONHandler)jsonPhone.get("phoneType");
	        		  if(phoneType!=null){
	        			  String lookupNameTel=(String)phoneType.get("lookupName");
	        			  if(lookupNameTel.equalsIgnoreCase("Teléfono de oficina"))
	        				  numeroOficina=numero;
	         			  else if(lookupNameTel.equalsIgnoreCase("Teléfono particular"))
	         				 numeroParticular=numero;
	        		  }
	        	 	}
	        	 }
	 	        csvOutput.write(insertarNoNull(numeroParticular));
		        csvOutput.write(insertarNoNull(numeroOficina));

	         
		 }
		 
		 private void insertarCampoVacio(CsvWriter csvOutput) throws IOException{
        	 csvOutput.write(insertarNoNull(""));
		 }
		 
			private void crearCabecerInsercionContacto(CsvWriter csvOutput) throws IOException{
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
		        csvOutput.write("TEL_PARTICULAR");
		        csvOutput.write("TEL_LABORAL");
		        csvOutput.endRecord();
			}
			
			private void crearCabecerInsercionIncidente(CsvWriter csvOutput) throws IOException{
				String [] cabeceras= {"ID_RIGHTNOW","LOOKUPNAME","ID","NRO_SAC","MODO_CONTACTO","CAUSA","PRODUCTO","MOTIVO","ESTADO","SECTOR_RESPONSABLE","HILO_CONVERSACION"};
				for(int i=0;i<cabeceras.length;i++)
					csvOutput.write(cabeceras[i]);
		        csvOutput.endRecord();
			}
			
			 public void escribirCSVERRORLongitud(String path,String line) throws IOException{
				 	CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
		            File aux = DirectorioManager.getDirectorioFechaYHoraInicio(path);
		            if(!aux.exists() || aux.length() == 0){
		            	String[] cabeceraArray = cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSV());
		            	for(int i=0;i<cabeceraArray.length;i++)
		            		csvOutput.write(cabeceraArray[i]);
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

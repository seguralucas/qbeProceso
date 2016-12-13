package exit.services.fileHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import exit.services.json.IJsonRestEstructura;
import exit.services.json.JsonRestClienteEstructura;
import exit.services.json.JsonRestIncidenteDelete;
import exit.services.json.JsonRestIncidentes;
import exit.services.parser.ParserXMLWSConnector;


public class ConvertidosJSONCSV {
    	private String line = "";
    	//private String cvsSplitBy = ParserXMLWSConnector.getInstance().getSeparadorCSV();
    	ArrayList<IJsonRestEstructura> listaJson;
    	private CSVHandler csv;
    	private String pathError;
		private BufferedReader br;
    	private boolean esPrimeraVez;
    	private boolean fin;
		public ConvertidosJSONCSV(){
            csv = new CSVHandler();
            this.pathError=ParserXMLWSConnector.getInstance().getFicheroCSVERROREJECUCION();
            this.esPrimeraVez=true;
            this.fin=false;
            br=null;
            listaJson= new ArrayList<IJsonRestEstructura>();
		}
		
		
	   public JsonRestClienteEstructura convertirCSVaArrayListJSONLineaALineaClientes(File fileCSV) {
		   try{
			   if(br==null)
			   br = new BufferedReader(
		  		         new InputStreamReader(
		  		                 new FileInputStream(fileCSV), "UTF-8"));
  		String[] cabeceras=null;
  		while ((line = br.readLine()) != null) {
  			if(this.esPrimeraVez){
  				cabeceras = line.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
  				this.esPrimeraVez=false;
  				CSVHandler.cabecera=line;//Esto es sólo en caso de que estemos haciendo update
  			}
  			else{
  	    		String[] valoresCsv= line.replace("\"", "'").split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
				try{
  					if(ColumnasMayorCabecera(valoresCsv))
  						throw new Exception();
  	    		JsonRestClienteEstructura jsonEstructura=creaJsonContactos(valoresCsv,CSVHandler.cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX()));
  	    		
  
  	
  	    		if(jsonEstructura.getCliensec()==null || !jsonEstructura.getCliensec().matches("[0-9]+")){
  					csv.escribirCSV(pathError.replace(".csv", "_registro_error_clientsec.csv"), line);
  					return null;
  	    		}
    			return jsonEstructura;
  				}
  				catch(Exception e){
  					e.printStackTrace();
  					csv.escribirCSV(pathError.replace(".csv", "_error_parser.csv"), line);
  					return null;
  				}
  			}
  		}
  		br.close();
  		this.fin=true;
      }
      catch(IOException e){
      	e.printStackTrace();
  		this.fin=true;
			return null;
      }	   
 			return null;
	   }
	   
	   public JsonRestIncidentes convertirCSVaArrayListJSONLineaALineaIncidentes(File fileCSV) {
		   try{
			   if(br==null)
			   br = new BufferedReader(
		  		         new InputStreamReader(
		  		                 new FileInputStream(fileCSV)));
  		String[] cabeceras=null;
  		while ((line = br.readLine()) != null) {
  			if(this.esPrimeraVez){
  				String firstChar=String.valueOf(line.charAt(0));
  				if(!firstChar.matches("[a-zA-Z]"))
  					line=line.substring(1);//Ocasionalmente el primer caracter erra un signo raro y hay que eliminarlo.
  				cabeceras = line.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
  				this.esPrimeraVez=false;
  				CSVHandler.cabecera=line;//Esto es sólo en caso de que estemos haciendo update
  			}
  			else{
  	    		String[] valoresCsv= line.replace("\"", "'").split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX());
				try{
  					if(ColumnasMayorCabecera(valoresCsv))
  						throw new Exception();
  	    		JsonRestIncidentes jsonEstructura=crearJsonIncidente(valoresCsv,CSVHandler.cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX()));  	
    			return jsonEstructura;
  				}
  				catch(Exception e){
  					//e.printStackTrace();
  					csv.escribirCSV(pathError.replace(".csv", "_error_parser.csv"), line);
  					return null;
  				}
  			}
  		}
  		br.close();
  		this.fin=true;
      }
      catch(IOException e){
      	e.printStackTrace();
  		this.fin=true;
			return null;
      }	   
 			return null;
	   }
			   
	   public void convertirCSVaArrayListJSON(File fileCSV,Tipo_Json tipo_json) {

		   try(BufferedReader br = new BufferedReader(
	  		         new InputStreamReader(
	  		                 new FileInputStream(fileCSV), "ISO-8859-1"))){
	  		boolean esPrimeraVez=true;
	  		String[] cabeceras=null;
	  		while ((line = br.readLine()) != null) {
	  		    String output = new String(line.getBytes("ISO-8859-1"), "UTF-8");
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
		  	    		if(tipo_json==Tipo_Json.CLIENTE){
		  	    			JsonRestClienteEstructura jsonEstructura=creaJsonContactos(valoresCsv,CSVHandler.cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX()));
			  	    		if(jsonEstructura.getCliensec()==null || !jsonEstructura.getCliensec().matches("[0-9]+"))
			  					csv.escribirCSV(pathError.replace(".csv", "_registro_error_clientsec.csv"), line);
			  	    		else
			  	    			this.listaJson.add(jsonEstructura);
		  				}
		  	    		else if(tipo_json==Tipo_Json.INCIDENTE){
		  	    			JsonRestIncidentes jsonEstructura=crearJsonIncidente(valoresCsv,CSVHandler.cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX()));
		  	    			this.listaJson.add(jsonEstructura);
		  	    		}
		  	    		else if(tipo_json==Tipo_Json.DELETEINCIDENTE){
		  	    			JsonRestIncidenteDelete jsonEstructura=crearJsonIncidenteDelete(valoresCsv,CSVHandler.cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX()));
		  	    			this.listaJson.add(jsonEstructura);
		  	    		}
	  				}
	  				catch(Exception e){
	  					csv.escribirCSV(pathError.replace(".csv", "_error_parser.csv"), line);
	  				}
	  			}
	  		}
	      }
	      catch(IOException e){
	      	e.printStackTrace();
	      }	  
		   	   
	    }
	   
	   private JsonRestClienteEstructura creaJsonContactos(String[] valoresCsv, String[] cabeceras){
	    		JsonRestClienteEstructura jsonEstructura= new JsonRestClienteEstructura(pathError);
	    		jsonEstructura.setLine(line);
	    		int i;
	    		for(i=0;i<valoresCsv.length;i++){
	    			switch (cabeceras[i]) {
		   				case "ID_CLIENTE": jsonEstructura.setId_cliente(valoresCsv[i]); break;
		   				case "CLIENSEC":  jsonEstructura.setCliensec(valoresCsv[i]); break;
		   				case "TIPOORGANIZACION": jsonEstructura.setTipoorganizacion(valoresCsv[i]);  break;
		   				case "APELLIDO": jsonEstructura.setApellido(valoresCsv[i]); break;
		   				case "NOMBRE": jsonEstructura.setNombre(valoresCsv[i]); break;
		   				case "ID_TIPO_DOCUMENTO": jsonEstructura.setId_tipo_documento(valoresCsv[i]); break;
		   				case "TIPO_DOCUMENTO": jsonEstructura.setTipo_documento(valoresCsv[i]); break;
		   				case "NRO_DOCUMENTO": jsonEstructura.setNro_documento(valoresCsv[i]); break;
		   				case "SEXO": jsonEstructura.setSexo(valoresCsv[i]); break;
		   				case "FECHANACIMIENTO": jsonEstructura.setFechanacimiento(valoresCsv[i]); break;
		   				case "DOMICILIO": jsonEstructura.setDomicilio(valoresCsv[i]); break;
		   				case "NUMERO": jsonEstructura.setNumero(valoresCsv[i]); break;
		   				case "PISO": jsonEstructura.setPiso(valoresCsv[i]); break;
		   				case "PUERTA": jsonEstructura.setPuerta(insertarNoVacio(valoresCsv[i])); break;
		   				case "LOCALIDAD": jsonEstructura.setLocalidad(valoresCsv[i]); break;
		   				case "CP": jsonEstructura.setCp(valoresCsv[i]); break;
		   				case "PROVINCIA": jsonEstructura.setProvincia(valoresCsv[i]); break;
		   				case "SEGMENTACION": jsonEstructura.setSegmentacion(valoresCsv[i]); break;
		   				case "CONFLICTIVO": jsonEstructura.setConflictivo(valoresCsv[i]); break;
		   				case "EMAIL_PERSONAL": jsonEstructura.setEmail_personal(valoresCsv[i]); break;
		   				case "EMAIL_LABORAL": jsonEstructura.setEmail_laboral(valoresCsv[i]); break;
		   				case "EMAIL_ALTERNATIVO": jsonEstructura.setEmail_alternativo(valoresCsv[i]); break;
		   				case "REGISTRO": jsonEstructura.setRegistro(valoresCsv[i]); break;
		   				case "USER_NAME": jsonEstructura.setUser_name(valoresCsv[i]); break;
		   				case "FECHA_ALTA": jsonEstructura.setFecha_alta(valoresCsv[i]); break;	
		   				case "TEL_PARTICULAR": jsonEstructura.setTel_personal(valoresCsv[i]); break;	
		   				case "TEL_LABORAL": jsonEstructura.setTel_oficina(valoresCsv[i]); break;	
   				
   				}
 			}
	    		return jsonEstructura;
	   }
	   
	   private JsonRestIncidentes crearJsonIncidente(String[] valoresCsv, String[] cabeceras){
		   JsonRestIncidentes jsonEstructura= new JsonRestIncidentes(pathError);
	   		jsonEstructura.setLine(line);
	   		int i;
	   		StringBuilder sb= new StringBuilder();
	   		for(i=0;i<valoresCsv.length;i++){
	   			switch (cabeceras[i]) {   										
					case "ID": jsonEstructura.setId(valoresCsv[i]); break;				
	   				case "NRO_SAC": jsonEstructura.setNro_sac(valoresCsv[i]); break;				
	   				case "MODO_CONTACTO": jsonEstructura.setModo_contacto(valoresCsv[i]); break;				
	   				case "CAUSA": jsonEstructura.setCausa(valoresCsv[i]); break;				
	   				case "PRODUCTO": jsonEstructura.setProducto(valoresCsv[i]); break;				
	   				case "MOTIVO": jsonEstructura.setMotivo(valoresCsv[i]); break;				
	   				case "ESTADO": jsonEstructura.setEstado(valoresCsv[i]); break;				
	   				case "SECTOR_RESPONSABLE": jsonEstructura.setSector_responsable(valoresCsv[i]); break;				
	   				case "HILO1": jsonEstructura.setHilo1(valoresCsv[i]); sb.append(valoresCsv[i]); break;				
	   				case "HILO2": jsonEstructura.setHilo2(valoresCsv[i]); sb.append(valoresCsv[i]); break;				
	   				case "ANIO": jsonEstructura.setAnio(valoresCsv[i]); break;				
				}
			}
				jsonEstructura.setHilo_conversacion(sb.toString());

	   		return jsonEstructura;
  }
	   
	   private JsonRestIncidenteDelete crearJsonIncidenteDelete(String[] valoresCsv, String[] cabeceras){
		   JsonRestIncidenteDelete jsonEstructura= new JsonRestIncidenteDelete(pathError);
	   		jsonEstructura.setLine(line);
	   		int i;
	   		for(i=0;i<valoresCsv.length;i++){
	   			switch (cabeceras[i]) {   										
					case "ID_RIGHTNOW": jsonEstructura.setId_rightnow(valoresCsv[i]); break;				
					}
			}
	   		return jsonEstructura;
  }
	   
	   private String insertarNoVacio(String valor){
		   if(valor==null || valor.length()==0)
			   return " ";
		   return valor;
	   }
	   
	   private boolean ColumnasMayorCabecera(String[] valoresCsv){
		   return CSVHandler.cabecera.split(ParserXMLWSConnector.getInstance().getSeparadorCSVREGEX()).length<valoresCsv.length;
	   }
	   
	   
	   
	   
	   public boolean isFin() {
		return fin;
	}


	public ArrayList<IJsonRestEstructura> getListaJson() {
		return listaJson;
	}

}

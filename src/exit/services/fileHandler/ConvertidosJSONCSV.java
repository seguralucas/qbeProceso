package exit.services.fileHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;

import exit.services.json.JSONHandler;
import exit.services.json.JsonRestEstructura;
import exit.services.parser.ParserXMLWSConnector;


public class ConvertidosJSONCSV {
    	private String line = "";
    	private String cvsSplitBy = ParserXMLWSConnector.getInstance().getSeparadorCSV();
    	ArrayList<JsonRestEstructura> listaJson;
    	private CSVHandlerUpdate csv;
    	private String pathError;
		private BufferedReader br;
    	private boolean esPrimeraVez;
    	private boolean fin;
		public ConvertidosJSONCSV(){
            csv = new CSVHandlerUpdate();
            this.pathError=ParserXMLWSConnector.getInstance().getFicheroCSVERROREJECUCION();
            this.esPrimeraVez=true;
            this.fin=false;
            br=null;
		}
		
		
	   public JsonRestEstructura convertirCSVaArrayListJSONLineaALinea(File fileCSV) {
		   try{
			   if(br==null)
			   br = new BufferedReader(
		  		         new InputStreamReader(
		  		                 new FileInputStream(fileCSV), "UTF-8"));
  		String[] cabeceras=null;
  		while ((line = br.readLine()) != null) {
  			if(this.esPrimeraVez){
  				cabeceras = line./*substring(1).*/split(cvsSplitBy);
  				this.esPrimeraVez=false;
  				CSVHandlerUpdate.cabecera=line/*.substring(1)*/;/*Esto es s�lo en caso de que estemos haciendo update*/
  			}
  			else{
  	    		String[] valoresCsv= line.replace("\"", "'").split(cvsSplitBy);
				try{
  					if(ColumnasMayorCabecera(valoresCsv))
  						throw new Exception();
  	    		JsonRestEstructura jsonEstructura=creaJson(valoresCsv,CSVHandlerUpdate.cabecera.split(cvsSplitBy));
  	    		
  
  	
  	    		if(jsonEstructura.getCliensec()==null || !jsonEstructura.getCliensec().matches("[0-9]+")){
  					csv.escribirCSV(pathError.replace(".csv", "_registro_error_clientsec.csv"), line);
  					return null;
  	    		}
    			return jsonEstructura;
  				}
  				catch(Exception e){
  					//e.printStackTrace();
  					csv.escribirCSV(pathError.replace(".csv", "_error_parser.csv"), line);
  					return null;
  				}
  			}
  		}
  		System.out.println("Sali�");
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
			   
	   public void convertirCSVaArrayListJSON(File fileCSV) {

		   try(BufferedReader br = new BufferedReader(
	  		         new InputStreamReader(
	  		                 new FileInputStream(fileCSV), "UTF-8"))){
	  		boolean esPrimeraVez=true;
	  		String[] cabeceras=null;
	  		while ((line = br.readLine()) != null) {
	  			if(esPrimeraVez){
	  				cabeceras = line./*substring(1).*/split(cvsSplitBy);
	  				esPrimeraVez=false;
	  				CSVHandlerUpdate.cabecera=line/*.substring(1)*/;/*Esto es s�lo en caso de que estemos haciendo update*/
	  			}
	  			else{
	  	    		String[] valoresCsv= line.replace("\"", "'").split(cvsSplitBy);
	  				try{
	  					if(ColumnasMayorCabecera(valoresCsv))
	  						throw new Exception();
	  	    		JsonRestEstructura jsonEstructura=creaJson(valoresCsv,CSVHandlerUpdate.cabecera.split(cvsSplitBy));
	  	    		

	  	
	  	    		if(jsonEstructura.getCliensec()==null || !jsonEstructura.getCliensec().matches("[0-9]+"))
	  					csv.escribirCSV(pathError.replace(".csv", "_registro_error_clientsec.csv"), line);
	  	    		else
	  	    			this.listaJson.add(jsonEstructura);
	  				}
	  				catch(Exception e){
	  					//e.printStackTrace();
	  					csv.escribirCSV(pathError.replace(".csv", "_error_parser.csv"), line);
	  				}
	  			}
	  		}
	      }
	      catch(IOException e){
	      	e.printStackTrace();
	      }	  
		   	   
	    }
	   
	   private JsonRestEstructura creaJson(String[] valoresCsv, String[] cabeceras){
	    		JsonRestEstructura jsonEstructura= new JsonRestEstructura(pathError);
	    		jsonEstructura.setLine(line);
	    		int i;
	    		for(i=0;i<valoresCsv.length;i++){
	    			switch (cabeceras[i]) {
		   				case "ID_CLIENTE": jsonEstructura.setId_cliente(valoresCsv[i]); break;
		   				case "CLIENSEC": System.out.println("Entro"); jsonEstructura.setCliensec(valoresCsv[i]); break;
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
	   
	   private String insertarNoVacio(String valor){
		   if(valor==null || valor.length()==0)
			   return " ";
		   return valor;
	   }
	   
	   private boolean ColumnasMayorCabecera(String[] valoresCsv){
		   return CSVHandlerUpdate.cabecera.split(cvsSplitBy).length<valoresCsv.length;
	   }
	   
	   
	   
	   
	   public boolean isFin() {
		return fin;
	}


	public ArrayList<JsonRestEstructura> getListaJson() {
		return listaJson;
	}

}

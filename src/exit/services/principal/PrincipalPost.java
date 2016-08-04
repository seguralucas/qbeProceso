package exit.services.principal;


import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.ejecutores.EjecutorInsercionContactos;
import exit.services.principal.ejecutores.EjecutorInsercionIncidentes;
import exit.services.principal.ejecutores.EjecutorUpdateContactosDistintosFicheros;

public class PrincipalPost {
	public static final String UPDATE_CONTACTOS="UPDATE_CONTACTOS";
	public static final String INSERTAR_CONTACTOS="INSERTAR_CONTACTOS";
	public static final String INSERTAR_INCIDENTES="INSERTAR_INCIDENTES";
	
	public static void main(String[] args) throws IOException {
		

		/***********************************************************/
		//***Ejecucion Paralela***/
/***********************************************************/
   		long time_start, time_end;
    	time_start = System.currentTimeMillis();
    	if(ParserXMLWSConnector.getInstance().getAcction().equalsIgnoreCase(UPDATE_CONTACTOS)){
	    	EjecutorUpdateContactosDistintosFicheros ejecutoUpdateContactos = new EjecutorUpdateContactosDistintosFicheros();
	    	try {
			//	ejecutoUpdateContactos.updatear();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	time_end = System.currentTimeMillis();
	    	System.out.println(ManagementFactory.getThreadMXBean().getThreadCount() );
	    	double tiempoDemorado=(time_end - time_start)/1000/60 ;
    		if(tiempoDemorado>1){
        		FileWriter fw = new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("duracion.txt"));
    			fw.write("El proceso de updateo demoró un total de: "+tiempoDemorado+" minutos");
        		fw.close();
    		}
    	}
    	else if(ParserXMLWSConnector.getInstance().getAcction().equalsIgnoreCase(INSERTAR_CONTACTOS)){
	    	EjecutorInsercionContactos hiloApartre = new EjecutorInsercionContactos();
	    //	hiloApartre.start();
	        synchronized(hiloApartre){
	            try{
	                hiloApartre.wait();
	            }catch(InterruptedException e){
	            	e.printStackTrace();
		        	FileWriter fw = new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("errorLote.txt"));
		        	fw.write(e.getMessage());
		        	fw.close();	            }
	         }
	    	time_end = System.currentTimeMillis();
	    	System.out.println(ManagementFactory.getThreadMXBean().getThreadCount() );
	    	System.out.println("Creación de threads terminada. Tiempo: "+ ( time_end - time_start ) +" milliseconds");
    	}
    	else if(ParserXMLWSConnector.getInstance().getAcction().equalsIgnoreCase(INSERTAR_INCIDENTES)){
    		EjecutorInsercionIncidentes hiloApartre = new EjecutorInsercionIncidentes();
	      	try {
	      		hiloApartre.insertar();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	time_end = System.currentTimeMillis();
	    	System.out.println(ManagementFactory.getThreadMXBean().getThreadCount() );
	    	double tiempoDemorado=(time_end - time_start)/1000/60 ;
    		if(tiempoDemorado>1){
        		FileWriter fw = new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("duracion.txt"));
    			fw.write("El proceso de updateo demoró un total de: "+tiempoDemorado+" minutos");
        		fw.close();
    		}    	}
    	
/***********************************************************/
		//***Ejecucion secuencial***/
/***********************************************************/
		 /*	
    	long time_start, time_end;
    	time_start = System.currentTimeMillis();
	 	ParserXMLWSConnector parser = new ParserXMLWSConnector();
		WSConector wsCon = null; 
		try {
			wsCon= new WSConector(parser);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al establecer conexion");
		}
		CSVHandler csv = new CSVHandler();
	 	ArrayList<File> pathsCSV= csv.getAllCSV(parser.getPathCSVRegistros());
	 	int i=0; 

		 // Process the data from the input stream.

		for(File path:pathsCSV){
			System.out.println(path.getName());
		 	ConvertidosJSONCSV jsonHandler = new ConvertidosJSONCSV();
			jsonHandler.convertirCSVaArrayListJSON(path);
			for(JsonRestEstructura json: jsonHandler.getListaJson()){
				boolean excepcion= false;
				try{
				JSONHandler jsonH=json.createJson();
				}
				catch(ExceptionLongitud e){
					excepcion=true;
				}
				if(!excepcion)
//					wsCon.realizarPeticion(jsonH,"0");
				i++;
				if(i==100000)
					return;
			//	System.out.println(i);
			}


		}
		System.out.println(i);
		time_end = System.currentTimeMillis();

        
		// WSConector wsConector = new WSConector(WSConector.URL_DEFAULT, "POST", "application/json", "lucas.segura", "Lucas.segura");
		// wsConector.post();*/
		

	}

}

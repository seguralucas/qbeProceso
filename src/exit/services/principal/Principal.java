package exit.services.principal;


import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import exit.services.fileHandler.FilesAProcesarManager;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.ejecutores.EjecutorInsercionContactos;
import exit.services.principal.ejecutores.EjecutorInsercionIncidentes;
import exit.services.principal.ejecutores.EjecutorInsercionIncidentesDistintosFicheros;
import exit.services.principal.ejecutores.EjecutorUpdateContactosDistintosFicheros;
import exit.services.principal.peticiones.FetchMapeos;

public class Principal {
	public static final String UPDATE_CONTACTOS="UPDATE_CONTACTOS";
	public static final String INSERTAR_CONTACTOS="INSERTAR_CONTACTOS";
	public static final String INSERTAR_INCIDENTES="INSERTAR_INCIDENTES";
	
	public static void main(String[] args) throws IOException {
		

		/***********************************************************/
		//***Ejecucion Paralela***/
/***********************************************************/
   		long time_start, time_end;
    	time_start = System.currentTimeMillis();
    	System.out.println("Usuario: "+ParserXMLWSConnector.getInstance().getUser());
    	System.out.println("Password: "+ParserXMLWSConnector.getInstance().getPassword());
    	System.out.println("Acci�n: "+ParserXMLWSConnector.getInstance().getAcction());
    	System.out.println("Nivel de paralelismo: "+ParserXMLWSConnector.getInstance().getNivelParalelismo());
    	System.out.println("Usa Proxy: "+ParserXMLWSConnector.getInstance().getUsaProxy());
    	if(ParserXMLWSConnector.getInstance().getUsaProxy().equalsIgnoreCase("si")){
        	System.out.println("IP Proxy: "+ParserXMLWSConnector.getInstance().getIpProxy());
    		System.out.println("Puerto Proxy: "+ParserXMLWSConnector.getInstance().getPuertoProxy());
    	}
    	if(ParserXMLWSConnector.getInstance().getAcction().equalsIgnoreCase(UPDATE_CONTACTOS)){
	    	EjecutorUpdateContactosDistintosFicheros ejecutoUpdateContactos = new EjecutorUpdateContactosDistintosFicheros();
	    	try {
				ejecutoUpdateContactos.updatear();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	time_end = System.currentTimeMillis();
	    	System.out.println(ManagementFactory.getThreadMXBean().getThreadCount() );
	    	double tiempoDemorado=(time_end - time_start)/1000/60 ;
    		if(tiempoDemorado>1){
        		FileWriter fw = new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("duracion.txt"));
    			fw.write("El proceso de updateo demor� un total de: "+tiempoDemorado+" minutos");
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
	    	System.out.println("Creaci�n de threads terminada. Tiempo: "+ ( time_end - time_start ) +" milliseconds");
    	}
    	else if(ParserXMLWSConnector.getInstance().getAcction().equalsIgnoreCase(INSERTAR_INCIDENTES)){
    		EjecutorInsercionIncidentesDistintosFicheros hiloApartre = new EjecutorInsercionIncidentesDistintosFicheros();
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
    			fw.write("El proceso de updateo demor� un total de: "+tiempoDemorado+" minutos");
        		fw.close();
    		}
    }
    	
/***********************************************************/
		//***Borrar ficheros de ejecucion***/
/***********************************************************/
		FilesAProcesarManager.getInstance().deleteCSVAProcesar();

		

	}

}

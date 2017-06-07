package exit.services.servicio;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import exit.services.fileHandler.ConvertidosJSONCSV;
import exit.services.parser.ParserXMLFtpConfiguracion;
import exit.services.principal.FtpConector;



@Path("/clientes")
public class ServicioLeerCsvFtp {
    
    @GET
    @Path("/{param}")
    @Produces(MediaType.TEXT_HTML)
    public String getSaludoHTML(@PathParam("param") String id) {
    /*	ParserXMLFtpConfiguracion xmlConfig = new ParserXMLFtpConfiguracion();
    	ConvertidosJSONCSV json = new ConvertidosJSONCSV("archivoErrorEjecucion.csv");
    	FtpConector ftp= xmlConfig.crearConexionFTP();
    	for(String remoteFile: xmlConfig.getListaArchivos()){
    		File fileCSV=ftp.downloadFile(remoteFile, FtpConector.DOWNLOAD_DEFAULT_FILE);
    		//json.convertirCSVaJSON(fileCSV,id);
    	}
        return  id;*/
    	return "TEST";
    }
    

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getSaludoPlain() throws IOException {
    	/*ParserXMLFtpConfiguracion xmlConfig = new ParserXMLFtpConfiguracion();
    	ConvertidosJSONCSV json = new ConvertidosJSONCSV("archivoErrorEjecucion.csv");
    	FtpConector ftp= new FtpConector(xmlConfig);
    	for(String remoteFile: xmlConfig.getListaArchivos()){
    		File fileCSV=ftp.downloadFile(remoteFile, xmlConfig.getDirectorioDescarga());
    		//json.convertirCSVaJSON(fileCSV);
    	}*/
        return  "asd";
    }
    
    
    
    
 

    
}
package exit.services.procesadoresRespuesta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.csvreader.CsvWriter;

import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.Separadores;

public class ProcesarRespuestaReportesSOAP implements IProcesarRespuestaSOAP{
	 private ParserXMLWSConnector parser;

	 
	public ProcesarRespuestaReportesSOAP() {
		this.parser=ParserXMLWSConnector.getInstance();
	}
	@Override
	public void procesarPeticionOK(BufferedReader in, int responseCode) throws Exception{

		StringBuilder xml= new StringBuilder();
		String linea;
		while((linea=in.readLine())!=null)
			xml.append(linea);		 
		FileWriter fw=new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("resultadoConculta.xml"), true);
				fw.write(xml.toString());
		  try {	
		        InputStream stream = new ByteArrayInputStream(xml.toString().getBytes(StandardCharsets.UTF_8));
		        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        Document doc= builder.parse(stream);	
			     doc.getDocumentElement().normalize();
			     Element eElement = doc.getDocumentElement();
			     String[] cabeceras= eElement
			              .getElementsByTagName("n0:Columns")
			          .item(0)
			          .getFirstChild().getNodeValue().split(",");
				 escribirCSV("reporte.csv",cabeceras);
				 NodeList listaDeFilas= eElement
			              .getElementsByTagName("n0:Row");
			     for(int i=0;i<listaDeFilas.getLength();i++){
			    	 String[] valoresReporte=listaDeFilas.item(i).getFirstChild().getNodeValue().split(",");
					 escribirCSV("reporte.csv",valoresReporte);
			     }
			      } catch (Exception e) {
			         e.printStackTrace();
			      }
		  
	 }
	@Override
	public void procesarPeticionError(BufferedReader in,  int responseCode) throws Exception{
		String path=parser.getFicheroError().replace(".txt", "_error_insercion_"+responseCode+".txt");
	     	File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	out.println(inputLine);
            }
            out.close();
            path="Reportes_"+parser.getFicheroError();
            fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
            out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
            out.println(Separadores.SEPARADOR_ERROR_PETICION);
            out.close();
		 }
	
	private void escribirCSV(String path,String[] valores) throws IOException{
		 CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
		 for(int i=0;i<valores.length;i++)
			 csvOutput.write(insertarNoNull(valores[i])); 
		 csvOutput.endRecord();
         csvOutput.close();
         
      
 }
	 private String insertarNoNull(String cadena){
		 if(cadena!=null)
			 return cadena;
		 return "";
	 }
	
/**************************************************************************************************************************/
}
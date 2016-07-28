package exit.services.parser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import exit.services.principal.FtpConector;

public class ParserXMLFtpConfiguracion {
	
	//    private static final File INPUT_FILE = new File("../webapps/testWS/FtpDatosConexion.xml"); 
	   private static final File INPUT_FILE = new File("WebContent/FtpDatosConexion.xml"); 

	    private String server;
		private int port;
		private String user;
		private String pass;
		private ArrayList<String> listaArchivos;
		private String directorioDescarga;
		
		public ParserXMLFtpConfiguracion(){
			listaArchivos= new ArrayList<String>();
			buscarConfiguracionXML();
		}
		
	   public void buscarConfiguracionXML(){	   
		  try {	

		     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		     DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		     Document doc = dBuilder.parse(INPUT_FILE);
		     doc.getDocumentElement().normalize();
		     Element eElement = doc.getDocumentElement();
		     server= eElement
		              .getElementsByTagName("servidor")
		          .item(0)
		          .getFirstChild().getNodeValue();;
		 port= Integer.parseInt(eElement
		               .getElementsByTagName("puerto")
		               .item(0)
		               .getFirstChild().getNodeValue());
		 user = eElement
		          .getElementsByTagName("user")
		          .item(0)
		          .getFirstChild().getNodeValue();
		 directorioDescarga = eElement
		          .getElementsByTagName("directorioDescarga")
		          .item(0)
		          .getFirstChild().getNodeValue();
		 pass = eElement
		          .getElementsByTagName("pass")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		 NodeList archivosEnXml = eElement
		          .getElementsByTagName("archivo");
		 
		 
		 listaArchivos.clear();
		 for(int i=0; i<archivosEnXml.getLength();i++){
			 listaArchivos.add(archivosEnXml.item(i).getFirstChild().getNodeValue());
		 }
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		   }
	   
	   @Deprecated
	   public void crearConexionFTP(FtpConector ftpConector){
	         ftpConector.setServer(server);
	         ftpConector.setPort(port);
	         ftpConector.setUser(user);
	         ftpConector.setPass(pass);
	   }
	   @Deprecated
	   public FtpConector crearConexionFTP(){
		   return new FtpConector(server,port,user,pass);
	   }

	public ArrayList<String> getListaArchivos() {
		return listaArchivos;
	}

	public String getServer() {
		return server;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getDirectorioDescarga() {
		return directorioDescarga;
	}
	   
}
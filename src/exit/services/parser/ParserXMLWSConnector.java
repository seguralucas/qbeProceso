package exit.services.parser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParserXMLWSConnector {
	//    private static final File INPUT_FILE = new File("../webapps/testWS/WSDatosConexion.xml"); 
	   private static final File INPUT_FILE = new File("WebContent/WSDatosConexion.xml"); 
	   
	   private String ficheroOk;
	   private String ficheroError;
	   private String ficheroCSVOK;
	   private String ficheroCSVERRORPETICION;
	   private String ficheroCSVERROREJECUCION;
	   private String pathCSVRegistros;
	   private String url;
	   private String acction;
	   private String contentType;
	   private String user;
	   private String password;
	   private int nivelParalelismo;
	   private String ipProxy;
	   private String puertoProxy;
	   private String separadorCSV;
	   private String usaProxy;
	   private static ParserXMLWSConnector instance=null;
		
		private ParserXMLWSConnector(){
			buscarConfiguracionXML();
		}
		
		public static ParserXMLWSConnector getInstance(){
			if(instance==null)
				instance= new ParserXMLWSConnector();
			return instance;
		}
		
	   public void buscarConfiguracionXML(){	   
		  try {	

		     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		     DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		     Document doc = dBuilder.parse(INPUT_FILE);
		     doc.getDocumentElement().normalize();
		     Element eElement = doc.getDocumentElement();
		     ficheroOk= eElement
		              .getElementsByTagName("ficheroOk")
		          .item(0)
		          .getFirstChild().getNodeValue();;
		          ficheroError= eElement
		               .getElementsByTagName("ficheroError")
		               .item(0)
		               .getFirstChild().getNodeValue();
		          ficheroCSVOK = eElement
		          .getElementsByTagName("ficheroCSVOK")
		          .item(0)
		          .getFirstChild().getNodeValue();
		          ficheroCSVERRORPETICION = eElement
		          .getElementsByTagName("ficheroCSVERRORPETICION")
		          .item(0)
		          .getFirstChild().getNodeValue();
		          ficheroCSVERROREJECUCION = eElement
		          .getElementsByTagName("ficheroCSVERROREJECUCION")
		          .item(0)
		          .getFirstChild().getNodeValue();		          
		          pathCSVRegistros = eElement
		          .getElementsByTagName("pathCSVRegistros")
		          .item(0)
		          .getFirstChild().getNodeValue();
		          url = eElement
		          .getElementsByTagName("url")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          acction = eElement
		          .getElementsByTagName("acction")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          contentType = eElement
		          .getElementsByTagName("content-type")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          user = eElement
		          .getElementsByTagName("user")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          password = eElement
		          .getElementsByTagName("password")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          nivelParalelismo = Integer.parseInt(eElement
		          .getElementsByTagName("nivelParalelismo")
		                  .item(0)
		                  .getFirstChild().getNodeValue());
		          
		          separadorCSV=	  eElement
		          .getElementsByTagName("separadorCSV")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          
		          puertoProxy=	  eElement
		          .getElementsByTagName("puertoProxy")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          
		          ipProxy=	  eElement
		          .getElementsByTagName("ipProxy")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          
		          usaProxy=	  eElement
		          .getElementsByTagName("usaProxy")
		                  .item(0)
		                  .getFirstChild().getNodeValue();
		          }
		  

		  catch (Exception e) {
		         e.printStackTrace();
		      }
		  
		  
		   }
	   
	   

	public String getSeparadorCSV() {
		return separadorCSV;
	}
	
	public String getSeparadorCSVREGEX() {
		return "\\"+separadorCSV;
	}
	

	public void setSeparadorCSV(String separadorCSV) {
		this.separadorCSV = separadorCSV;
	}


	public int getNivelParalelismo() {
		return nivelParalelismo;
	}

	public void setNivelParalelismo(int nivelParalelismo) {
		this.nivelParalelismo = nivelParalelismo;
	}


	public String getFicheroCSVERRORPETICION() {
		return ficheroCSVERRORPETICION;
	}

	public void setFicheroCSVERRORPETICION(String ficheroCSVERRORPETICION) {
		this.ficheroCSVERRORPETICION = ficheroCSVERRORPETICION;
	}

	public String getFicheroCSVERROREJECUCION() {
		return ficheroCSVERROREJECUCION;
	}

	public void setFicheroCSVERROREJECUCION(String ficheroCSVERROREJECUCION) {
		this.ficheroCSVERROREJECUCION = ficheroCSVERROREJECUCION;
	}

	public String getFicheroOk() {
		return ficheroOk;
	}

	public void setFicheroOk(String ficheroOk) {
		this.ficheroOk = ficheroOk;
	}

	public String getFicheroError() {
		return ficheroError;
	}

	public void setFicheroError(String ficheroError) {
		this.ficheroError = ficheroError;
	}

	public String getFicheroCSVOK() {
		return ficheroCSVOK;
	}

	public void setFicheroCSVOK(String ficheroCSVOK) {
		this.ficheroCSVOK = ficheroCSVOK;
	}

	public String getPathCSVRegistros() {
		return pathCSVRegistros;
	}

	public void setPathCSVRegistros(String pathCSVRegistros) {
		this.pathCSVRegistros = pathCSVRegistros;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAcction() {
		return acction;
	}

	public void setAcction(String acction) {
		this.acction = acction;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIpProxy() {
		return ipProxy;
	}

	public String getPuertoProxy() {
		return puertoProxy;
	}

	public String getUsaProxy() {
		return usaProxy;
	}

	
	
	
}

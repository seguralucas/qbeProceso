package TESTSOAP;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.csvreader.CsvWriter;

import Decoder.BASE64Encoder;
import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import exit.services.principal.WSConector;
import exit.services.procesadoresRespuesta.IProcesarRespuestaSOAP;
import exit.services.procesadoresRespuesta.ProcesarRespuestaReportesSOAP;
import exit.services.procesadoresRespuesta.ProcesarResputaInsercionIncidentes;


public class TestSoap {
		 private HttpURLConnection conexion;		 
		 private URL url;
		 public static void main(String[] args) throws Exception{	
				try{
			 PropiedadReporteSOAP prs= PropiedadReporteSOAP.getInstance();
			 TestSoap ts = new TestSoap("POST","https://qbe.custhelp.com/cgi-bin/qbe.cfg/services/soap","text/xml");
	        	HttpURLConnection conn=ts.getConexion();
	        	IProcesarRespuestaSOAP procesarRespuestaSOAP = new ProcesarRespuestaReportesSOAP();
	        	DataOutputStream wr = new DataOutputStream(
	        			conn.getOutputStream());
	        	
	        	wr.write(("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"urn:messages.ws.rightnow.com/v1_3\" xmlns:v11=\"urn:base.ws.rightnow.com/v1_3\">   <soapenv:Header>   <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" soapenv:mustUnderstand=\"1\">            <wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"UsernameToken-3902281\">                <wsse:Username>usuario.importador</wsse:Username>                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">Lucas123</wsse:Password>            </wsse:UsernameToken>   </wsse:Security>       <v1:ClientInfoHeader>         <v1:AppID>?</v1:AppID>      </v1:ClientInfoHeader>   </soapenv:Header><soapenv:Body><ns7:RunAnalyticsReport xmlns:ns7=\"urn:messages.ws.rightnow.com/v1_3\"><ns7:AnalyticsReport xmlns:ns4=\"urn:objects.ws.rightnow.com/v1_3\"><ID xmlns=\"urn:base.ws.rightnow.com/v1_3\" id=\""+prs.getIDReporte()+"\" /><ns4:Filters xsi:type=\"ns4:AnalyticsReportFilter\"></ns4:Filters></ns7:AnalyticsReport><ns7:Limit>10</ns7:Limit><ns7:Start>0</ns7:Start<ns4:Name>Opportunity ID</ns4:Name></ns7:RunAnalyticsReport></soapenv:Body></soapenv:Envelope>").getBytes("UTF-8"));
	        	wr.flush();
	        	wr.flush();
	        	wr.close();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	procesarRespuestaSOAP.procesarPeticionOK(in, responseCode);
	            }
	            else{
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	            	procesarRespuestaSOAP.procesarPeticionError(in, responseCode);
	            }
				}
				catch(Exception e){
					CSVHandler csv= new CSVHandler();
					csv.escribirErrorException(e.getStackTrace());
					}
		 }
		 

		 
		
		 public TestSoap(String method,String url,String contentType) throws Exception{
			 	this.url = new URL(url);
			 	initConecction(method,contentType);
		 }
		 
		 public TestSoap(String method,String url) throws Exception{
			 	this(url,method,null);
		 }
		 
		 public TestSoap(String method) throws Exception{
			 	this(method,ParserXMLWSConnector.getInstance().getUrl());
		 }
		 
		
		private void initConecction(String method, String contentType) throws Exception{
			try{
			 PropiedadReporteSOAP prs= PropiedadReporteSOAP.getInstance();
			HttpURLConnection conn = null;
			if(prs.getUsaProxy().equalsIgnoreCase("SI")){
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(prs.getIP(), Integer.parseInt(prs.getPuerto())));
				conn = (HttpURLConnection) url.openConnection(proxy);
			}
			else
				conn = (HttpURLConnection) url.openConnection();
			if(method.equalsIgnoreCase("POST")){
					conn.setRequestMethod("POST");
			}
			else if(method.equalsIgnoreCase("UPDATE")){
				conn.setRequestMethod("POST");
				conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			}
			else if(method.equalsIgnoreCase("GET")){
				conn.setRequestMethod("GET");
			}
			if(contentType!=null)
				conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("soapAction", "RunAnalyticsReport");
			
			
		//	conn.setRequestProperty("charset", "UTF-8");
			conn.setDoOutput(true);
			String userPassword = ParserXMLWSConnector.getInstance().getUser() + ":" + ParserXMLWSConnector.getInstance().getPassword();
			BASE64Encoder encode= new BASE64Encoder();
			String encoding = encode.encode(userPassword.getBytes());
		//	conn.setRequestProperty("Authorization", "Basic " + encoding);	 
		//	conn.setRequestProperty("OSvC-CREST-Suppress-All", "true");	 
			this.conexion= conn;
			}
			catch(Exception e){
				CSVHandler csv= new CSVHandler();
				csv.escribirErrorException(e.getStackTrace());
				}
			}	

		public HttpURLConnection getConexion() {
			return conexion;
		}
}



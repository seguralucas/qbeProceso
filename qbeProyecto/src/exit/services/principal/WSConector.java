package exit.services.principal;


import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import Decoder.BASE64Encoder;
import exit.services.parser.ParserXMLWSConnector;



public class WSConector {
	 private HttpURLConnection conexion;
	 
	 private URL url;
	
	 public WSConector(String method,String url,String contentType) throws Exception{
		 	this.url = new URL(url);
		 	initConecction(method,contentType);
	 }
	 
	 public WSConector(String method,String url) throws Exception{
		 	this(url,method,null);
	 }
	 
	 public WSConector(String method) throws Exception{
		 	this(method,ParserXMLWSConnector.getInstance().getUrl());
	 }
	
	private void initConecction(String method, String contentType) throws Exception{
		HttpURLConnection conn=null;
		if(ParserXMLWSConnector.getInstance().getUsaProxy().equalsIgnoreCase("SI")){
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ParserXMLWSConnector.getInstance().getIpProxy(), Integer.parseInt(ParserXMLWSConnector.getInstance().getPuertoProxy())));
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
		else if(method.equalsIgnoreCase("DELETE")){
			conn.setRequestMethod("DELETE");
		}
		if(contentType!=null)
			conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("charset", "UTF-8");
		conn.setDoOutput(true);
		String userPassword = ParserXMLWSConnector.getInstance().getUser() + ":" + ParserXMLWSConnector.getInstance().getPassword();
		BASE64Encoder encode= new BASE64Encoder();
		String encoding = encode.encode(userPassword.getBytes());
		conn.setRequestProperty("Authorization", "Basic " + encoding);	 
		conn.setRequestProperty("OSvC-CREST-Suppress-All", "true");	 
		this.conexion= conn;
		
	}

	public HttpURLConnection getConexion() {
		return conexion;
	}
	 
	

	 
	 
	 
	 
	
}

package exit.services.principal;


import java.net.HttpURLConnection;
import java.net.URL;
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
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
		conn.setRequestProperty("charset", "UTF-8");
		conn.setDoOutput(true);
		System.out.println(ParserXMLWSConnector.getInstance().getUser());
		String userPassword = ParserXMLWSConnector.getInstance().getUser() + ":" + ParserXMLWSConnector.getInstance().getPassword();
		String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
		conn.setRequestProperty("Authorization", "Basic " + encoding);	 
		conn.setRequestProperty("OSvC-CREST-Suppress-All", "true");	 
		this.conexion= conn;
		
	}

	public HttpURLConnection getConexion() {
		return conexion;
	}
	 
	

	 
	 
	 
	 
	
}

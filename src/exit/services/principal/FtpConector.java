package exit.services.principal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import exit.services.parser.ParserXMLFtpConfiguracion;
public class FtpConector {
	
	public static final String REMOTE_DEFAULT_FILE="/clientes.csv";
	public static final String DOWNLOAD_DEFAULT_FILE="D:/Exit/QBE/donwloadFTP/tmp.csv";
	
	public static final String SERVER_DEFAULT = "www.total01.ferozo.net";
	public static final int PORT_DEFAULT = 21;
	public static final String USER_DEFAULT = "total01";
	public static final String PASS_DEFAULT = "T0t4lt3x";
	
	private String server;
    private int port;
    private String user;
    private String pass;
	
    
    
     public FtpConector(){

     }
	
	 public FtpConector(String server, int port, String user, String pass) {
		super();
		this.server = server;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}
	 
	 public FtpConector(ParserXMLFtpConfiguracion configXML) {
		this(configXML.getServer(),configXML.getPort(),configXML.getUser(),configXML.getPass());
	}
	 

	 

	public File downloadFile(String pathRemoteFile, String pathDownloadFile1) {
	       
	 
	        FTPClient ftpClient = new FTPClient();
	        try {
	 
	            ftpClient.connect(server, port);
	            ftpClient.login(user, pass);
	            ftpClient.enterLocalPassiveMode();
	            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	 
	            // APPROACH #1: using retrieveFile(String, OutputStream)

	            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(pathDownloadFile1));
	            boolean success = ftpClient.retrieveFile(pathRemoteFile, outputStream1);
	            outputStream1.close();
	 
	            if (success) {
	                System.out.println("File "+ pathRemoteFile +" has been downloaded successfully.");
	                return new File(pathDownloadFile1);
	            }
	            return null;
	 
	        } catch (IOException ex) {
	            System.out.println("Error: " + ex.getMessage());
	            ex.printStackTrace();
	            return null;
	        } finally {
	            try {
	                if (ftpClient.isConnected()) {
	                    ftpClient.logout();
	                    ftpClient.disconnect();
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	

	public String getServer() {
		return server;
	}



	public void setServer(String server) {
		this.server = server;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public String getPass() {
		return pass;
	}



	public void setPass(String pass) {
		this.pass = pass;
	}
}

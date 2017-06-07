package TESTSOAP;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropiedadReporteSOAP {
	
	private String IDReporte;
	private String usaProxy;
	private String IP;
	private String puerto;
	private String pathSalida;
	private String isUtf8;
	private static PropiedadReporteSOAP instance;
    private PropiedadReporteSOAP() throws IOException{
        Properties props = new Properties();
		props.load(new FileReader("WebContent/configuracionSOAP.properties"));
		IDReporte=props.getProperty("IDReporte");
		usaProxy=props.getProperty("UsaProxy");
		IP=props.getProperty("IP");
		puerto=props.getProperty("Puerto");
		pathSalida=props.getProperty("PathSalida");
		isUtf8=props.getProperty("UTF-8");
    }
    
    public static synchronized PropiedadReporteSOAP getInstance() throws IOException{
    	if(instance==null)
    		instance=new PropiedadReporteSOAP();
    	return instance;
    }

	public String getIDReporte() {
		return IDReporte;
	}

	public void setIDReporte(String iDReporte) {
		IDReporte = iDReporte;
	}

	public String getUsaProxy() {
		return usaProxy;
	}

	public void setUsaProxy(String usaProxy) {
		this.usaProxy = usaProxy;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getIsUtf8() {
		return isUtf8;
	}

	public void setIsUtf8(String isUtf8) {
		this.isUtf8 = isUtf8;
	}
	
	
    
    
    
}

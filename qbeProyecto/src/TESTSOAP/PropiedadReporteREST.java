package TESTSOAP;

import java.util.Properties;

public class PropiedadReporteREST
{
  private String IDReporte;
  private String usaProxy;
  private String IP;
  private String puerto;
  private String eliminarColumnas;
  private String isUtf8;
  private String url;
  private static PropiedadReporteREST instance;
  private String[] cabecerasInt;
  private String nombreSalida;
  private String pathSalida;
  
  private PropiedadReporteREST() throws java.io.IOException
  {
    Properties props = new Properties();
    props.load(new java.io.FileReader("WebContent/configuracionREST.properties"));
    IDReporte = props.getProperty("IDReporte");
    usaProxy = props.getProperty("UsaProxy");
    IP = props.getProperty("IP");
    puerto = props.getProperty("Puerto");
    eliminarColumnas = props.getProperty("EliminarColumnas");
    isUtf8 = props.getProperty("UTF-8");
    url = props.getProperty("Url");
    cabecerasInt = props.getProperty("cabecerasInt").split(",");
    nombreSalida = props.getProperty("nombreSalida");
    pathSalida = props.getProperty("pathSalida");
  }
  
  public static synchronized PropiedadReporteREST getInstance() throws java.io.IOException
  {
    if (instance == null)
      instance = new PropiedadReporteREST();
    return instance;
  }
  
  public int getIDReporte() {
    return Integer.parseInt(IDReporte);
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
  
  public String getEliminarColumnas() {
    return eliminarColumnas;
  }
  
  public String getUrl() {
    return url;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }
  
  public String[] getCabecerasInt() {
    return cabecerasInt;
  }
  
  public void setCabecerasInt(String[] cabecerasInt) {
    this.cabecerasInt = cabecerasInt;
  }
  
  public String getNombreSalida() {
    return nombreSalida;
  }
  
  public void setNombreSalida(String nombreSalida) {
    this.nombreSalida = nombreSalida;
  }
  
  public String getPathSalida() {
    return pathSalida;
  }
  
  public void setPathSalida(String pathSalida) {
    this.pathSalida = pathSalida;
  }
}
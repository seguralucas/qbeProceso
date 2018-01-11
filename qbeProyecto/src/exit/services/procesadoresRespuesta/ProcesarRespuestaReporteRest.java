package exit.services.procesadoresRespuesta;

import TESTSOAP.PropiedadReporteREST;
import com.csvreader.CsvWriter;
import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;
import exit.services.principal.DirectorioManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnmappableCharacterException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ProcesarRespuestaReporteRest
  implements IProcesarRespuestaREST
{
  private ParserXMLWSConnector parser;
  
  public ProcesarRespuestaReporteRest()
  {
    parser = ParserXMLWSConnector.getInstance();
  }
  
  private boolean esCabeceraOmitida(String cabecera) throws IOException {
    if (PropiedadReporteREST.getInstance().getEliminarColumnas() == null)
      return false;
    String[] cabecerasOmitidas = PropiedadReporteREST.getInstance().getEliminarColumnas().split(",");
    for (int i = 0; i < cabecerasOmitidas.length; i++)
      if (cabecerasOmitidas[i].equalsIgnoreCase(cabecera))
        return true;
    return false;
  }
  
  private boolean isIndiceOmitido(Integer indice, ArrayList<Integer> indicesOmitidos) {
    for (Integer i : indicesOmitidos)
      if (i.equals(indice))
        return true;
    return false;
  }
  
  public void procesarPeticionOK(BufferedReader in, JSONHandler json, int responseCode)
    throws Exception
  {
    StringBuilder jsonBuilder = new StringBuilder();
    
    FileWriter fw = new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("resultadoConsulta.json"), true);
    String linea;
    while ((linea = in.readLine()) != null) {
      jsonBuilder.append(linea);
      fw.write(linea + "\r\n");
    }
    String jsonString = jsonBuilder.toString();
    JSONParser parser = new JSONParser();
    JSONObject jsonObject = (JSONObject)parser.parse(new String(jsonString.getBytes(), "UTF-8"));
    
    fw.close();
    Charset charset;
    if (PropiedadReporteREST.getInstance().getIsUtf8().equalsIgnoreCase("SI")) {
      charset = Charset.forName("UTF-8");
    } else
      charset = Charset.forName("ISO-8859-1");
    try {
      String pathSalidaCSV = PropiedadReporteREST.getInstance().getPathSalida() + "/" + PropiedadReporteREST.getInstance().getNombreSalida();
      
      OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(pathSalidaCSV, true), charset.newEncoder());
      
      Long count = (Long)jsonObject.get("count");
      if (count.compareTo(Long.valueOf(10000L)) < 0) {
        TESTSOAP.AutomatizadorRest.esFin = true;
      }
      JSONArray columnas = (JSONArray)jsonObject.get("columnNames");
      String cabeceras = "";
      for (int i = 0; i < columnas.size(); i++) {
        if (i != columnas.size() - 1) {
          cabeceras = cabeceras + columnas.get(i) + ";";
        } else
          cabeceras = cabeceras + columnas.get(i);
      }
      String[] cabecerasSinOmision = cabeceras.split(";");
      ArrayList<Integer> indicisesOmitidos = new ArrayList();
      String cabeceraAGuardar = "";
      for (int i = 0; i < cabecerasSinOmision.length; i++) {
        if (esCabeceraOmitida(cabecerasSinOmision[i])) {
          indicisesOmitidos.add(Integer.valueOf(i));
        } else {
          cabeceraAGuardar = cabeceraAGuardar + cabecerasSinOmision[i] + ";";
        }
      }
      cabeceraAGuardar = cabeceraAGuardar.substring(0, cabeceraAGuardar.length() - 1);
      if (esPrimeraIteracion(pathSalidaCSV)) {
        writer.write(cabeceraAGuardar);
        writer.write("\r\n");
      }
      String[] arrayCabeceras = cabeceraAGuardar.split(";");
      JSONArray rows = (JSONArray)jsonObject.get("rows");
      for (int i = 0; i < rows.size();) {
        try {
          String valores = "";
          JSONArray row = (JSONArray)rows.get(i);
          for (int h = 0; h < row.size(); h++) {
            if (h != columnas.size() - 1) {
              valores = valores + row.get(h) + ";";
            } else
              valores = valores + row.get(h);
          }
          String[] arr = valores.split(";");
          StringBuilder sb = new StringBuilder();
          for (int j = 0; j < arr.length; j++)
            if (!isIndiceOmitido(Integer.valueOf(j), indicisesOmitidos))
            {
              if ((arr[j].length() == 0) || (arr[j].equalsIgnoreCase("null"))) {
                if (isCabeceraInt(cabecerasSinOmision[j])) {
                  sb.append("0");
                } else {
                  sb.append("Sin valor");
                }
              } else if (arr[j].matches("'[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9]'")) {
                String aux = arr[j].substring(1, 11);
                String[] auxArr = aux.split("-");
                sb.append(auxArr[2] + "/" + auxArr[1] + "/" + auxArr[0]);

              }
              else if (isCabeceraInt(cabecerasSinOmision[j])) {
                sb.append(arr[j].split("\\.")[0]);
              }
              else {
                sb.append(arr[j]);
              }
              sb.append(";");
            }
          String aux = sb.toString();
          try {
            if (charset.newEncoder().canEncode(aux)) {
              writer.write(aux.substring(0, aux.length() - 1));
              writer.write("\r\n");
            }
            else {
              System.out.println("No pudo " + aux);
            }
          } catch (UnmappableCharacterException e) {
            System.out.println(aux);
          }
          i++;
        }
        catch (Exception localException1) {}
      }
      writer.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode)
    throws Exception
  {
    String path = parser.getFicheroError().replace(".txt", "_error_automatizacionReporte_" + responseCode + ".txt");
    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
    String inputLine;
    while ((inputLine = in.readLine()) != null) { 
      out.println(inputLine);
    }
    out.close();
    path = "Reportes_" + parser.getFicheroError();
    fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
    out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
    out.println("**********************************************************************************************");
    out.close();
  }
  
  private void escribirCSV(String path, String[] valores) throws IOException {
    CsvWriter csvOutput = new CsvWriter(new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(path), true), ';');
    for (int i = 0; i < valores.length; i++)
      csvOutput.write(insertarNoNull(valores[i]));
    csvOutput.endRecord();
    csvOutput.close();
  }
  
  private String insertarNoNull(String cadena) {
    if (cadena != null)
      return cadena;
    return "";
  }
  
  private boolean isCabeceraInt(String cabecera) throws IOException {
    String[] cabecerasInt = PropiedadReporteREST.getInstance().getCabecerasInt();
    for (int i = 0; i < cabecerasInt.length; i++) {
      if (cabecerasInt[i].equalsIgnoreCase(cabecera))
        return true;
    }
    return false;
  }
  
  private boolean esPrimeraIteracion(String path) { File f = new File(path);
    return (!f.exists()) || (f.length() == 0L);
  }
}
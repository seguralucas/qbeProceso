package BORRAR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class ReemplazarPuntoYComa {
	
	public static void main(String[] args){
//		File f2= new File("C:/Users/Lucas/Desktop/final.csv");
//		try(BufferedReader br = new BufferedReader(
// 		         new InputStreamReader(
// 		                 new FileInputStream("C:/Users/Lucas/Desktop/conPuntoa.txt"), "UTF-8"));
//			BufferedReader br2 = new BufferedReader(
//		 		         new InputStreamReader(
//		 		                 new FileInputStream("C:/Users/Lucas/Desktop/sinhilo.csv"), "UTF-8"))
//				){
//			PrintWriter pw= new PrintWriter(f2);
//			String line;
//			String line2;
//			pw.write(br2.readLine());
//			pw.write("\n");
//			while((line=br.readLine())!=null && (line2=br2.readLine())!=null){
//				pw.write(line2+line);
//				pw.write("\n");
//			}
//			pw.close();
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		map.put("PENDIENTE", 112);
		map.put("RESUELTO", 2);
		map.put("INGRESADO", 1);
		map.put("PENDIENTE DOCUMENTACION", 8);
		map.put("INCORRECTO / INCOMPLETO", 3);
		map.put("DERIVADO", 106);
		map.put("NO PROCEDENTE", 107);
		map.put("ANULADO", 108);
		map.put("ATENDIDO", 109);
		map.put("PENDIENTE PARA LLAMAR", 113);
		map.put("COORDINADO", 114);
		
		
		String[] cadena={"Pendiente",
			"Resuelto" ,
			"Ingresado" ,
			"Pendiente Documentacion" ,
			"Incorrecto / Incompleto" ,
			"Derivado" ,
			"No Procedente" ,
			"Anulado" ,
			"Atendido" ,
			"Pendiente para llamar" ,
			"Coordinado"} ;
		for(int i=0;i<cadena.length;i++)
			System.out.println(cadena[i]+"-"+map.get(cadena[i].toUpperCase()));
	}
}

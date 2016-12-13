package exit.extras;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class SepararFicheros {

	public static void main(String[] args) throws IOException{
		File f= new File("H:/Users/GAS/Desktop/borrar/eliminar.csv");
		FileInputStream fis = new FileInputStream(f);
		FileWriter nuevos= new FileWriter("H:/Users/GAS/Desktop/borrar/nuevos.csv");
		FileWriter repetidos= new FileWriter("H:/Users/GAS/Desktop/borrar/repetidos.csv");
		BufferedInputStream bis = new BufferedInputStream(fis);
		DataInputStream dis = new DataInputStream(bis);
		String anterior="";
		while (dis.available() != 0) {
			String line=dis.readLine();
			String[] l=line.split(",");
			if(l[0].equalsIgnoreCase(anterior))
				repetidos.write(line+"\n");
			else
				nuevos.write(line+"\n");
			anterior=l[0];
		}
		nuevos.close();
		repetidos.close();
	}
	
}

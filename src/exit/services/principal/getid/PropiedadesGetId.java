package exit.services.principal.getid;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class PropiedadesGetId {
	private String path;
	private File f;


	private static PropiedadesGetId instance;
    private PropiedadesGetId() throws IOException{
        Properties props = new Properties();
		props.load(new FileReader("WebContent/configuracionGetIDRIGHTNOW.properties"));
		path=props.getProperty("path");
		 f= new File(path);
    }
    
    public static synchronized PropiedadesGetId getInstance() throws IOException{
    	if(instance==null)
    		instance=new PropiedadesGetId();
    	return instance;
    }

	public String getPath() {
		return path;
	}

	public File getF() {
		return f;
	}


    
}

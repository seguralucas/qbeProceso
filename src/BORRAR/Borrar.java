package BORRAR;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import exit.services.parser.ParserXMLWSConnector;

public class Borrar {

	public static void main(String[] args) throws IOException {
					URL url = new URL("https://cbxd-test.crm.us2.oraclecloud.com/crmCommonApi/resources/11.1.9/contacts");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/vnd.oracle.adf.resourceitem+json");
					conn.setRequestProperty("Authorization", "Basic bHNlZ3VyYTpWZXJhbm9kZWw5Mg==");	 
    		        String input ="Content-Type: { \"OrganizationName\": \"Pinnacle Inc\",	\"Type\": \"ZCA_CUSTOMER\",	\"PrimaryAddress\": [{  		\"AddressLine1\": \"500 oracle parkway\",		\"City\": \"Redwood Shores\",  		\"State\": \"CA\", 	\"Country\": \"US\"	}]  }";
   		           conn.setDoOutput(true);
				            OutputStream outputStream = conn.getOutputStream();
				            outputStream.write(input.getBytes());
				            outputStream.flush(); 
					int responseCode = conn.getResponseCode();
					System.out.println(responseCode);

		
	}
}

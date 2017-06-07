package exit.services.procesadoresRespuesta;

import java.io.BufferedReader;
import java.io.IOException;

import exit.services.json.JSONHandler;
import exit.services.parser.ParserXMLWSConnector;

public interface IProcesarRespuestaREST {
	public void procesarPeticionOK(BufferedReader in, JSONHandler json,int responseCode) throws Exception;
    public void procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode) throws Exception;

}

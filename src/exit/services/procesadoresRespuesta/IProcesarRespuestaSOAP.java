package exit.services.procesadoresRespuesta;

import java.io.BufferedReader;

import exit.services.json.JSONHandler;

public interface IProcesarRespuestaSOAP {
	public void procesarPeticionOK(BufferedReader in, int responseCode) throws Exception;
    public void procesarPeticionError(BufferedReader in, int responseCode) throws Exception;

}

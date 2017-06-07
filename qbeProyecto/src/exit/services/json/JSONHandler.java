package exit.services.json;

import org.json.simple.JSONObject;

public class JSONHandler extends JSONObject{
	
	private String line;
	
	private JsonRestClienteEstructura jsonRestEstructura;
	private JsonRestIncidentes jsonRestIncidentes;
	private JsonRestIncidenteDelete jsonRestIncidentesDelete;
	
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	

	public JsonRestIncidenteDelete getJsonRestIncidentesDelete() {
		return jsonRestIncidentesDelete;
	}

	public void setJsonRestIncidentesDelete(JsonRestIncidenteDelete jsonRestIncidentesDelete) {
		this.jsonRestIncidentesDelete = jsonRestIncidentesDelete;
	}

	public JsonRestIncidentes getJsonRestIncidentes() {
		return jsonRestIncidentes;
	}

	public void setJsonRestIncidentes(JsonRestIncidentes jsonRestIncidentes) {
		this.jsonRestIncidentes = jsonRestIncidentes;
	}

	public JsonRestClienteEstructura getJsonRestEstructura() {
		return jsonRestEstructura;
	}

	public void setJsonRestEstructura(JsonRestClienteEstructura jsonRestEstructura) {
		this.jsonRestEstructura = jsonRestEstructura;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString().replace("\\", "").replace(",", ",\n");
	}
	
	public String toStringSinEnter(){
		return super.toString().replace("\\", "");
	}
	
	public String toStringNormal(){
		return super.toString();
	}
}

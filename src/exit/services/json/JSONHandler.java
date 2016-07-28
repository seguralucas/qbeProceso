package exit.services.json;

import org.json.simple.JSONObject;

public class JSONHandler extends JSONObject{
	
	private String line;
	private JsonRestEstructura jsonRestEstructura;
	
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	

	public JsonRestEstructura getJsonRestEstructura() {
		return jsonRestEstructura;
	}

	public void setJsonRestEstructura(JsonRestEstructura jsonRestEstructura) {
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
}

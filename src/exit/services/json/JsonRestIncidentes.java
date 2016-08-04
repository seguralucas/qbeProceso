package exit.services.json;

import java.util.HashMap;

import org.json.simple.JSONArray;

import exit.services.principal.ExceptionLongitud;

public class JsonRestIncidentes implements IJsonRestEstructura{
	private String id;
	private String nro_sac;
	private String modo_contacto;
	private String causa;
	private String producto;
	private String estado;
	private String motivo;
	private String sector_responsable;
	private String hilo_conversacion;
	private String hilo_conversacion_antiguo;
	/***********************************************/
	private String line;
	private JSONHandler json;
	private String pathError;

	public JsonRestIncidentes(String pathError){
		this.pathError=pathError;
	}
	
	@Override
	public JSONHandler createJson(TipoTarea tarea){
		json= new JSONHandler();
		insertarPrimaryContact(tarea);
		insertarSubject(tarea);
		insertarQBE(tarea);
		insertarProducto(tarea);
		insertarEstado(tarea);
		insertarThread(tarea);
		json.setLine(this.getLine());
		json.setJsonRestIncidentes(this);
		return json;
	}
	
	private void insertarPrimaryContact(TipoTarea tarea){
		if(tarea == TipoTarea.INSERTAR){
			if(insertarString(insertarString(this.getId()))!=null){
				JSONHandler jsonContactType = new JSONHandler();
				jsonContactType.put("id",Integer.parseInt(insertarString(this.getId())));
				json.put("primaryContact", jsonContactType);
			}
		}
	}
	
	private void insertarSubject(TipoTarea tarea){
		if(tarea == TipoTarea.INSERTAR){
				json.put("subject", "Incidente importado desde SAC");
		}
	}

	
	private void insertarQBE(TipoTarea tarea){
			JSONHandler jsonCustomFields = new JSONHandler();
			JSONHandler jsonQbe = new JSONHandler();
			int count=0;
			if(tarea==TipoTarea.INSERTAR ){
				if(insertarString(insertarString(this.getNro_sac()))!=null){
					jsonQbe.put("NumeroQbe", insertarString(this.getNro_sac()));
					count++;
				}
			}
			if(count!=0){
				jsonCustomFields.put("Qbe", jsonQbe);
				jsonCustomFields.put("c", new JSONHandler());
				json.put("customFields", jsonCustomFields);
			}
	}
	
	private void insertarProducto(TipoTarea tarea){
		if(tarea==TipoTarea.INSERTAR){
			JSONHandler product = new JSONHandler();
			product.put("lookupName", this.getProducto());
			this.json.put("product", product);
		}
	}

	private void insertarEstado(TipoTarea tarea){
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
		if(tarea==TipoTarea.INSERTAR){
			JSONHandler statusWithType = new JSONHandler();
			JSONHandler status= new JSONHandler();
				status.put("id", 1);
			statusWithType.put("status", status);
			json.put("statusWithType", statusWithType);
		}	
	}
	
	private void insertarThread(TipoTarea tarea){
		if(tarea==TipoTarea.INSERTAR){
			JSONArray arrayThread = new JSONArray();
			JSONHandler jsonArray = new JSONHandler();
				JSONHandler jsonEntryType = new JSONHandler();
				jsonEntryType.put("lookupName", "Nota");
			jsonArray.put("entryType", jsonEntryType);
			String texto= "Modo contacto: "+this.getModo_contacto()+"\n"+"Causa: "+this.getCausa()+"\n"+"Motivo: "+this.getMotivo()+"\nEstado: "+this.getEstado()+"\nSector Responsable: "+this.getSector_responsable()+"\n*********************\n"+this.getHilo_conversacion();
			jsonArray.put("text",texto);
			JSONHandler jsonChannel = new JSONHandler();
				jsonChannel.put("id", 6);
			jsonArray.put("channel", jsonChannel);
			arrayThread.add(jsonArray);
			json.put("threads", arrayThread);
		}
	}	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNro_sac() {
		return nro_sac;
	}
	public void setNro_sac(String nro_sac) {
		this.nro_sac = nro_sac;
	}
	public String getModo_contacto() {
		return modo_contacto;
	}
	public void setModo_contacto(String modo_contacto) {
		this.modo_contacto = modo_contacto;
	}
	public String getCausa() {
		return causa;
	}
	public void setCausa(String causa) {
		this.causa = causa;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getSector_responsable() {
		return sector_responsable;
	}
	public void setSector_responsable(String sector_responsable) {
		this.sector_responsable = sector_responsable;
	}
	public String getHilo_conversacion() {
		return hilo_conversacion;
	}
	public void setHilo_conversacion(String hilo_conversacion) {
		this.hilo_conversacion = hilo_conversacion.replaceAll("#\\|", "\n");
		this.hilo_conversacion_antiguo=hilo_conversacion;

	}
	
	
	
	public String getHilo_conversacion_antiguo() {
		return hilo_conversacion_antiguo;
	}

	public void setHilo_conversacion_antiguo(String hilo_conversacion_antiguo) {
		this.hilo_conversacion_antiguo = hilo_conversacion_antiguo;
	}

	@Override
	public String getPathError() {
		return this.pathError;
	}
	@Override
	public String getLine() {
		return this.line;
	}
	@Override
	public void setLine(String line) {
		this.line=line;
	}
	
	
}

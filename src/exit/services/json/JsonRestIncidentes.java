package exit.services.json;

import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;

import com.sun.jersey.api.ParamException;

import exit.services.excepciones.ExceptionAnioInvalido;
import exit.services.excepciones.ExceptionEstadoInvalido;
import exit.services.excepciones.ExceptionIDNoNumerico;
import exit.services.excepciones.ExceptionIDNullIncidente;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.excepciones.ExceptionModoContactoInvalido;
import exit.services.excepciones.ExceptionTipoIncidenteInvalido;

public class JsonRestIncidentes implements IJsonRestEstructura{
	private static final String SIN_VALOR="SIN VALOR";
	private static final String NULL="NULL";
	/******************************************************/
	private String id;
	private String nro_sac;
	private String modo_contacto;
	private String causa;
	private String producto;
	private String estado;
	private String motivo;
	private String anio;
	private String sector_responsable;
	private String hilo_conversacion;
	private String hilo1;
	private String hilo2;
	//private String hilo_conversacion_antiguo;
	/***********************************************/
	private String line;
	private JSONHandler json;
	private String pathError;
	HashMap<String, Integer> mapEstado;
	public static HashMap<String, Long> mapTipoIncidente= new HashMap<String,Long>();
	public static HashMap<String, Long> mapModoContacto=new HashMap<String,Long>();
	
	public static HashSet<Integer> hashEstados= new HashSet<Integer>();
	public JsonRestIncidentes(String pathError){
		this.pathError=pathError;
		mapEstado = new HashMap<String,Integer>();
		mapEstado.put("DERIVADO", 106);
		mapEstado.put("NO PROCEDENTE", 107);
		mapEstado.put("ANULADO", 108);
		mapEstado.put("ATENDIDO", 109);

		mapEstado.put("PENDIENTE PARA LLAMAR", 113);
		mapEstado.put("COORDINADO", 114);
		mapEstado.put("DERIVADO PARA GESTIONAR", 115);
		mapEstado.put("DEMORA DE RESPUESTOS", 116);
		mapEstado.put("PENDIENTE DE DOCUMENTACION", 117);
		mapEstado.put("PENDIENTE DE CLIENTE", 118);
		mapEstado.put("PENDIENTE", 112);
		mapEstado.put("RESUELTO", 2);
		mapEstado.put("INGRESADO", 1);
		mapEstado.put("RESPUESTA DE CLIENTE", 8);
		mapEstado.put("INCORRECTO / INCOMPLETO", 3);
		
		hashEstados.add(106);
		hashEstados.add(107);
		hashEstados.add(108);
		hashEstados.add(109);
		hashEstados.add(113);
		hashEstados.add(114);
		hashEstados.add(115);
		hashEstados.add(116);
		hashEstados.add(117);
		hashEstados.add(118);
		hashEstados.add(112);
		hashEstados.add(2);
		hashEstados.add(1);
		hashEstados.add(8);
		hashEstados.add(3);
	}
	
	@Override
	public JSONHandler createJson(TipoTarea tarea) throws ExceptionEstadoInvalido, ExceptionTipoIncidenteInvalido, ExceptionIDNullIncidente, ExceptionModoContactoInvalido, ExceptionIDNoNumerico, ExceptionAnioInvalido{
		json= new JSONHandler();
		insertarPrimaryContact(tarea);
		insertarSubject(tarea);
		insertarQBE(tarea);
		insertarProducto(tarea);
		insertarEstado(tarea);
		insertarThread(tarea);
		insertarMotivo(tarea);
		insertarSectorResponsable(tarea);
		json.setLine(this.getLine());
		json.setJsonRestIncidentes(this);
		return json;
	}
	
	private void insertarPrimaryContact(TipoTarea tarea) throws ExceptionIDNullIncidente, ExceptionIDNoNumerico{
		if(tarea == TipoTarea.INSERTAR){
			if(insertarString(insertarString(this.getId()))!=null){
				try{
					Integer.parseInt(insertarString(this.getId()));
				}
				catch(NumberFormatException e){
					throw new ExceptionIDNoNumerico("No se posee un valor ID en la estructura");
				}
				JSONHandler jsonContactType = new JSONHandler();
				jsonContactType.put("id",Integer.parseInt(insertarString(this.getId())));
				json.put("primaryContact", jsonContactType);
			}
			else{
				throw new ExceptionIDNullIncidente("No se posee un valor ID en la estructura");
			}
		}
	}
	
	private void insertarSubject(TipoTarea tarea){
		if(tarea == TipoTarea.INSERTAR){
				json.put("subject", "Incidente importado desde SAC");
		}
	}

	
	private void insertarQBE(TipoTarea tarea) throws ExceptionTipoIncidenteInvalido, ExceptionModoContactoInvalido, ExceptionAnioInvalido{
			JSONHandler jsonCustomFields = new JSONHandler();
			JSONHandler jsonQbe = new JSONHandler();
			int count=0;
			if(tarea==TipoTarea.INSERTAR ){
				if(insertarString(this.getNro_sac())!=null){
					jsonQbe.put("NumeroQbe", insertarString(this.getNro_sac()));
					count++;
				}
				if(insertarString(this.getCausa())!=null && !esSinValor(this.getCausa())){
					HashSet<Long> idTipoIncidente=  new HashSet<Long>(mapTipoIncidente.values());
					if(idTipoIncidente.contains(Long.parseLong(this.getCausa().trim()))){
						JSONHandler tipoIncidente = new JSONHandler();
						tipoIncidente.put("ID", Long.parseLong(this.getCausa().trim()));
						jsonQbe.put("TipoIncidente", tipoIncidente);
						count++;
					}
					else 
						throw new ExceptionTipoIncidenteInvalido("El tipo de incidente de la estructura no es valido");
				}
				if(insertarString(this.getModo_contacto())!=null && !esSinValor(this.getModo_contacto())){
					HashSet<Long> idModoContacto=  new HashSet<Long>(mapModoContacto.values());
					if(idModoContacto.contains(Long.parseLong(this.getModo_contacto().trim()))){
						JSONHandler modoContacto = new JSONHandler();
						modoContacto.put("ID", Long.parseLong(this.getModo_contacto().trim()));
						jsonQbe.put("ModoContacto", modoContacto);
						count++;
					}
					else 
						throw new ExceptionModoContactoInvalido("El tipo de incidente de la estructura no es valido");
				}
				if(insertarString(this.anio)!=null && !esSinValor(this.anio) && this.anio.matches("[0-9][0-9][0-9][0-9]")){
					jsonQbe.put("anio", this.anio);
				}
				else
					throw new ExceptionAnioInvalido("El tipo de incidente de la estructura no es valido");

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
			product.put("id", Integer.parseInt(this.getProducto()));
			this.json.put("product", product);
		}
	}
	
	private boolean esSinValor(String valor){
		return valor==null || valor.equalsIgnoreCase(SIN_VALOR) || valor.equalsIgnoreCase(NULL);
	}

	private void insertarEstado(TipoTarea tarea) throws ExceptionEstadoInvalido{

		if(tarea==TipoTarea.INSERTAR){
			if(insertarString(this.getEstado())!=null && !esSinValor(this.getEstado())){
				if(hashEstados.contains(Integer.parseInt(this.getEstado().trim()))){
					JSONHandler statusWithType = new JSONHandler();
					JSONHandler status= new JSONHandler();
						status.put("id", Integer.parseInt(this.getEstado().trim()));
					statusWithType.put("status", status);
					json.put("statusWithType", statusWithType);
				}
				else 
					throw new ExceptionEstadoInvalido("El estado de la estructura no corresponde a un estado valido");
			}
		}	
	}
	
	private void insertarThread(TipoTarea tarea){
		if(tarea==TipoTarea.INSERTAR){
			JSONArray arrayThread = new JSONArray();
			JSONHandler jsonArray = new JSONHandler();
				JSONHandler jsonEntryType = new JSONHandler();
				jsonEntryType.put("lookupName", "Nota");
			jsonArray.put("entryType", jsonEntryType);
			String texto= "Numero de Sac: "+this.getNro_sac()+"\nModo contacto: "+this.getModo_contacto() +"\nCausa: "+this.getCausa()+"\n"+"Motivo: "+this.getMotivo()+"\nSector Responsable: "+this.getSector_responsable()+"\n*********************\n"+this.getHilo_conversacion();
			jsonArray.put("text",texto);
			JSONHandler jsonChannel = new JSONHandler();
				jsonChannel.put("id", 6);
			jsonArray.put("channel", jsonChannel);
			arrayThread.add(jsonArray);
			json.put("threads", arrayThread);
		}
	}	
	
	private void insertarMotivo(TipoTarea tarea ){
		if(esSinValor(this.getMotivo()) || this.getMotivo().trim().length()==0)
			return;
		if(tarea==TipoTarea.INSERTAR){
			JSONHandler product = new JSONHandler();
			product.put("id",  Integer.parseInt(this.getMotivo()));
			this.json.put("category", product);		
			}		
	}
	private void insertarSectorResponsable(TipoTarea tarea ){
		if(esSinValor(this.getSector_responsable())  || this.getSector_responsable().trim().length()==0)
			return;
		if(tarea==TipoTarea.INSERTAR){
			JSONHandler product = new JSONHandler();
			product.put("id",  Integer.parseInt(this.getSector_responsable()));
			this.json.put("queue", product);		
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

	}
	
	public String getHilo1() {
		return hilo1;
	}

	public void setHilo1(String hilo1) {
		this.hilo1 = hilo1;
	}

	public String getHilo2() {
		return hilo2;
	}

	public void setHilo2(String hilo2) {
		this.hilo2 = hilo2;
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

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	
}

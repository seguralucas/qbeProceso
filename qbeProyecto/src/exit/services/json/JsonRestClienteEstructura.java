package exit.services.json;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.csvreader.CsvWriter;

import exit.services.excepciones.ExceptionFormatoFecha;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.fileHandler.CSVHandler;

public class JsonRestClienteEstructura implements IJsonRestEstructura{
	/*************************************/
	private String pathError;
	
	/*************************************/
	private String	id_cliente;	
	private String	cliensec;
	private String	tipoorganizacion;
	private String	apellido;	
	private String	nombre;
	private String	id_tipo_documento;	
	private String	tipo_documento;	
	private String	nro_documento;	
	private String	sexo;
	private String	fechanacimiento;	
	private String	domicilio;	
	private String	numero;	
	private String	piso;
	private String	puerta;
	private String	localidad;
	private String	cp;
	private String	provincia;
	private String	segmentacion;	
	private String	conflictivo;
	private String	email_personal;	
	private String	email_laboral;
	private String	email_alternativo;
	private String	registro;
	private String	user_name;
	private String	fecha_alta;
	private String	tel_oficina;
	private String	tel_personal;
	/***********************************************/
	private JSONHandler json;
	private String  line;


	public JsonRestClienteEstructura(String pathError){
		this.pathError=pathError;
	}
	
	public JSONHandler createJson(TipoTarea tarea) throws ExceptionLongitud, ExceptionFormatoFecha{
			json= new JSONHandler();
			insertarContactType(tarea);
			insertarCRMModules(tarea);
			insertarQBE(tarea);
			insertarDisable(tarea);
			insertarLogin(tarea);
			insertarName(tarea);
			insertarEmails();
			insertarTelefonos(tarea);
			insertarOrganizacion(tarea);
			insertarTitle(tarea);
			json.setLine(this.getLine());
			json.setJsonRestEstructura(this);
		return json;
	}
	
	
	private void insertarContactType(TipoTarea tarea){
		if(insertarString(insertarString(this.getTipoorganizacion()))!=null){
			JSONHandler jsonContactType = new JSONHandler();
			jsonContactType.put("lookupName", insertarString(this.getTipoorganizacion()));
			json.put("contactType", jsonContactType);
		}
	}
	
	private void insertarCRMModules(TipoTarea tarea){
		if(tarea==TipoTarea.INSERTAR){
			JSONObject jsonCRMModules = new JSONHandler();
			jsonCRMModules.put("marketing", true);
			jsonCRMModules.put("sales", true);
			jsonCRMModules.put("service", true);
			json.put("cRMModules", jsonCRMModules);
		}
	}
	
	private void insertarQBE(TipoTarea tarea) throws ExceptionLongitud, ExceptionFormatoFecha{
		JSONHandler jsonCustomFields = new JSONHandler();
		JSONHandler jsonQbe = new JSONHandler();
		int count=0;
		if(tarea==TipoTarea.INSERTAR){
			jsonQbe.put("AceptaEnvioInformacion", true);
			jsonQbe.put("Beneficios", null);
			}
		if(insertarString(this.getDomicilio())!=null){
				jsonQbe.put("Calle", insertarString(this.getDomicilio()));
				count++;
			}
		if(insertarString(this.getCp())!=null){
			jsonQbe.put("CodigoPostal", insertarString(this.getCp()));
			count++;
		}
		if(insertarString(this.getConflictivo())!=null){
			jsonQbe.put("Conflictivo", insertarTrueOFalse(this.getConflictivo()));
			count++;
		}
		if(tarea==TipoTarea.INSERTAR)
			jsonQbe.put("CuilCuit", null);
		if(insertarString(this.getPuerta())!=null){
			jsonQbe.put("Departamento", insertarString(this.getPuerta()));
			count++;
		}
		if(insertarString(this.getNro_documento())!=null){
			jsonQbe.put("Dni",  insertarString(this.getNro_documento()));
			count++;
		}
		if(insertarString(this.getNumero())!=null){
			jsonQbe.put("Domicilio", insertarString(this.getNumero()));
			count++;
		}
				if( insertarString(this.getFecha_alta())!=null){
					jsonQbe.put("FechaAlta", insertarFecha(this.getFecha_alta()));
					count++;
				}

			if(insertarString(this.getSexo())!=null){
				jsonQbe.put("Genero", insertarString(this.getSexo()));
				count++;
			}
			if(tarea==TipoTarea.INSERTAR){
				jsonQbe.put("GrupoAfinidad", null);
			}
			if(insertarString(this.getId_cliente())!=null){
				jsonQbe.put("IdBI", insertarString(this.getId_cliente()));
				count++;
			}
			if( insertarString(this.getId_tipo_documento())!=null){
				jsonQbe.put("IdTipoDocumento", insertarString(this.getId_tipo_documento()));
				count++;
			}
			if(insertarString(this.getLocalidad())!=null){
				jsonQbe.put("Localidad", insertarString(this.getLocalidad()));
				count++;
			}
			try{
				if(insertarString(this.getFechanacimiento())!=null){
					jsonQbe.put("Nacimiento", insertarFecha(this.getFechanacimiento()));
					count++;
				}
			}
			catch(Exception e){
				throw new ExceptionLongitud();
			}
			if(insertarString(this.getPiso())!=null){
				jsonQbe.put("Piso", insertarString(this.getPiso()));
				count++;
			}
			if(insertarString(this.getProvincia())!=null){
				jsonQbe.put("Provincia", insertarString(this.getProvincia()));
				count++;
			}
			if(insertarString(this.getSegmentacion())!=null){
				jsonQbe.put("Segmentacion", insertarString(this.getSegmentacion()));
				count++;
			}
			if(tarea==TipoTarea.INSERTAR){
				jsonQbe.put("TieneDeuda", false);
				jsonQbe.put("TieneSiniestro", false);
			}
			if(insertarString(this.getTipo_documento())!=null){
				jsonQbe.put("TipoDocumento",insertarString(this.getTipo_documento()));
				count++;
			}
			if(tarea==TipoTarea.INSERTAR && this.getCliensec()!=null)
				jsonQbe.put("idAIS", insertarString(this.getCliensec()));
		if(count!=0){
			jsonCustomFields.put("Qbe", jsonQbe);
			jsonCustomFields.put("c", new JSONHandler());
			json.put("customFields", jsonCustomFields);
		}
		
	}
	
	private void insertarDisable(TipoTarea tarea){
		if(tarea==TipoTarea.INSERTAR)
			json.put("disabled", false);
	}
	
	private void insertarLogin(TipoTarea tarea){
		if(insertarString(this.getUser_name())!=null)
		json.put("login", this.getUser_name());
	}
	
	private void insertarName(TipoTarea tarea){
		JSONHandler jsonName = new JSONHandler();
		int count=0;
		if(insertarString(this.getNombre())!=null){
			jsonName.put("first", insertarString(this.getNombre()));
			count++;
		}
		if(insertarString(this.getApellido())!=null){
			jsonName.put("last", insertarString(this.getApellido()));
			count++;
		}
		if(count!=0)
			json.put("name", jsonName);
	}
	
	private void insertarEmails(){
		JSONArray arrayEmails= new JSONArray();
		if(hayMail(0)){
			JSONHandler emailPrincipal = new JSONHandler();
			emailPrincipal.put("address", getEmail_personal());
			JSONHandler addressType = new JSONHandler();
				addressType.put("lookupName", "Correo electrónico - Principal");
			emailPrincipal.put("addressType", addressType);
			emailPrincipal.put("invalid", false);
			arrayEmails.add(emailPrincipal);
		}
		if(hayMail(1)){
			JSONHandler emailLaboral = new JSONHandler();
			emailLaboral.put("address",getEmail_laboral());
			JSONHandler addressType = new JSONHandler();
				addressType.put("lookupName", "Correo electrónico alternativo 1");
				emailLaboral.put("addressType", addressType);
				emailLaboral.put("invalid", false);
				arrayEmails.add(emailLaboral);
		}
		if(hayMail(2)){
			JSONHandler emailAlternativo = new JSONHandler();
			emailAlternativo.put("address",getEmail_alternativo());
			JSONHandler addressType = new JSONHandler();
				addressType.put("lookupName", "Correo electrónico alternativo 2");
				emailAlternativo.put("addressType", addressType);
				emailAlternativo.put("invalid", false);	
				arrayEmails.add(emailAlternativo);
		}
		if(arrayEmails.size()>0)
			json.put("emails", arrayEmails);
		
	}
	
	private void insertarTelefonos(TipoTarea tarea){
		JSONArray arrayTelefono= new JSONArray();
		if(hayTelefono(0)){
			JSONHandler telefonoOficina = new JSONHandler();
			telefonoOficina.put("number", this.getTel_oficina());
			if(tarea==TipoTarea.UPDATE)
				telefonoOficina.put("rawNumber", this.getTel_oficina());
			JSONHandler phoneType = new JSONHandler();
				phoneType.put("lookupName", "Teléfono de oficina");
				telefonoOficina.put("phoneType", phoneType);
			arrayTelefono.add(telefonoOficina);
		}
		if(hayTelefono(4)){
			JSONHandler telefonoPersonal = new JSONHandler();
			telefonoPersonal.put("number", this.getTel_personal());
			if(tarea==TipoTarea.UPDATE)
				telefonoPersonal.put("rawNumber", this.getTel_personal());
			JSONHandler phoneType = new JSONHandler();
				phoneType.put("lookupName", "Teléfono particular");
				telefonoPersonal.put("phoneType", phoneType);
			arrayTelefono.add(telefonoPersonal);
		}
		if(arrayTelefono.size()>0)
			json.put("phones", arrayTelefono);
	}
	
	private void insertarOrganizacion(TipoTarea tarea){
		if(tarea==TipoTarea.INSERTAR)
			json.put("organization", null);
	}
	
	private void insertarTitle(TipoTarea tarea){
		if(tarea==TipoTarea.INSERTAR)
			json.put("title", null);

	}
	
	private boolean hayMail(int i){
		if(i==0)
			return this.getEmail_personal()!=null && this.getEmail_personal().length()>0;
		if(i==1)
			return this.getEmail_laboral()!=null && this.getEmail_laboral().length()>0;
		if(i==2)
			return this.getEmail_alternativo()!=null && this.getEmail_alternativo().length() > 0;
		return false;
	}
	
	private boolean hayTelefono(int i){
		if(i==0)
			return this.getTel_oficina()!=null && this.getTel_oficina().length()>0;
		if(i==4)
			return this.getTel_personal()!=null && this.getTel_personal().length()>0;
		return false;
	}
	


	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getCliensec() {
		return cliensec;
	}

	public void setCliensec(String cliensec) {
		this.cliensec = cliensec;
	}

	public String getTipoorganizacion() {
		return tipoorganizacion;
	}

	public void setTipoorganizacion(String tipoorganizacion) {
		this.tipoorganizacion = tipoorganizacion;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getId_tipo_documento() {
		return id_tipo_documento;
	}

	public void setId_tipo_documento(String id_tipo_documento) {
		this.id_tipo_documento = id_tipo_documento;
	}

	public String getTipo_documento() {
		return tipo_documento;
	}

	public void setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
	}

	public String getNro_documento() {
		return nro_documento;
	}

	public void setNro_documento(String nro_documento) {
		this.nro_documento = nro_documento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getFechanacimiento() {
		return fechanacimiento;
	}

	public void setFechanacimiento(String fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getSegmentacion() {
		return segmentacion;
	}

	public void setSegmentacion(String segmentacion) {
		this.segmentacion = segmentacion;
	}

	public String getConflictivo() {
		return conflictivo;
	}

	public void setConflictivo(String conflictivo) {
		this.conflictivo = conflictivo;
	}

	public String getEmail_personal() {
		return email_personal;
	}

	public void setEmail_personal(String email_personal) {
		this.email_personal = email_personal;
	}

	public String getEmail_laboral() {
		return email_laboral;
	}

	public void setEmail_laboral(String email_laboral) {
		this.email_laboral = email_laboral;
	}

	public String getEmail_alternativo() {
		return email_alternativo;
	}

	public void setEmail_alternativo(String email_alternativo) {
		this.email_alternativo = email_alternativo;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getFecha_alta() {
		return fecha_alta;
	}

	public void setFecha_alta(String fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	@Override
	public String getLine() {
		return line;
	}
	@Override
	public void setLine(String line) {
		this.line = line;
	}
	public String getTel_oficina() {
		return tel_oficina;
	}
	public void setTel_oficina(String tel_laboral) {
		this.tel_oficina = tel_laboral;
	}
	public String getTel_personal() {
		return tel_personal;
	}
	public void setTel_personal(String tel_personal) {
		this.tel_personal = tel_personal;
	}

	@Override
	public String getPathError() {
		return this.pathError;
	}
	
	
	

}

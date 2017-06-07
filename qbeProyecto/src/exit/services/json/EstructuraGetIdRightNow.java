package exit.services.json;

import exit.services.excepciones.ExceptionEstadoInvalido;
import exit.services.excepciones.ExceptionFormatoFecha;
import exit.services.excepciones.ExceptionIDNoNumerico;
import exit.services.excepciones.ExceptionIDNullIncidente;
import exit.services.excepciones.ExceptionLongitud;
import exit.services.excepciones.ExceptionModoContactoInvalido;
import exit.services.excepciones.ExceptionTipoIncidenteInvalido;

public class EstructuraGetIdRightNow {
	
	String line;
	String id;
	String idDeAIS;
	String nroSac;
	String modoContacto;
	String causa;
	String producto;
	String motivo; 
	String estado;
	String sector_responsable;
	String hilo1;
	String hilo2;


	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line=line;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdDeAIS() {
		return idDeAIS;
	}

	public void setIdDeAIS(String idDeAIS) {
		this.idDeAIS = idDeAIS;
	}

	public String getNroSac() {
		return nroSac;
	}

	public void setNroSac(String nroSac) {
		this.nroSac = nroSac;
	}

	public String getModoContacto() {
		return modoContacto;
	}

	public void setModoContacto(String modoContacto) {
		this.modoContacto = modoContacto;
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

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSector_responsable() {
		return sector_responsable;
	}

	public void setSector_responsable(String sector_responsable) {
		this.sector_responsable = sector_responsable;
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

	
	
}

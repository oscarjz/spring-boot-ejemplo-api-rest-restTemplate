/**
 * 
 */
package com.example.login.models;

import java.util.List;

/**
 * @author oscar
 *
 */
public class Provincia {


	private Long id;
	private String nombre;
	private List<Centroide> centroide;

	/**
	 * 
	 */
	public Provincia() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Centroide> getCentroide() {
		return centroide;
	}
	public void setCentroide(List<Centroide> centroide) {
		this.centroide = centroide;
	}
	
	  @Override
	  public String toString() {
	    return "Provincia{" +
	        " id = " + id +
	        ", nombre='" + nombre + '\'' +
	        '}';
	  }
}

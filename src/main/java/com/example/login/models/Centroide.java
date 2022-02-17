/**
 * 
 */
package com.example.login.models;

/**
 * @author oscar
 *
 */
public class Centroide {


	private Long lat;
	
	private Long lon;

	/**
	 * 
	 */
	public Centroide() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getLat() {
		return lat;
	}

	public void setLat(Long lat) {
		this.lat = lat;
	}

	public Long getLon() {
		return lon;
	}

	public void setLon(Long lon) {
		this.lon = lon;
	}
	
	  @Override
	  public String toString() {
	    return "Centroide{" +
	        "lat=" + lat +
	        ", lon='" + lon + '\'' +
	        '}';
	  }
}

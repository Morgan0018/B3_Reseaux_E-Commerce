package db.airport.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Morgan
 */
public class Language implements Serializable {

	private String id;
	private String name;

	/**
	 * Empty constructor
	 */
	public Language() {
	}

	/**
	 * Full constructor
	 *
	 * @param id
	 * @param name
	 */
	public Language(String id, String name) {
		this.id = id;
		this.name = name;
	}

	//<editor-fold defaultstate="collapsed" desc="Getters&Setters">
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	//</editor-fold>

	/**
	 * 
	 * @return 
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 37 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Language other = (Language) obj;
		return Objects.equals(this.id, other.id);
	}

}

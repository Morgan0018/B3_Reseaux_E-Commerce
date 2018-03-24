package db.airport.models;

import java.io.Serializable;

/**
 * @author Morgan
 */
public class Employer implements Serializable {

	private int id;
	private String name;
	private String certificate;

	/**
	 * Empty constructor
	 */
	public Employer() {
	}

	/**
	 * New insert constructor
	 *
	 * @param name
	 * @param certificate
	 */
	public Employer(String name, String certificate) {
		this.name = name;
		this.certificate = certificate;
	}

	/**
	 * Select constructor
	 * @param id
	 * @param name
	 * @param certificate 
	 */
	public Employer(int id, String name, String certificate) {
		this.id = id;
		this.name = name;
		this.certificate = certificate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

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
		hash = 29 * hash + this.id;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Employer other = (Employer) obj;
		return this.id == other.id;
	}

}

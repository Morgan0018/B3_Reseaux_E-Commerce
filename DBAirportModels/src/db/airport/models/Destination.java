package db.airport.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Morgan
 */
public class Destination implements Serializable {

	private String Id;
	private int distance;
	private String RefZone;

	/**
	 * Empty constructor.
	 */
	public Destination() {}

	/**
	 * Constructor.
	 *
	 * @param Id : destination id (name)
	 * @param distance
	 * @param RefZone : zone id (code)
	 */
	public Destination(String Id, int distance, String RefZone) {
		this.Id = Id;
		this.distance = distance;
		this.RefZone = RefZone;
	}


	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getRefZone() {
		return RefZone;
	}

	public void setRefZone(String RefZone) {
		this.RefZone = RefZone;
	}

	/**
	 *
	 * @return id (name)
	 */
	@Override
	public String toString() {
		return Id;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Objects.hashCode(this.Id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Destination other = (Destination) obj;
		return Objects.equals(this.Id, other.Id);
	}

}

package db.airport.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Morgan
 */
public class Zone implements Serializable {

	private String Id;

	/**
	 * Empty constructor.
	 */
	public Zone() {}

	/**
	 * Constructor.
	 *
	 * @param Id : zone id (code)
	 */
	public Zone(String Id) {
		this.Id = Id;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	/**
	 *
	 * @return id (code)
	 */
	@Override
	public String toString() {
		return Id;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.Id);
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
		final Zone other = (Zone) obj;
		return Objects.equals(this.Id, other.Id);
	}

}

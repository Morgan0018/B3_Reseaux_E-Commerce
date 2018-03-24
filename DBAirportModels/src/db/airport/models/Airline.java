package db.airport.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Morgan
 */
public class Airline implements Serializable {

	private String Id;

	/**
	 * Empty constructor.
	 */
	public Airline() {}

	/**
	 * Constructor.
	 *
	 * @param Id : airline id (name)
	 */
	public Airline(String Id) {
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
		final Airline other = (Airline) obj;
		return Objects.equals(this.Id, other.Id);
	}

}

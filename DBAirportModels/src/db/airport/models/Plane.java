package db.airport.models;

import java.io.Serializable;

/**
 * @author Morgan
 */
public class Plane implements Serializable {

	private int id;
	private boolean checkOk;
	private int nbSeats;

	/**
	 * Empty constructor.
	 */
	public Plane() {
	}

	/**
	 * New insert constructor.
	 *
	 * @param checkOk : if the plane is airworthy or not
	 * @param nbSeats : number of seats in the plane
	 */
	public Plane(boolean checkOk, int nbSeats) {
		this.checkOk = checkOk;
		this.nbSeats = nbSeats;
	}

	/**
	 * Select constructor.
	 *
	 * @param id : id of the plane
	 * @param checkOk : if the plane is airworthy or not
	 * @param nbSeats : number of seats in the plane
	 */
	public Plane(int id, boolean checkOk, int nbSeats) {
		this.id = id;
		this.checkOk = checkOk;
		this.nbSeats = nbSeats;
	}

	public boolean isCheckOk() {
		return checkOk;
	}

	public void setCheckOk(boolean checkOk) {
		this.checkOk = checkOk;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNbSeats() {
		return nbSeats;
	}

	public void setNbSeats(int nbSeats) {
		this.nbSeats = nbSeats;
	}

	/**
	 *
	 * @return id & check ok & nbSeats
	 */
	@Override
	public String toString() {
		String ok = checkOk ? "OK" : "FAIL";
		return "Avion " + id + " : " + nbSeats + " seats (check " + ok + ")";
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + this.id;
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
		final Plane other = (Plane) obj;
		return this.id == other.id;
	}

}

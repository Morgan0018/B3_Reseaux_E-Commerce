package db.airport.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Morgan
 */
public class Ticket implements Serializable {

	private String id;
	private String refPassenger;
	private Passenger passenger;
	private int refFlight;
	private Flight flight;

	/**
	 * Empty constructor.
	 */
	public Ticket() {}

	/**
	 * New insert constructor.
	 * 
	 * @param refPassenger
	 * @param refFlight 
	 */
	public Ticket(String refPassenger, int refFlight) {
		this.refPassenger = refPassenger;
		this.refFlight = refFlight;
	}

	/**
	 * Full constructor.
	 *
	 * @param id : ticket id (generated from flight id and departure date)
	 * @param refPassenger : Passenger Id
	 * @param refFlight : Id of flight the ticket is for
	 */
	public Ticket(String id, String refPassenger, int refFlight) {
		this.id = id;
		this.refPassenger = refPassenger;
		this.refFlight = refFlight;
	}

	public int getRefFlight() {
		return refFlight;
	}

	public void setRefFlight(int refFlight) {
		this.refFlight = refFlight;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public String getRefPassenger() {
		return refPassenger;
	}

	public void setRefPassenger(String refPassenger) {
		this.refPassenger = refPassenger;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	/**
	 *
	 * @return id
	 */
	@Override
	public String toString() {
		return id;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.id);
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
		final Ticket other = (Ticket) obj;
		return Objects.equals(this.id, other.id);
	}

}

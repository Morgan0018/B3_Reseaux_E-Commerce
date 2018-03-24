package db.airport.models;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Morgan
 */
public class Flight implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Variables">
	private int id;
	private String refDestination;
	private Destination destination;
	private String refAirline;
	private Airline airline;
	private Date departureDate;
	private String departureTime;
	private String arrivalTime;
	private String eta;
	private int refPlane;
	private Plane plane;
	private float price;
    // </editor-fold>

	/**
	 * Empty constructor.
	 */
	public Flight() {}

	/**
	 * New insert constructor.
	 *
	 * @param id : flight id (a 3-digits number)
	 * @param refDestination : the flight refDestination
	 * @param refAirline : the refAirline operating the flight
	 * @param departureDate : date of departure
	 * @param departureTime : time of departure
	 * @param eta : estimated time of arrival
	 * @param refPlane : the plane used for the flight
	 */
	public Flight(int id, String refDestination, String refAirline,
		Date departureDate, String departureTime, String eta, int refPlane) {
		this.id = id;
		this.refDestination = refDestination;
		this.refAirline = refAirline;
		this.departureDate = departureDate;
		this.departureTime = departureTime;
		this.eta = eta;
		this.refPlane = refPlane;
	}

	/**
	 * Select constructor
	 *
	 * @param id : flight id (a 3-digits number)
	 * @param refDestination : the flight refDestination
	 * @param refAirline : the refAirline operating the flight
	 * @param departureDate : date of departure
	 * @param departureTime : time of departure
	 * @param arrivalTime : real time of arrival
	 * @param eta : estimated time of arrival
	 * @param refPlane : the plane used for the flight
	 * @param price
	 */
	public Flight(int id, String refDestination, String refAirline,
		Date departureDate, String departureTime, String arrivalTime,
		String eta, int refPlane, float price) {
		this.id = id;
		this.refDestination = refDestination;
		this.refAirline = refAirline;
		this.departureDate = departureDate;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.eta = eta;
		this.refPlane = refPlane;
		this.price = price;
	}

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
	public int getRefPlane() {
		return refPlane;
	}

	public void setRefPlane(int refPlane) {
		this.refPlane = refPlane;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRefDestination() {
		return refDestination;
	}

	public void setRefDestination(String refDestination) {
		this.refDestination = refDestination;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getRefAirline() {
		return refAirline;
	}

	public void setRefAirline(String refAirline) {
		this.refAirline = refAirline;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public Airline getAirline() {
		return airline;
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
    // </editor-fold>
    
	/**
	 *
	 * @return id & refAirline & refDestination & departureTime
	 */
	@Override
	public String toString() {
		//String d = (new SimpleDateFormat("dd MMMM yyyy")).format(departureDate);
		return "Vol " + id + " " + refAirline + " - " + refDestination + " " + departureTime;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + this.id;
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
		final Flight other = (Flight) obj;
		return this.id == other.id;
	}

}

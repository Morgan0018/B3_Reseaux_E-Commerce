package db.airport.models;

import db.airport.models.Ticket;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Morgan
 */
public class Luggage implements Serializable {

	private String id;
	private boolean suitcase;
	private float weight;
	private boolean received;
	private char loaded;
	private boolean customChecked;
	private String comments;
	private String refTicket;
	private Ticket ticket;

	/**
	 * Empty constructor.
	 */
	public Luggage() {
	}

	/**
	 * New insert constructor
	 *
	 * @param id : luggage id (generated from ticket id and passenger name)
	 * @param suitcase : if the luggage is a suitcase or not
	 * @param weight : the luggage's weight
	 * @param refTicket : the id of the ticket this luggage is linked to
	 */
	public Luggage(String id, boolean suitcase, float weight, String refTicket) {
		this.id = id;
		this.suitcase = suitcase;
		this.weight = weight;
		this.refTicket = refTicket;
	}

	/**
	 * Select constructor
	 *
	 * @param id : luggage id (generated from ticket id and passenger name)
	 * @param suitcase : if the luggage is a suitcase or not
	 * @param weight : the luggage's weight
	 * @param received : whether the luggage has been received by the luggage handler
	 * @param loaded : whether the luggage has been loaded in the plane yet or refused (accepted values : 'O', 'N', 'R')
	 * @param customChecked : wether the plane has been checked by custom
	 * @param comments : comments concerning the luggage
	 * @param refTicket : the id of the ticket this luggage is linked to
	 */
	public Luggage(String id, boolean suitcase, float weight, boolean received, char loaded, boolean customChecked, String comments, String refTicket) {
		this.id = id;
		this.suitcase = suitcase;
		this.weight = weight;
		this.received = received;
		this.loaded = loaded;
		this.customChecked = customChecked;
		this.comments = comments;
		this.refTicket = refTicket;
	}

	public String getRefTicket() {
		return refTicket;
	}

	public void setRefTicket(String refTicket) {
		this.refTicket = refTicket;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSuitcase() {
		return suitcase;
	}

	public void setSuitcase(boolean suitcase) {
		this.suitcase = suitcase;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public char getLoaded() {
		return loaded;
	}

	public void setLoaded(char loaded) {
		this.loaded = loaded;
	}

	public boolean isCustomChecked() {
		return customChecked;
	}

	public void setCustomChecked(boolean customChecked) {
		this.customChecked = customChecked;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
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
		hash = 97 * hash + Objects.hashCode(this.id);
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
		final Luggage other = (Luggage) obj;
		return Objects.equals(this.id, other.id);
	}

}

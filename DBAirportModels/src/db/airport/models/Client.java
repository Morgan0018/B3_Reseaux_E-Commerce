package db.airport.models;

import java.io.Serializable;

/**
 * @author Morgan
 */
public class Client implements Serializable {

	private int id;
	private String login;
	private String password;
	private String refPassenger;
	private Passenger passenger;

	/**
	 * Empty constructor.
	 */
	public Client() {
	}

	/**
	 * New insert constructor.
	 *
	 * @param login
	 * @param password
	 * @param refPassenger
	 */
	public Client(String login, String password, String refPassenger) {
		this.login = login;
		this.password = password;
		this.refPassenger = refPassenger;
	}

	/**
	 * Select constructor.
	 * 
	 * @param id
	 * @param login
	 * @param password
	 * @param refPassenger
	 */
	public Client(int id, String login, String password, String refPassenger) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.refPassenger = refPassenger;
	}

	public String getRefPassenger() {
		return refPassenger;
	}

	public void setRefPassenger(String refPassenger) {
		this.refPassenger = refPassenger;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "Client{" + "id=" + id + ", login=" + login + ", refPassenger=" + refPassenger + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
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
		final Client other = (Client) obj;
		return this.id == other.id;
	}

}

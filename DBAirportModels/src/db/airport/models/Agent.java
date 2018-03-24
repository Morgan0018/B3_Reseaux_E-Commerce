package db.airport.models;

import java.io.Serializable;

/**
 * @author Morgan
 */
public class Agent implements Serializable {

	private int id;
	private String login;
	private String password;
	private int worksFor;
	private Employer employer;

	/**
	 * Empty constructor.
	 */
	public Agent() {}

	/**
	 * New insert constructor.
	 *
	 * @param login : agent login
	 * @param password : agent password
	 */
	public Agent(String login, String password) {
		this.login = login;
		this.password = password;
	}

	/**
	 * Select constructor.
	 *
	 * @param id : agent id
	 * @param login : agent login
	 * @param password : agent password
	 * @param worksFor
	 */
	public Agent(int id, String login, String password, int worksFor) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.worksFor = worksFor;
	}

	//<editor-fold defaultstate="collapsed" desc="Getters&Setters">
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
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
	
	public int getWorksFor() {
		return worksFor;
	}
	
	public void setWorksFor(int worksFor) {
		this.worksFor = worksFor;
	}
	
	public Employer getEmployer() {
		return employer;
	}
	
	public void setEmployer(Employer employer) {
		this.employer = employer;
	}
	//</editor-fold>
	/**
	 *
	 * @return id - login
	 */
	@Override
	public String toString() {
		return id + " - " + login;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + this.id;
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
		final Agent other = (Agent) obj;
		return this.id == other.id;
	}

}

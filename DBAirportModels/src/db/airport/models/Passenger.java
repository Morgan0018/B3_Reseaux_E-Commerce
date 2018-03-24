package db.airport.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Morgan
 */
public class Passenger implements Serializable {

	private String Id;
	private String lastName;
	private String firstName;
	private String numId;
	private int age;
	private char gender;

	/**
	 * Empty constructor.
	 */
	public Passenger() {}

	/**
	 * New insert constructor.
	 *
	 * @param lastName : Last name of passenger
	 * @param firstName : First name of passenger
	 * @param age : Age of passenger
	 * @param gender : Gender of passenger (M or F)
	 */
	public Passenger(String lastName, String firstName, int age, char gender) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.age = age;
		this.gender = gender;
	}

	/**
	 * Select constructor.
	 *
	 * @param Id : Passenger id (first 2 letters of first name + last name)
	 * @param lastName : Last name of passenger
	 * @param firstName : First name of passenger
	 * @param numId : Number of passenger Identification (ex: Passport, Id card, ...)
	 * @param age : Age of passenger
	 * @param gender : Gender of passenger (M or F)
	 */
	public Passenger(String Id, String lastName, String firstName, String numId, int age, char gender) {
		this.Id = Id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.numId = numId;
		this.age = age;
		this.gender = gender;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getNumId() {
		return numId;
	}

	public void setNumId(String numId) {
		this.numId = numId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	/**
	 *
	 * @return last name & first name
	 */
	@Override
	public String toString() {
		return lastName + ", " + firstName;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(this.Id);
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
		final Passenger other = (Passenger) obj;
		return Objects.equals(this.Id, other.Id);
	}

}

package db.decisions.models;

import java.io.Serializable;

/**
 * @author Morgan
 */
public class Statistics implements Serializable {

	private int id;
	private String title;
	private String results;

	/**
	 * Empty constructor.
	 */
	public Statistics() {}

	/**
	 * New insert constructor.
	 * 
	 * @param title
	 * @param results
	 */
	public Statistics(String title, String results) {
		this.title = title;
		this.results = results;
	}

	/**
	 * Select constructor.
	 * 
	 * @param id
	 * @param title
	 * @param results
	 */
	public Statistics(int id, String title, String results) {
		this.id = id;
		this.title = title;
		this.results = results;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "Statistics{" + "title=" + title + ", results=" + results + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
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
		final Statistics other = (Statistics) obj;
		return this.id == other.id;
	}

}

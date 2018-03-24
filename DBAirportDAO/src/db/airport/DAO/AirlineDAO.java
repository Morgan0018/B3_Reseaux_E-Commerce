package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import database.utilities.BeanDBAccess;
import db.airport.models.Airline;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class AirlineDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public AirlineDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes an Airline from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 * @return a db.airport.models.Airline
	 * @throws SQLException
	 */
	private Airline resultSetToAirline(ResultSet rs) throws SQLException {
		return new Airline(rs.getString("Id"));
	}

	/**
	 * collects all the airlines
	 *
	 * @return an ArrayList of Airline
	 */
	public ArrayList<Airline> selectAll() {
		try {
			db.setResultSet(db.ExecuteQuery("SELECT * FROM airlines;"));
			ArrayList<Airline> airlines = new ArrayList<>();
			while (db.getResultSet().next()) {
				airlines.add(resultSetToAirline(db.getResultSet()));
			}
			return airlines;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

}

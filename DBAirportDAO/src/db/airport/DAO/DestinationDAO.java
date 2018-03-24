package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import database.utilities.BeanDBAccess;
import db.airport.models.Destination;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class DestinationDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public DestinationDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Destination from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 * @return a db.airport.models.Agent
	 * @throws SQLException
	 */
	private Destination resultSetDestination(ResultSet rs) throws SQLException {
		return new Destination(rs.getString("Id"), rs.getInt("Distance"),
			rs.getString("RefZone"));
	}

	/**
	 * collects all the Destinations
	 *
	 * @return an ArrayList of Destination
	 */
	public ArrayList<Destination> selectAll() {
		try {
			db.setResultSet(db.ExecuteQuery("SELECT * FROM destination;"));
			ArrayList<Destination> airlines = new ArrayList<>();
			while (db.getResultSet().next()) {
				airlines.add(resultSetDestination(db.getResultSet()));
			}
			return airlines;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

}

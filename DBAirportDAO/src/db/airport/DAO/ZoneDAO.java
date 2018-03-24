package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import database.utilities.BeanDBAccess;
import db.airport.models.Zone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class ZoneDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public ZoneDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Zone from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 * @return a db.airport.models.Zone
	 * @throws SQLException
	 */
	private Zone resultSetToZone(ResultSet rs) throws SQLException {
		return new Zone(rs.getString("Id"));
	}

	/**
	 * Collects all the zones
	 * 
	 * @return an ArrayList of Zone
	 */
	public ArrayList<Zone> selectAll() {
		try {
			db.setResultSet(db.ExecuteQuery("SELECT * FROM zones;"));
			ArrayList<Zone> zones = new ArrayList<>();
			while (db.getResultSet().next()) {
				zones.add(resultSetToZone(db.getResultSet()));
			}
			return zones;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

}

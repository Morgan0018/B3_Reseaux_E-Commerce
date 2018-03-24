package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import database.utilities.BeanDBAccess;
import db.airport.models.Plane;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Morgan
 */
public class PlaneDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public PlaneDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Plane from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 * @return a db.airport.models.Plane
	 * @throws SQLException
	 */
	private Plane resultSetToPlane(ResultSet rs) throws SQLException {
		return new Plane(rs.getInt("Id"), rs.getBoolean("Check_OK"),
			rs.getInt("NbSeats"));
	}

}

package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import db.airport.models.Luggage;
import database.utilities.BeanDBAccess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class LuggageDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public LuggageDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Luggage from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 * @return a db.airport.models.Luggage
	 * @throws SQLException
	 */
	private Luggage resultSetToLuggage(ResultSet rs) throws SQLException {
		return new Luggage(rs.getString("Id"), rs.getBoolean("Suitcase"),
			rs.getFloat("Weight"), rs.getBoolean("Received"),
			rs.getString("Loaded").charAt(0), rs.getBoolean("CustomChecked"),
			rs.getString("Comments"), rs.getString("RefTicket"));
	}

	/**
	 * Collect all the luggage for a specific flight from the database
	 *
	 * @param idFlight : the id of the flight the luggage are on
	 * @return an ArrayList of Luggage
	 */
	public ArrayList<Luggage> selectAllForFlight(int idFlight) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT * FROM luggages WHERE RefTicket IN "
				+ "(SELECT Id FROM tickets WHERE RefFlight = ?);"));
			db.getPreparedStatement().setInt(1, idFlight);
			db.setResultSet(db.getPreparedStatement().executeQuery());

			ArrayList<Luggage> luggages = new ArrayList<>();
			while (db.getResultSet().next()) {
				luggages.add(resultSetToLuggage(db.getResultSet()));
			}
			return luggages;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 * 
	 * @param l
	 * @return 
	 */
	private synchronized boolean insertNew(Luggage l) throws SQLException {
		int executeUpdate = 0;
		db.setPreparedStatement(db.prepareStatement(
			"INSERT INTO Luggages (Id, Suitcase, Weight, RefTicket) VALUES (?, ?, ?, ?);"));
		db.getPreparedStatement().setString(1, l.getId());
		db.getPreparedStatement().setBoolean(2, l.isSuitcase());
		db.getPreparedStatement().setFloat(3, l.getWeight());
		db.getPreparedStatement().setString(4, l.getRefTicket());
		executeUpdate = db.getPreparedStatement().executeUpdate();
		return executeUpdate != 0;
	}
	
	/**
	 * 
	 * @param luggages
	 * @return 
	 */
	public synchronized boolean insertMultipleNew(ArrayList<Luggage> luggages) {
		try {
			boolean insertOK = false;
			for (Luggage luggage : luggages) {
				insertOK = insertNew(luggage);
				if (!insertOK) break;
			}
			if (insertOK) db.commit();
			else db.getConnection().rollback();
			return insertOK;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return false;
		}
	}
	
	/**
	 * Updates the field "received" of a specific luggage to the value provided
	 *
	 * @param id : the id of the luggage
	 * @param received : the new value of the field "received"
	 * @return the success of the update operation or not
	 */
	public synchronized boolean updateReceived(String id, boolean received) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE luggages SET Received = ? WHERE Id = ? ;"));
			db.getPreparedStatement().setBoolean(1, received);
			db.getPreparedStatement().setString(2, id);
			executeUpdate = db.getPreparedStatement().executeUpdate();
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return executeUpdate != 0;
	}

	/**
	 * Updates the field "customCheck" of a specific luggage to the value provided
	 *
	 * @param id : the id of the luggage
	 * @param checked : the new value of the field "customChecked"
	 * @return the success of the update operation or not
	 */
	public synchronized boolean updateCustomCheck(String id, boolean checked) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE luggages SET CustomChecked = ? WHERE Id = ? ;"));
			db.getPreparedStatement().setBoolean(1, checked);
			db.getPreparedStatement().setString(2, id);
			executeUpdate = db.getPreparedStatement().executeUpdate();
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return executeUpdate != 0;
	}

	/**
	 * Updates the field "received" of a specific luggage to the value provided
	 *
	 * @param id : the id of the luggage
	 * @param loaded : the new value of the field "loaded"
	 * @return the success of the update operation or not
	 */
	public synchronized boolean updateLoaded(String id, char loaded) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE luggages SET Loaded = ? WHERE Id = ? ;"));
			db.getPreparedStatement().setString(1, loaded + "");
			db.getPreparedStatement().setString(2, id);
			executeUpdate = db.getPreparedStatement().executeUpdate();
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return executeUpdate != 0;
	}

	/**
	 * Updates the field "received" of a specific luggage to the value provided
	 *
	 * @param id : the id of the luggage
	 * @param comment : the new value of the field "comments"
	 * @return the success of the update operation or not
	 */
	public synchronized boolean updateComment(String id, String comment) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE luggages SET Comments = ? WHERE Id = ? ;"));
			if ("".equals(comment))
				db.getPreparedStatement().setNull(1, java.sql.Types.VARCHAR);
			else db.getPreparedStatement().setString(1, comment);
			db.getPreparedStatement().setString(2, id);
			executeUpdate = db.getPreparedStatement().executeUpdate();
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return executeUpdate != 0;
	}

	/**
	 *
	 * @param luggages
	 * @return
	 */
	public synchronized boolean bulkUpdateReceivedAndLoaded(ArrayList<Luggage> luggages) {
		for (Luggage luggage : luggages) {
			if (!updateReceivedAndLoaded(luggage)) return false;
		}
		return true;
	}

	/**
	 *
	 * @param luggage
	 * @return
	 */
	public synchronized boolean updateReceivedAndLoaded(Luggage luggage) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE luggages SET Received = ?, Loaded = ? WHERE Id = ? ;"));
			db.getPreparedStatement().setBoolean(1, luggage.isReceived());
			db.getPreparedStatement().setString(2, luggage.getLoaded()+"");
			db.getPreparedStatement().setString(3, luggage.getId());
			executeUpdate = db.getPreparedStatement().executeUpdate();
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return executeUpdate != 0;
	}

}

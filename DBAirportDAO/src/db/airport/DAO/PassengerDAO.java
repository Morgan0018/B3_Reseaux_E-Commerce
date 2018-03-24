package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import database.utilities.BeanDBAccess;
import db.airport.models.Passenger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class PassengerDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public PassengerDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Passenger from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 * @return a db.airport.models.Passenger
	 * @throws SQLException
	 */
	private Passenger resultSetToPassenger(ResultSet rs) throws SQLException {
		return new Passenger(rs.getString("Id"), rs.getString("LastName"),
			rs.getString("FirstName"), rs.getString("NumId"), rs.getInt("Age"),
			rs.getString("Gender").charAt(0));
	}

	/**
	 * 
	 * @return 
	 */
	public ArrayList<Passenger> selectAll() {
		try {
			db.setPreparedStatement(db.prepareStatement("SELECT * FROM passengers;"));
			db.setResultSet(db.getPreparedStatement().executeQuery());
			ArrayList<Passenger> passengers = new ArrayList<>();
			while (db.getResultSet().next()) {				
				passengers.add(resultSetToPassenger(db.getResultSet()));
			}
			return passengers;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return 
	 */
	public Passenger selectOne(String id) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT * FROM passengers WHERE Id = ? ;"));
			db.getPreparedStatement().setString(1, id);
			db.setResultSet(db.getPreparedStatement().executeQuery());
			db.getResultSet().next();
			return resultSetToPassenger(db.getResultSet());
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}
	
	/**
	 * 
	 * @param p
	 * @return 
	 */
	public synchronized boolean insertNew(Passenger p) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"INSERT INTO Passengers (LastName, FirstName, NumId, Age, Gender)"
					+ " VALUES (?, ?, ?, ?, ?);"));
			db.getPreparedStatement().setString(1, p.getLastName());
			db.getPreparedStatement().setString(2, p.getFirstName());
			db.getPreparedStatement().setString(3, p.getNumId());
			db.getPreparedStatement().setInt(4, p.getAge());
			db.getPreparedStatement().setString(5, p.getGender()+"");
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
	 * @param id
	 * @param numId
	 * @return 
	 */
	public synchronized boolean updateNumId(String id, String numId) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE passengers SET NumId = ? WHERE Id = ? ;"));
			db.getPreparedStatement().setString(1, numId);
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
	 * @param id
	 * @return 
	 */
	public synchronized boolean deletePassenger(String id) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"DELETE FROM passengers WHERE Id = ? ;"));
			db.getPreparedStatement().setString(1, id);
			executeUpdate = db.getPreparedStatement().executeUpdate();
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return executeUpdate != 0;
	}
	
}

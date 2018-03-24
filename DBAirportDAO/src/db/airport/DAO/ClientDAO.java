package db.airport.DAO;

import database.utilities.BeanDBAccess;
import database.utilities.DAO.AbstractDAO;
import db.airport.models.Client;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Morgan
 */
public class ClientDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public ClientDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Client from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 *
	 * @return a db.airport.models.Client
	 *
	 * @throws SQLException
	 */
	private Client resultSetToClient(ResultSet rs) throws SQLException {
		return new Client(rs.getInt("Id"), rs.getString("Login"),
			rs.getString("Password"), rs.getString("RefPassenger"));
	}

	/**
	 * Returns the client corresponding to the provided "login" string.
	 *
	 * @param login : the login for the client
	 *
	 * @return a db.airport.models.Client
	 */
	public synchronized Client selectOne(String login) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT * FROM clients WHERE Login = ? ;"));
			db.getPreparedStatement().setString(1, login);
			db.setResultSet(db.getPreparedStatement().executeQuery());
			db.getResultSet().next();
			return resultSetToClient(db.getResultSet());
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 *
	 * @param client
	 *
	 * @return
	 */
	public synchronized int insertNewClient(Client client) {
		int last_insert_id = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"INSERT INTO Clients (Login, Password, RefPassenger) VALUES (?, ?, ?);"));
			db.getPreparedStatement().setString(1, client.getLogin());
			db.getPreparedStatement().setString(2, client.getPassword());
			db.getPreparedStatement().setString(3, client.getRefPassenger());
			if (db.getPreparedStatement().executeUpdate() > 0) {
				db.setResultSet(db.ExecuteQuery("SELECT last_insert_id() AS 'Id';"));
				db.getResultSet().next();
				last_insert_id = db.getResultSet().getInt("Id");
			}
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return last_insert_id;
	}

	/**
	 *
	 * @param id
	 * @param passengerId
	 *
	 * @return
	 */
	public synchronized boolean updatePassenger(int id, String passengerId) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE clients SET RefPassenger = ? WHERE Id = ? ;"));
			db.getPreparedStatement().setString(1, passengerId);
			db.getPreparedStatement().setInt(2, id);
			executeUpdate = db.getPreparedStatement().executeUpdate();
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return executeUpdate != 0;
	}

}

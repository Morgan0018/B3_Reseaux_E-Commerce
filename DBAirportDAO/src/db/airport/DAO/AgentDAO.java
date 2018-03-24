package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import db.airport.models.Agent;
import database.utilities.BeanDBAccess;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Morgan
 */
public class AgentDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public AgentDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Luggage from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 * @return a db.airport.models.Agent
	 * @throws SQLException
	 */
	private Agent resultSetToAgent(ResultSet rs) throws SQLException {
		return new Agent(rs.getInt("Id"), rs.getString("Login"),
			rs.getString("Password"), rs.getInt("WorksFor"));
	}

	/**
	 * Returns the agent corresponding to the provided "login" string.
	 *
	 * @param login : the login for the agent
	 * @return a db.airport.models.Agent
	 */
	public Agent selectOne(String login) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT * FROM agents WHERE Login = ? ;"));
			db.getPreparedStatement().setString(1, login);
			db.setResultSet(db.getPreparedStatement().executeQuery());
			db.getResultSet().next();
			return resultSetToAgent(db.getResultSet());
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

}

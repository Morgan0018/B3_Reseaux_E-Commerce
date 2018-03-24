package database.utilities;

import java.io.Serializable;
import java.sql.*;

/**
 * @author Morgan
 */
public class BeanDBAccess implements Serializable {

	protected Connection connection;
	protected Statement statement;
	protected PreparedStatement preparedStatement;
	protected ResultSet resultSet;

	/**
	 * Constructor : Loads the driver, opens the connection and creates a
	 * Statement
	 *
	 * @param driver : Driver string for the Class.forName()
	 * @param database : Database connection info
	 * @param username : for database authentification
	 * @param pwd : for database authentification
	 * @param autoCommit : set the database connection to auto-commit or not
	 */
	public BeanDBAccess(String driver, String database, String username, String pwd, boolean autoCommit) {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(database, username, pwd);
			connection.setAutoCommit(autoCommit);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			System.err.println("Erreur : " + e.getClass() + " - Message : " + e.getMessage());
			//System.exit(1);
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage() + " / SQL STATE : " + e.getSQLState());
		}
	}

	// Getters - Setters
	/**
	 *
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 *
	 * @param connection
	 */
	protected void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 *
	 * @return
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 *
	 * @param statement
	 */
	protected void setStatement(Statement statement) {
		this.statement = statement;
	}

	/**
	 *
	 * @return
	 */
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	/**
	 *
	 * @param preparedStatement
	 */
	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}

	/**
	 *
	 * @return
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 *
	 * @param resultSet
	 */
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * Executes the query through the Statement.executeQuery() method
	 *
	 * @param query : A string containing an SQL query
	 * @return The query's result
	 */
	public ResultSet ExecuteQuery(String query) {
		try {
			resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (Exception e) {
			System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
			return null;
		}
	}

	/**
	 * Executes the query through the Statement.executeUpdate() method
	 *
	 * @param query : The query string
	 * @return
	 */
	public boolean ExecuteUpdate(String query) {
		try {
			if (statement.executeUpdate(query) != 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	/**
	 *
	 * @param query : A string containing an SQL query with ?
	 * @return A PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(String query) throws SQLException {
		return connection.prepareStatement(query);
	}

	/**
	 * Commits the transaction.
	 */
	public void commit() {
		try {
			connection.commit();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Closes the Connection.
	 */
	public void Close() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
}

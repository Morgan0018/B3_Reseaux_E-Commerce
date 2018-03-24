package db.airport.DAO;

import database.utilities.BeanDBAccess;
import database.utilities.DAO.AbstractDAO;
import db.airport.models.Employer;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Morgan
 */
public class EmployerDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db
	 */
	public EmployerDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private Employer resultSetToEmployer(ResultSet rs) throws SQLException {
		return new Employer(rs.getInt("Id"), rs.getString("Name"), rs.getString("Certificate"));
	}
	
	/**
	 * 
	 * @param agentLogin
	 * @return 
	 */
	public String getCertificateEntry(String agentLogin) {
		try {
			db.setPreparedStatement(db.prepareStatement("SELECT Certificate"
				+ " FROM employer JOIN agents ON employer.Id = WorksFor"
				+ " WHERE Login = ? ;"));
			db.getPreparedStatement().setString(1, agentLogin);
			db.setResultSet(db.getPreparedStatement().executeQuery());
			db.getResultSet().next();
			return db.getResultSet().getString("Certificate");
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}
	
}

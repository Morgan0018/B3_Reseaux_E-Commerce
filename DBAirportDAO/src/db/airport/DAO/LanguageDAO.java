package db.airport.DAO;

import database.utilities.BeanDBAccess;
import database.utilities.DAO.AbstractDAO;
import db.airport.models.Language;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class LanguageDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db
	 */
	public LanguageDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private Language resultSetToLanguage(ResultSet rs) throws SQLException {
		return new Language(rs.getString("Id"), rs.getString("Name"));
	}
	
	/**
	 * 
	 * @return 
	 */
	public ArrayList<Language> selectAll() {
		try {
			db.setPreparedStatement(db.prepareStatement("SELECT * FROM languages;"));
			db.setResultSet(db.getPreparedStatement().executeQuery());
			ArrayList<Language> languages = new ArrayList<>();
			while (db.getResultSet().next()) {
				languages.add(resultSetToLanguage(db.getResultSet()));
			}
			return languages;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}
	
}

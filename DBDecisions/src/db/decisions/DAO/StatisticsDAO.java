package db.decisions.DAO;

import database.utilities.BeanDBAccess;
import database.utilities.DAO.AbstractDAO;
import db.decisions.models.Statistics;
import java.sql.SQLException;

/**
 * @author Morgan
 */
public class StatisticsDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public StatisticsDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * 
	 * @param statistics
	 * @return
	 */
	public synchronized boolean insertOne(Statistics statistics) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"INSERT INTO Statistics (Title, Results) VALUES (?, ?);"));
			db.getPreparedStatement().setString(1, statistics.getTitle());
			db.getPreparedStatement().setString(2, statistics.getResults());
			int executeUpdate = db.getPreparedStatement().executeUpdate();
			if (executeUpdate > 0) return true;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
		}
		return false;
	}

}

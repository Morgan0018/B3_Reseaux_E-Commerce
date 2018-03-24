package db.decisions;

import database.utilities.DAO.AbstractDAOFactoryMySQL;
import db.decisions.DAO.StatisticsDAO;

/**
 * @author Morgan
 */
public class DAOFactoryDecision extends AbstractDAOFactoryMySQL {

	private static DAOFactoryDecision factoryInstance = null;

	private StatisticsDAO statisticsDAO;

	/**
	 * Constructor.
	 */
	private DAOFactoryDecision() {
		super("db_decisions");
	}

	/**
	 * 
	 * @return 
	 */
	public synchronized static DAOFactoryDecision getInstance() {
		if (factoryInstance == null) factoryInstance = new DAOFactoryDecision();
		return factoryInstance;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized StatisticsDAO getStatisticsDAO() {
		if (statisticsDAO == null) statisticsDAO = new StatisticsDAO(bDAccess);
		return statisticsDAO;
	}

}

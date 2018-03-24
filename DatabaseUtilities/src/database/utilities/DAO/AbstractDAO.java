package database.utilities.DAO;

import database.utilities.BeanDBAccess;

/**
 * @author Morgan
 */
public abstract class AbstractDAO {

	protected final BeanDBAccess db;

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public AbstractDAO(BeanDBAccess db) {
		this.db = db;
	}

	/**
	 *
	 * @return a database.utilities.BeanDBAccess
	 */
	public BeanDBAccess getDb() {
		return db;
	}

}

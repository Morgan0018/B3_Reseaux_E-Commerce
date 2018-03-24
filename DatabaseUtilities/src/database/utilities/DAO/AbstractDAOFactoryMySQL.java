package database.utilities.DAO;

import database.utilities.BeanDBAccess;

/**
 * @author Morgan
 */
public abstract class AbstractDAOFactoryMySQL {

	protected BeanDBAccess bDAccess;

	protected AbstractDAOFactoryMySQL(String dbName) {
		bDAccess = new BeanDBAccess("com.mysql.jdbc.Driver",
			"jdbc:mysql://localhost:3306/"+dbName, "root", "mysql", false);
	}

}

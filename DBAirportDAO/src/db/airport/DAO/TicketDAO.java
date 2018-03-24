package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import database.utilities.BeanDBAccess;
import db.airport.models.Ticket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class TicketDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public TicketDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Luggage from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 *
	 * @return a db.airport.models.Ticket
	 *
	 * @throws SQLException
	 */
	private Ticket resultSetToTicket(ResultSet rs) throws SQLException {
		return new Ticket(rs.getString("Id"), rs.getString("RefPassenger"),
			rs.getInt("RefFlight"));
	}

	/**
	 *
	 * @param ticket
	 *
	 * @return
	 */
	private synchronized boolean insertNew(Ticket ticket) throws SQLException {
		int executeUpdate = 0;
		db.setPreparedStatement(db.prepareStatement(
			"INSERT INTO Tickets (RefPassenger, RefFlight) VALUES (?, ?);"));
		db.getPreparedStatement().setString(1, ticket.getRefPassenger());
		db.getPreparedStatement().setInt(2, ticket.getRefFlight());
		executeUpdate = db.getPreparedStatement().executeUpdate();
		return executeUpdate != 0;
	}

	/**
	 *
	 * @param tickets
	 *
	 * @return
	 */
	public synchronized boolean insertMultipleNew(ArrayList<Ticket> tickets) {
		try {
			boolean insertOK = false;
			for (Ticket ticket : tickets) {
				insertOK = insertNew(ticket);
				if (!insertOK) break;
			}
			if (insertOK) db.commit();
			else db.getConnection().rollback();
			return insertOK;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return false;
		}
	}

	/**
	 *
	 * @param idPassenger
	 *
	 * @return
	 */
	public ArrayList<Ticket> selectNotPaidForPassenger(String idPassenger) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT * FROM tickets WHERE RefPassenger = ? AND Paid = FALSE;"));
			db.getPreparedStatement().setString(1, idPassenger);
			db.setResultSet(db.getPreparedStatement().executeQuery());

			ArrayList<Ticket> tickets = new ArrayList<>();
			while (db.getResultSet().next()) {
				tickets.add(resultSetToTicket(db.getResultSet()));
			}
			return tickets;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 *
	 * @param id
	 * @param paid
	 *
	 * @return
	 */
	public synchronized boolean updatePaid(String id, boolean paid) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE tickets SET Paid = ? WHERE Id = ? ;"));
			db.getPreparedStatement().setBoolean(1, paid);
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
	 * @param idPassenger
	 *
	 * @return
	 */
	public synchronized boolean updateAllPaidForPassenger(String idPassenger) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement(
				"UPDATE tickets SET Paid = TRUE WHERE RefPassenger = ? ;"));
			db.getPreparedStatement().setString(1, idPassenger);
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
	 * @param idFlight
	 * @param idPassenger
	 *
	 * @return
	 */
	public synchronized boolean deleteUnpaidTicketsForFlightAndPassenger(int idFlight, String idPassenger) {
		int executeUpdate = 0;
		try {
			db.setPreparedStatement(db.prepareStatement("DELETE FROM tickets "
				+ "WHERE Paid = FALSE AND RefFlight = ? AND RefPassenger = ? ;"));
			db.getPreparedStatement().setInt(1, idFlight);
			db.getPreparedStatement().setString(2, idPassenger);
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
	 * @param idTicket
	 *
	 * @return
	 */
	public Ticket selectOne(String idTicket) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT * FROM tickets WHERE Id = ? AND Paid = TRUE;"));
			db.getPreparedStatement().setString(1, idTicket);
			db.setResultSet(db.getPreparedStatement().executeQuery());

			db.getResultSet().next();
			return resultSetToTicket(db.getResultSet());
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 * 
	 * @param idPassenger
	 * @param idFlight
	 * @return 
	 */
	public int countForPassengerAndFlight(String idPassenger, int idFlight) {
		try {
			db.setPreparedStatement(db.prepareStatement("SELECT count(*) AS 'NB'"
				+ " FROM tickets WHERE RefPassenger = ? AND RefFlight = ? ;"));
			db.getPreparedStatement().setString(1, idPassenger);
			db.getPreparedStatement().setInt(2, idFlight);
			db.setResultSet(db.getPreparedStatement().executeQuery());
			db.getResultSet().next();
			return db.getResultSet().getInt("NB");
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return -1;
		}
	}
	
}

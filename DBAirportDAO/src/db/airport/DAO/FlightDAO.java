package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import db.airport.models.Flight;
import database.utilities.BeanDBAccess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Morgan
 */
public class FlightDAO extends AbstractDAO {

	private static final String NB_SEATS = "Available";
	private static final String NB_TICK = "Tickets";

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public FlightDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 * Creates and initializes a Flight from a ResultSet
	 *
	 * @param rs : a java.sql.ResultSet already positioned at the desired "line"
	 *
	 * @return a db.airport.models.Flight
	 *
	 * @throws SQLException
	 */
	private Flight resultSetToFlight(ResultSet rs) throws SQLException {
		return new Flight(rs.getInt("Id"), rs.getString("RefDestination"),
			rs.getString("RefAirline"), rs.getDate("DepartureDate"),
			rs.getString("DepartureTime"), rs.getString("ArrivalTime"),
			rs.getString("ETA"), rs.getInt("RefPlane"), rs.getFloat("Price"));
	}

	/**
	 * 
	 * @return 
	 */
	public Flight selectNextFlight() {
		try {
			db.setPreparedStatement(db.prepareStatement("SELECT * FROM flights "
				+ "WHERE DepartureDate > curdate() OR (DepartureDate = curdate()"
				+ " AND str_to_date(DepartureTime, '%Hh%i') > curtime()) "
				+ "ORDER BY DepartureDate, DepartureTime LIMIT 1;"));
			db.setResultSet(db.getPreparedStatement().executeQuery());
			db.getResultSet().next();
			return resultSetToFlight(db.getResultSet());
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}
	
	/**
	 * Collect all the flights for the day
	 *
	 * @return an ArrayList of Flights
	 */
	public ArrayList<Flight> selectTodaysFlights() {
		try {
			db.setPreparedStatement(db.prepareStatement("SELECT * FROM flights"
				+ " WHERE DepartureDate = curdate() ORDER BY DepartureTime;"));
			db.setResultSet(db.getPreparedStatement().executeQuery());

			ArrayList<Flight> flights = new ArrayList<>();
			while (db.getResultSet().next()) {
				flights.add(resultSetToFlight(db.getResultSet()));
			}
			return flights;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 * Collect the flights for 7 days stating at the current date
	 *
	 * @return an ArrayList of Flights
	 */
	public LinkedHashMap<Flight, Integer> select7DaysFlights() {
		try {
			db.setPreparedStatement(db.prepareStatement("SELECT flights.*, NbSeats-"
				+ "(SELECT count(*) FROM tickets WHERE RefFlight = flights.Id)"
				+ " AS '" + NB_SEATS + "' FROM flights JOIN planes ON planes.Id = RefPlane"
				+ " WHERE DepartureDate BETWEEN curdate() AND adddate(curdate(), INTERVAL 7 DAY)"
				+ " ORDER BY DepartureDate, DepartureTime;"));
			db.setResultSet(db.getPreparedStatement().executeQuery());

			LinkedHashMap<Flight, Integer> flights = new LinkedHashMap<>();
			while (db.getResultSet().next()) {
				flights.put(resultSetToFlight(db.getResultSet()),
					db.getResultSet().getInt(NB_SEATS));
			}
			return flights;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 *
	 * @param destinationId
	 *
	 * @return
	 */
	public LinkedHashMap<Flight, Integer> selectFlightsForDestination(String destinationId) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT flights.*, NbSeats-count(tickets.Id) AS '" + NB_SEATS
				+ "' FROM planes JOIN flights ON planes.Id = RefPlane"
				+ " LEFT JOIN tickets ON flights.Id = RefFlight"
				+ " WHERE RefDestination = ?"
				+ " GROUP BY flights.Id;"));
			db.getPreparedStatement().setString(1, destinationId);
			db.setResultSet(db.getPreparedStatement().executeQuery());

			LinkedHashMap<Flight, Integer> flights = new LinkedHashMap<>();
			while (db.getResultSet().next()) {
				flights.put(resultSetToFlight(db.getResultSet()),
					db.getResultSet().getInt(NB_SEATS));
			}
			return flights;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 *
	 * @param passengerId
	 *
	 * @return
	 */
	public LinkedHashMap<Flight, Integer> selectFlightsWithUnpaidTicketsForPassenger(String passengerId) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT flights.*, count(tickets.Id) AS '" + NB_TICK
				+ "' FROM tickets JOIN flights ON RefFlight = flights.Id"
				+ " WHERE Paid = FALSE AND RefPassenger = ?"
				+ " GROUP BY RefFlight;"));
			db.getPreparedStatement().setString(1, passengerId);
			db.setResultSet(db.getPreparedStatement().executeQuery());

			LinkedHashMap<Flight, Integer> flights = new LinkedHashMap<>();
			while (db.getResultSet().next()) {
				flights.put(resultSetToFlight(db.getResultSet()),
					db.getResultSet().getInt(NB_TICK));
			}
			return flights;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 * 
	 * @param id
	 * @return 
	 */
	public int selectPriceForFlight(int id) {
		try {
			db.setPreparedStatement(db.prepareStatement(
				"SELECT Price FROM flights WHERE Id = ? ;"));
			db.getPreparedStatement().setInt(1, id);
			db.setResultSet(db.getPreparedStatement().executeQuery());
			db.getResultSet().next();
			return db.getResultSet().getInt("Price");
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return 0;
		}
	}
	
}

package db.airport.DAO;

import database.utilities.DAO.AbstractDAO;
import database.utilities.BeanDBAccess;
import db.airport.models.stats.WeightDistance;
import db.airport.models.stats.WeightZone;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Morgan
 */
public class StatsDAO extends AbstractDAO {

	/**
	 * Constructor
	 *
	 * @param db : a database.utilities.BeanDBAccess
	 */
	public StatsDAO(BeanDBAccess db) {
		super(db);
	}

	/**
	 *
	 * @param year : year (4-numbers format)
	 * @param month : month number (-1 if no month)
	 * @param airline : airline name (null if no airline)
	 * @param nbAcc : whether to add the number of accompanying people
	 * @param age : whether to add the age of the person
	 * @return
	 */
	public ArrayList<WeightDistance> selectMeanWeightsAndDistances(int year,
		int month, String airline, boolean nbAcc, boolean age) {
		try {
			String query = "SELECT sum(Weight)/count(Weight) as MeanWeight, Distance "
				+ (age ? ", Age " : "")
				+ (nbAcc ? ", t.NbAcc " : "")
				+ "FROM luggages JOIN (SELECT tickets.*, count(Id) as NbAcc "
				+ " FROM tickets GROUP BY RefPassenger, RefFlight) "
				+ " as t ON RefTicket = t.Id "
				+ " JOIN passengers ON RefPassenger = passengers.Id "
				+ " JOIN flights ON RefFlight = flights.Id "
				+ " JOIN destination ON RefDestination = destination.Id "
				+ "WHERE year(DepartureDate) = ? "
				+ ( month > 0 ? " AND month(DepartureDate) = ? " : "" )
				+ ( airline != null ? " AND flights.RefAirline = ? " : "" )
				+ "GROUP BY RefTicket;";
			//System.err.println("Debug : " + query);
			db.setPreparedStatement(db.prepareStatement(query));
			db.getPreparedStatement().setInt(1, year);
			if (month > 0) db.getPreparedStatement().setInt(2, month);
			if (airline != null) {
				if (month > 0) db.getPreparedStatement().setString(3, airline);
				else db.getPreparedStatement().setString(2, airline);
			}
			db.setResultSet(db.getPreparedStatement().executeQuery());

			ArrayList<WeightDistance> list = new ArrayList<>();
			while (db.getResultSet().next()) {
				WeightDistance wd = new WeightDistance(db.getResultSet()
					.getDouble("MeanWeight"), db.getResultSet().getInt("Distance"));
				if (age) wd.setAge(db.getResultSet().getInt("Age"));
				if (nbAcc) wd.setNbAcc(db.getResultSet().getInt("NbAcc"));
				list.add(wd);
			}
			return list;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param airline
	 * @return
	 */
	public ArrayList<WeightDistance> selectMeanWeightsAndDistances(int year,
		int month, String airline) {
		return selectMeanWeightsAndDistances(year, month, airline, false, false);
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param airline
	 * @param gender
	 * @return
	 */
	public ArrayList<WeightZone> selectWeightsAndZones(int year, int month,
		String airline, boolean gender) {
		try {
			String query = "SELECT sum(Weight)/count(Weight) as MeanWeight, RefZone "
				+ (gender ? ", Gender " : ", sum(Weight) as Sum ")
				+ "FROM luggages JOIN (SELECT tickets.*, count(Id) as NbAcc "
				+ " FROM tickets GROUP BY RefPassenger, RefFlight) "
				+ " as t ON RefTicket = t.Id "
				+ " JOIN passengers ON RefPassenger = passengers.Id "
				+ " JOIN flights ON RefFlight = flights.Id "
				+ " JOIN destination ON RefDestination = destination.Id "
				+ "WHERE year(DepartureDate) = ? "
				+ ( month > 0 ? " AND month(DepartureDate) = ? " : "" )
				+ ( airline != null ? " AND flights.RefAirline = ? " : "" )
				+ "GROUP BY RefTicket;";
			db.setPreparedStatement(db.prepareStatement(query));
			db.getPreparedStatement().setInt(1, year);
			if (month > 0) db.getPreparedStatement().setInt(2, month);
			if (airline != null) {
				if (month > 0) db.getPreparedStatement().setString(3, airline);
				else db.getPreparedStatement().setString(2, airline);
			}
			db.setResultSet(db.getPreparedStatement().executeQuery());

			ArrayList<WeightZone> list = new ArrayList<>();
			while (db.getResultSet().next()) {
				WeightZone wz = new WeightZone(db.getResultSet().getDouble("MeanWeight"),
					db.getResultSet().getString("RefZone"));
				if (gender) wz.setGender(db.getResultSet().getString("Gender"));
				else wz.setTotalWeight(db.getResultSet().getDouble("Sum"));
				list.add(wz);
			}
			return list;
		} catch (SQLException e) {
			System.err.println("Error : " + e.getMessage()
				+ " / SQL STATE : " + e.getSQLState());
			return null;
		}
	}
}

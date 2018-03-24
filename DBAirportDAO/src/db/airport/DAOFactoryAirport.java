package db.airport;

import db.airport.DAO.*;
import database.utilities.DAO.AbstractDAOFactoryMySQL;

/**
 * @author Morgan
 */
public class DAOFactoryAirport extends AbstractDAOFactoryMySQL {

	private static DAOFactoryAirport factoryInstance = null;

    // <editor-fold defaultstate="collapsed" desc="DAO Variables">
	private AgentDAO agentDAO;
	private AirlineDAO airlineDAO;
	private ClientDAO clientDAO;
	private DestinationDAO destinationDAO;
	private EmployerDAO employerDAO;
	private FlightDAO flightDAO;
	private LanguageDAO languageDAO;
	private LuggageDAO luggageDAO;
	private PassengerDAO passengerDAO;
	private PlaneDAO planeDAO;
	private TicketDAO ticketDAO;
	private ZoneDAO zoneDAO;

	private StatsDAO statsDAO;
    // </editor-fold>

	/**
	 * Constructor.
	 */
	private DAOFactoryAirport() {
		super("db_airport");
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static DAOFactoryAirport getInstance(){
		if (factoryInstance == null) factoryInstance = new DAOFactoryAirport();
		return factoryInstance;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized AgentDAO getAgentDAO() {
		if (agentDAO == null) agentDAO = new AgentDAO(bDAccess);
		return agentDAO;
	}

	/**
	 *
	 * @return
	 */
	public synchronized AirlineDAO getAirlineDAO() {
		if (airlineDAO == null) airlineDAO = new AirlineDAO(bDAccess);
		return airlineDAO;
	}

	/**
	 * 
	 * @return 
	 */
	public synchronized ClientDAO getClientDAO() {
		if (clientDAO == null) clientDAO = new ClientDAO(bDAccess);
		return clientDAO;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized DestinationDAO getDestinationDAO() {
		if (destinationDAO == null) destinationDAO = new DestinationDAO(bDAccess);
		return destinationDAO;
	}

	/**
	 * 
	 * @return 
	 */
	public synchronized EmployerDAO getEmployerDAO() {
		if (employerDAO == null) employerDAO = new EmployerDAO(bDAccess);
		return employerDAO;
	}
	
	/**
	 *
	 * @return
	 */
	public synchronized FlightDAO getFlightDAO() {
		if (flightDAO == null) flightDAO = new FlightDAO(bDAccess);
		return flightDAO;
	}
	
	/**
	 * 
	 * @return 
	 */
	public synchronized LanguageDAO getLanguageDAO() {
		if (languageDAO == null) languageDAO = new LanguageDAO(bDAccess);
		return languageDAO;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized LuggageDAO getLuggageDAO() {
		if (luggageDAO == null) luggageDAO = new LuggageDAO(bDAccess);
		return luggageDAO;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized PassengerDAO getPassengerDAO(){
		if (passengerDAO == null) passengerDAO = new PassengerDAO(bDAccess);
		return passengerDAO;
	}

	/**
	 *
	 * @return
	 */
	public synchronized PlaneDAO getPlaneDAO() {
		if (planeDAO == null) planeDAO = new PlaneDAO(bDAccess);
		return planeDAO;
	}

	/**
	 *
	 * @return
	 */
	public synchronized TicketDAO getTicketDAO() {
		if (ticketDAO == null) ticketDAO = new TicketDAO(bDAccess);
		return ticketDAO;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized ZoneDAO getZoneDAO() {
		if (zoneDAO == null) zoneDAO = new ZoneDAO(bDAccess);
		return zoneDAO;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized StatsDAO getStatsDAO() {
		if (statsDAO == null) statsDAO = new StatsDAO(bDAccess);
		return statsDAO;
	}
    
}

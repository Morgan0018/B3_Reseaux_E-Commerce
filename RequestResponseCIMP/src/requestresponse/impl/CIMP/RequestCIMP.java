package requestresponse.impl.CIMP;

import db.airport.DAOFactoryAirport;
import db.airport.models.Agent;
import db.airport.models.Flight;
import db.airport.models.Luggage;
import db.airport.models.Ticket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import requestresponse.connection.ConnectionCServer;
import requestresponse.multithread.interfaces.ConsoleServer;
import requestresponse.multithread.interfaces.Request;

/**
 * @author Morgan
 */
public class RequestCIMP implements Request {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	public final static int LOGOUT = 0;
	public final static int LOGIN = 1;
	public final static int FLIGHT_INFO = 2;
	public final static int CHECK_TICKET = 3;
	public final static int SAVE_LUGGAGE = 4;
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Variables">
	private final int type;
	private final String chargeUtile;
	private ConnectionCServer parent;
	//private DataOutputStream dos = null;
	private ResponseCIMP response;
	//</editor-fold>

	/**
	 *
	 * @param type
	 * @param chargeUtile
	 */
	public RequestCIMP(int type, String chargeUtile) {
		this.type = type;
		this.chargeUtile = chargeUtile;
		response = null;
	}

	//<editor-fold defaultstate="collapsed" desc="Getters&Setters">
	/**
	 *
	 * @return
	 */
	@Override
	public int getType() {
		return type;
	}

	/**
	 *
	 * @return the "message" that was set in the constructor
	 */
	public String getChargeUtile() {
		return chargeUtile;
	}

	/**
	 * Gives access to the "session" variables contained in ConnectionCServer.
	 *
	 * @return the requestresponse.multithread.ConnectionCServer that handles this client
	 */
	public ConnectionCServer getParent() {
		return parent;
	}

	/**
	 * Sets the ConnectionCServer that handles all the Request from this client.
	 * Used in the ConnectionCServer run()
	 *
	 * @param parent: a requestresponse.multithread.ConnectionCServer
	 */
	public void setParent(ConnectionCServer parent) {
		this.parent = parent;
	}
	//</editor-fold>

	/**
	 *
	 * @param s
	 * @param res
	 */
	private void send(/*Socket s, */ResponseCIMP res) {
		try {
			String r = res.toString() + '\r' + '\n';
			System.out.println("Debug : " + r);
			parent.getDataOutputStream().write(r.getBytes());
			parent.getDataOutputStream().flush();
		} catch (IOException e) {
			System.err.println("Error - send : " + e.getMessage());
		}
	}

	/**
	 *
	 * @param s
	 * @param cs
	 *
	 * @return
	 */
	@Override
	public Runnable createRunnable(Socket s, ConsoleServer cs) {
		return new Runnable() {
			@Override
			public void run() {
				switch (type) {
					case LOGOUT:
						handleLogout(s, cs);
						break;
					case LOGIN:
						handleLogin(s, cs);
						break;
					case FLIGHT_INFO:
						handleFlightInfo(s, cs);
						break;
					case CHECK_TICKET:
						handleCheckTicket(s, cs);
						break;
					case SAVE_LUGGAGE:
						handleSaveLuggage(s, cs);
						break;
				}
				send(response);
			}
		};
	}

	/**
	 * 
	 * @param s
	 * @param cs 
	 */
	private void handleLogout(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.CIMP.RequestCIMP.handleLogout()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#LOGOUT#" + Thread.currentThread().getName());
		response = new ResponseCIMP(ResponseCIMP.LOGOUT_OK, "");
	}

	/**
	 * 
	 * @param s
	 * @param cs 
	 */
	private void handleLogin(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.CIMP.RequestCIMP.handleLogin()");
		StringTokenizer st = new StringTokenizer(chargeUtile, "#");
		String login = st.nextToken();
		cs.TraceEvents(s.getRemoteSocketAddress() + "#LOGIN for " + login + "#" + Thread.currentThread().getName());
		String pwd = st.nextToken();
		Agent a = DAOFactoryAirport.getInstance().getAgentDAO().selectOne(login);
		if (a != null && pwd.equals(a.getPassword()))
			response = new ResponseCIMP(ResponseCIMP.LOGIN_OK, "OK");
		else response = new ResponseCIMP(ResponseCIMP.LOGIN_FAIL, "Mauvais login ou mot de passe");
	}

	/**
	 * 
	 * @param s
	 * @param cs 
	 */
	private void handleFlightInfo(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.CIMP.RequestCIMP.handleFlightInfo()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#FLIGHT_INFO#" + Thread.currentThread().getName());
		Flight f = DAOFactoryAirport.getInstance().getFlightDAO().selectNextFlight();
		if (f != null) response = new ResponseCIMP(ResponseCIMP.FLIGHT_INFO_OK, f.toString());
		else response = new ResponseCIMP(ResponseCIMP.FLIGHT_INFO_FAIL, "Aucun vol prévu");
	}

	/**
	 * 
	 * @param s
	 * @param cs 
	 */
	private void handleCheckTicket(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.CIMP.RequestCIMP.handleCheckTicket()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#CHECK_TICKET#" + Thread.currentThread().getName());
		StringTokenizer st = new StringTokenizer(chargeUtile, "#");
		String numTicket = st.nextToken();
		int nbPass = Integer.parseInt(st.nextToken());
		Ticket t = DAOFactoryAirport.getInstance().getTicketDAO().selectOne(numTicket);
		int count = DAOFactoryAirport.getInstance().getTicketDAO()
			.countForPassengerAndFlight(t.getRefPassenger(), t.getRefFlight());
		if (count < 0) response = new ResponseCIMP(
			ResponseCIMP.CHECK_TICKET_FAIL, "Problème avec les données");
		else if (nbPass <= count) response = new ResponseCIMP(ResponseCIMP.CHECK_TICKET_OK, t.getRefPassenger());
		else response = new ResponseCIMP(ResponseCIMP.CHECK_TICKET_FAIL, "Trop d'accompagnants");
	}

	/**
	 * 
	 * @param s
	 * @param cs 
	 */
	private void handleSaveLuggage(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.CIMP.RequestCIMP.handleCheckLuggage()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#SAVE_LUGGAGE#" + Thread.currentThread().getName());
		StringTokenizer st = new StringTokenizer(chargeUtile, "#");
		String numTick = st.nextToken();
		ArrayList<Luggage> luggages = new ArrayList<>();
		while (st.hasMoreTokens()) {			
			String bag = st.nextToken();
			StringTokenizer bagTok = new StringTokenizer(bag, "@");
			String idBag = bagTok.nextToken();
			boolean t = "VALISE".equals(bagTok.nextToken());
			float weight = Float.parseFloat(bagTok.nextToken());
			luggages.add(new Luggage(idBag, t, weight, numTick));
		}
		if (DAOFactoryAirport.getInstance().getLuggageDAO().insertMultipleNew(luggages))
			response = new ResponseCIMP(ResponseCIMP.SAVE_LUGGAGE_OK, "OK");
		else response = new ResponseCIMP(ResponseCIMP.SAVE_LUGGAGE_FAIL, "La sauvegarde a échoué");
	}

}

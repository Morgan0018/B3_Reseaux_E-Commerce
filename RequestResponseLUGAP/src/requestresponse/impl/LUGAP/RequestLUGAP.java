package requestresponse.impl.LUGAP;

import db.airport.DAOFactoryAirport;
import db.airport.models.Flight;
import db.airport.models.Luggage;
import java.net.Socket;
import java.util.ArrayList;
import requestresponse.multithread.abstracts.AbstractRequest;
import static requestresponse.multithread.abstracts.AbstractRequest.LOGIN;
import static requestresponse.multithread.abstracts.AbstractRequest.LOGOUT;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * Implementation of Request for the LUGAP protocol.
 *
 * @author Morgan
 */
public class RequestLUGAP extends AbstractRequest {

	//Protocol constants
	public final static int GET_FLIGHTS = 2;
	public final static int CHOOSE_FLIGHT = 3;
	public final static int RECEIVE_LUGGAGE = 4;
	public final static int CUSTOM_CHECK = 5;
	public final static int LOAD_LUGGAGE = 6;
	public final static int ADD_COMMENT = 7;

	/**
	 * Constructor
	 *
	 * @param type : one of the constants defined in the Request
	 * @param chargeUtile : the "message" to transmit
	 */
	public RequestLUGAP(int type, Object chargeUtile) {
		super(type, chargeUtile);
	}

	/**
	 * Creates a Runnable that will call a different method depending on the type
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 * @return a Runnable
	 */
	@Override
	public Runnable createRunnable(Socket s, ConsoleServer cs) {
		return new Runnable() {
			@Override
			public void run() {
				response = null;
				if (!parent.isLoggedIn() && type != LOGIN) {
					handleUnknownOrFail(s, cs, "NOT LOGGED IN");
				} else {
					switch (type) {
						case LOGIN:
							parent.setLoggedIn(handleLogin(s, cs));
							break;
						case LOGOUT:
							parent.setLoggedIn(handleLogout(s, cs));
							break;
						case GET_FLIGHTS:
							handleGetFlights(s, cs);
							break;
						case CHOOSE_FLIGHT:
							handleChooseFlight(s, cs);
							break;
						case RECEIVE_LUGGAGE:
							handleReceiveLuggage(s, cs);
							break;
						case CUSTOM_CHECK:
							handleCustomCheck(s, cs);
							break;
						case LOAD_LUGGAGE:
							handleLoadLuggage(s, cs);
							break;
						case ADD_COMMENT:
							handleAddComment(s, cs);
							break;
						default:
							handleUnknownOrFail(s, cs, "unknown command");
					}
				}
				send(s, response);
			}
		};
	}

	/**
	 * Builds the response for a GET_FLIGHTS request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleGetFlights(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleGetFlights()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#GET_FLIGHTS#" + Thread.currentThread().getName());
		ArrayList<Flight> listVols = DAOFactoryAirport.getInstance().getFlightDAO().selectTodaysFlights();
		if (listVols != null) response = new ResponseLUGAP(ResponseLUGAP.GET_FLIGHTS_OK, listVols);
		else response = new ResponseLUGAP(ResponseLUGAP.GET_FLIGHTS_FAIL, "No flights");
	}

	/**
	 * Builds the Response for a CHOOSE_FLIGHT Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleChooseFlight(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleChooseFlight()");
		int idFlight = (int) getChargeUtile();
		cs.TraceEvents(s.getRemoteSocketAddress() + "#Flight " + idFlight + "#" + Thread.currentThread().getName());
		ArrayList<Luggage> luggages = DAOFactoryAirport.getInstance().getLuggageDAO().selectAllForFlight(idFlight);
		//System.out.println("Debug : " + luggages);
		if (luggages != null && !luggages.isEmpty()) {
			response = new ResponseLUGAP(ResponseLUGAP.CHOOSE_FLIGHT_OK, luggages);
		} else {
			response = new ResponseLUGAP(ResponseLUGAP.CHOOSE_FLIGHT_FAIL, "No luggages for flight");
		}
	}

	/**
	 * Builds the Response for a RECEIVE_LUGGAGE Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleReceiveLuggage(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleReceiveLuggage()");
		Object[] cu = (Object[]) getChargeUtile();
		String idL = (String) cu[0];
		cs.TraceEvents(s.getRemoteSocketAddress() + "#RECEIVED Luggage " + idL + "#" + Thread.currentThread().getName());
		if (DAOFactoryAirport.getInstance().getLuggageDAO().updateReceived(idL, (boolean) cu[1])) {
			response = new ResponseLUGAP(ResponseLUGAP.RECEIVE_LUGGAGE_OK, "");
		} else {
			response = new ResponseLUGAP(ResponseLUGAP.RECEIVE_LUGGAGE_FAIL, "Update failed");
		}
	}

	/**
	 * Builds the Response for a CUSTOM_CHECK Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleCustomCheck(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleCustomCheck()");
		Object[] cu = (Object[]) getChargeUtile();
		String idL = (String) cu[0];
		cs.TraceEvents(s.getRemoteSocketAddress() + "#CUSTOM CHECK Luggage " + idL + "#" + Thread.currentThread().getName());
		if (DAOFactoryAirport.getInstance().getLuggageDAO().updateCustomCheck(idL, (boolean) cu[1])) {
			response = new ResponseLUGAP(ResponseLUGAP.CUSTOM_CHECK_OK, "");
		} else {
			response = new ResponseLUGAP(ResponseLUGAP.CUSTOM_CHECK_FAIL, "Update failed");
		}
	}

	/**
	 * Builds the Response for a LOAD_LUGGAGE Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleLoadLuggage(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleLoadLuggage()");
		Object[] cu = (Object[]) getChargeUtile();
		String idL = (String) cu[0];
		cs.TraceEvents(s.getRemoteSocketAddress() + "#LOAD Luggage " + idL + "#" + Thread.currentThread().getName());
		if (DAOFactoryAirport.getInstance().getLuggageDAO().updateLoaded(idL, (char) cu[1])) {
			response = new ResponseLUGAP(ResponseLUGAP.LOAD_LUGGAGE_OK, "");
		} else {
			response = new ResponseLUGAP(ResponseLUGAP.LOAD_LUGGAGE_FAIL, "Update failed");
		}
	}

	/**
	 * Builds the Response for an ADD_COMMENT Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleAddComment(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleAddComment()");
		Object[] cu = (Object[]) getChargeUtile();
		String idL = (String) cu[0];
		cs.TraceEvents(s.getRemoteSocketAddress() + "#COMMENT Luggage " + idL + "#" + Thread.currentThread().getName());
		if (DAOFactoryAirport.getInstance().getLuggageDAO().updateComment(idL, (String) cu[1])) {
			response = new ResponseLUGAP(ResponseLUGAP.ADD_COMMENT_OK, "");
		} else {
			response = new ResponseLUGAP(ResponseLUGAP.ADD_COMMENT_FAIL, "Update failed");
		}
	}

}

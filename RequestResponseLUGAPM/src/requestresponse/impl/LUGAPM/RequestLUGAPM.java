package requestresponse.impl.LUGAPM;

import db.airport.DAOFactoryAirport;
import db.airport.models.Agent;
import db.airport.models.Flight;
import db.airport.models.Luggage;
import java.net.Socket;
import java.util.ArrayList;
import requestresponse.multithread.abstracts.AbstractRequest;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public class RequestLUGAPM extends AbstractRequest {

	//Protocol constants
	public final static int CHOOSE_FLIGHT = 2;
	public final static int RECEIVED_AND_LOADED = 3;

	/**
	 *
	 * @param type
	 * @param chargeUtile
	 */
	public RequestLUGAPM(int type, Object chargeUtile) {
		super(type, chargeUtile);
	}

	/**
	 *
	 * @param s
	 * @param cs
	 * @return
	 */
	@Override
	public Runnable createRunnable(Socket s, ConsoleServer cs) {
		return new Runnable() {
			@Override
			public void run() {
				response = null;
				if (!parent.isLoggedIn() && type != LOGIN && type != LOGOUT) {
					handleUnknownOrFail(s, cs, "NOT LOGGED IN");
				} else {
					switch (type) {
						case LOGIN:
							parent.setLoggedIn(handleLogin(s, cs));
							break;
						case LOGOUT:
							parent.setLoggedIn(handleLogout(s, cs));
							break;
						case CHOOSE_FLIGHT:
							handleChooseFlight(s, cs);
							break;
						case RECEIVED_AND_LOADED:
							handleReceivedAndLoaded(s, cs);
							break;
						default:
							handleUnknownOrFail(s, cs, "Unknown Command");
					}
				}
				send(s, response);
			}
		};
	}

	/**
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 * @return a boolean representing the success (or not) of the login
	 */
	@Override
	protected boolean handleLogin(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAPM.RequestLUGAPM.handleLogin()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#LOGIN#" + Thread.currentThread().getName());
		Object cu[] = (Object[]) getChargeUtile();
		String login = (String) cu[0];
		String pwd = (String) cu[1];
		Agent a = DAOFactoryAirport.getInstance().getAgentDAO().selectOne(login);
		if (pwd.equals(a.getPassword())) {
			ArrayList<Flight> flights = DAOFactoryAirport.getInstance().getFlightDAO().selectTodaysFlights();
			if (flights != null) {
				response = new ResponseLUGAPM(ResponseLUGAPM.LOGIN_OK, flights);
				return true;
			} else response = new ResponseLUGAPM(ResponseLUGAPM.LOGIN_FAIL, "No data for flights");
		} else response = new ResponseLUGAPM(ResponseLUGAPM.LOGIN_FAIL, "No such Agent");
		return false;
	}

	/**
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleChooseFlight(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAPM.RequestLUGAPM.handleChooseFlight()");
		int idFlight = (int) getChargeUtile();
		cs.TraceEvents(s.getRemoteSocketAddress() + "#Flight " + idFlight + "#" + Thread.currentThread().getName());
		ArrayList<Luggage> luggages = DAOFactoryAirport.getInstance().getLuggageDAO().selectAllForFlight(idFlight);
		if (luggages != null && !luggages.isEmpty()) {
			response = new ResponseLUGAPM(ResponseLUGAPM.CHOOSE_FLIGHT_OK, luggages);
		} else {
			response = new ResponseLUGAPM(ResponseLUGAPM.CHOOSE_FLIGHT_FAIL, "No Data");
		}
	}

	/**
	 * 
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleReceivedAndLoaded(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAPM.RequestLUGAPM.handleReceivedAndLoaded()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#RECEIVED_AND_LOADED#"+Thread.currentThread().getName());
		ArrayList<Luggage> luggages = (ArrayList<Luggage>) getChargeUtile();
		boolean updateOK = DAOFactoryAirport.getInstance().getLuggageDAO().bulkUpdateReceivedAndLoaded(luggages);
		if (updateOK) {
			response = new ResponseLUGAPM(ResponseLUGAPM.RECEIVED_AND_LOADED_OK, " ");
		} else {
			response = new ResponseLUGAPM(ResponseLUGAPM.RECEIVED_AND_LOADED_FAIL, "Update fail");
		}
	}

}

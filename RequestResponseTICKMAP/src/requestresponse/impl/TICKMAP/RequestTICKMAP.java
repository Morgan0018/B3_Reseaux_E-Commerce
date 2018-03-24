package requestresponse.impl.TICKMAP;

import crypto.utilities.CryptoUtilities;
import db.airport.DAOFactoryAirport;
import db.airport.models.Flight;
import db.airport.models.Passenger;
import db.airport.models.Ticket;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import requestresponse.multithread.connection.SecureConnection;
import requestresponse.multithread.abstracts.AbstractRequest;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public class RequestTICKMAP extends AbstractRequest {

	//<editor-fold defaultstate="collapsed" desc="Protocol constants">
	public final static int HANDSHAKE = 2;
	public final static int GET_FLIGHTS = 3;
	public final static int CHOOSE_FLIGHT = 4;
	public final static int HOLD_TICKETS = 5;
	public final static int CONFIRM_TICKETS = 6;
	public final static int CONFIRM_PAYMENT = 7;
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Variables">
	private final char[] pwd = "azerty".toCharArray();
	private final String alias = "SerTick";
	private final String asymetricAlgorithm = "RSA/ECB/PKCS1Padding";
	private final String symetricGenAlgorithm = "DES";
	private final String symetricAlgorithm = "DES/ECB/PKCS5Padding";
	//</editor-fold>

	/**
	 * Constructor
	 *
	 * @param type        : one of the constants defined in the Request
	 * @param chargeUtile : the "message" to transmit
	 */
	public RequestTICKMAP(int type, Object chargeUtile) {
		super(type, chargeUtile);
	}

	/**
	 * Creates a Runnable that will call a different method depending on the type
	 *
	 * @param s  : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 *
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
						case HANDSHAKE:
							handleHandshake(s, cs);
							break;
						case GET_FLIGHTS:
							handleGetFlights(s, cs);
							break;
						case CHOOSE_FLIGHT:
							handleChooseFlight(s, cs);
							break;
						case HOLD_TICKETS:
							handleHoldTickets(s, cs);
							break;
						case CONFIRM_TICKETS:
							handleConfirmTickets(s, cs);
							break;
						case CONFIRM_PAYMENT:
							handleConfirmPayment(s, cs);
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
	 *
	 * @param s
	 * @param cs
	 */
	private void handleHandshake(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.TICKMAP.RequestTICKMAP.handleHandshake()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#Handshake#" + Thread.currentThread().getName());
		Object cu[] = (Object[]) getChargeUtile();
		byte[] wrapKeyHmac = (byte[]) cu[0];
		byte[] wrapKeyCrypt = (byte[]) cu[1];
		String msg = "";
		//get server private key
		PrivateKey prk = CryptoUtilities.getPrivateKeyFromKeystore(System
			.getProperty("user.dir") + System.getProperty("file.separator")
			+ "ksSerTick", pwd, alias);
		if (prk != null) {
			//unwrap secret keys
			SecretKey hmacSecretKey = (SecretKey) CryptoUtilities
				.unwrapKey(asymetricAlgorithm, prk, wrapKeyHmac, symetricGenAlgorithm, Cipher.SECRET_KEY);
			SecretKey cryptSecretKey = (SecretKey) CryptoUtilities
				.unwrapKey(asymetricAlgorithm, prk, wrapKeyCrypt, symetricGenAlgorithm, Cipher.SECRET_KEY);
			if (hmacSecretKey != null && cryptSecretKey != null) {
				//regiter session keys
				((SecureConnection) parent).setHMACSecretKey(hmacSecretKey);
				((SecureConnection) parent).setCryptSecretKey(cryptSecretKey);
				response = new ResponseTICKMAP(ResponseTICKMAP.HANDSHAKE_OK, "");
			} else msg = "Problem unwrapping the keys";
		} else msg = "Problem getting the private key";
		if (!"".equals(msg))
			response = new ResponseTICKMAP(ResponseTICKMAP.HANDSHAKE_FAIL, msg);
	}

	/**
	 * Builds the response for a GET_FLIGHTS request
	 *
	 * @param s  : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleGetFlights(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleGetFlights()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#GET_FLIGHTS#" + Thread.currentThread().getName());
		LinkedHashMap<Flight, Integer> listFlights = DAOFactoryAirport.getInstance().getFlightDAO().select7DaysFlights();
		if (listFlights != null)
			response = new ResponseTICKMAP(ResponseTICKMAP.GET_FLIGHTS_OK, listFlights);
		else response = new ResponseTICKMAP(ResponseTICKMAP.GET_FLIGHTS_FAIL, "No flights");
	}

	/**
	 * Builds the Response for a CHOOSE_FLIGHT Request
	 *
	 * @param s  : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleChooseFlight(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGAP.RequestLUGAP.handleChooseFlight()");
		int idFlight = (int) getChargeUtile();
		cs.TraceEvents(s.getRemoteSocketAddress() + "#Flight " + idFlight + "#" + Thread.currentThread().getName());
		ArrayList<Passenger> passengers = DAOFactoryAirport.getInstance()
			.getPassengerDAO().selectAll();
		if (passengers != null)
			response = new ResponseTICKMAP(ResponseTICKMAP.CHOOSE_FLIGHT_OK, passengers);
		else response = new ResponseTICKMAP(ResponseTICKMAP.CHOOSE_FLIGHT_FAIL,
				"Problem getting passengers from DB");
	}

	/**
	 *
	 * @param s
	 * @param cs
	 */
	private void handleHoldTickets(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.TICKMAP.RequestTICKMAP.handleHoldTickets()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#HOLD_TICKETS#" + Thread.currentThread().getName());
		byte[] cryptedCU = (byte[]) getChargeUtile();
		SecretKey key = ((SecureConnection) parent).getCryptSecretKey();
		Object cu[] = (Object[]) CryptoUtilities.symmetricDecryption(cryptedCU, symetricAlgorithm, key);
		int idFlight = (int) cu[0];
		int nbPassengers = (int) cu[1];
		Passenger p = (Passenger) cu[2];

		boolean seatsOK = true; //TODO : check available seats
		if (!seatsOK) response = new ResponseTICKMAP(
			ResponseTICKMAP.HOLD_TICKETS_FAIL, "Not enough seats available");
		else {
			if (DAOFactoryAirport.getInstance().getPassengerDAO().selectOne(p.getId()) == null)
				DAOFactoryAirport.getInstance().getPassengerDAO().insertNew(p);
			ArrayList<Ticket> tickets = new ArrayList<>();
			for (int i = 0; i < nbPassengers; i++) {
				tickets.add(new Ticket(p.getId(), idFlight));
			}
			DAOFactoryAirport.getInstance().getTicketDAO().insertMultipleNew(tickets);
			float price = DAOFactoryAirport.getInstance().getFlightDAO().selectPriceForFlight(idFlight);
			if (price == 0) response = new ResponseTICKMAP(
				ResponseTICKMAP.HOLD_TICKETS_FAIL, "Couldn't get tickets price");
			else {
				price = price * nbPassengers;
				Object toSend[] = {price};
				byte[] cryptedToSend = CryptoUtilities.symmetricEncryption(toSend, symetricAlgorithm, key);
				response = new ResponseTICKMAP(ResponseTICKMAP.HOLD_TICKETS_OK, cryptedToSend);
			}
		}
	}

	/**
	 *
	 * @param s
	 * @param cs
	 */
	private void handleConfirmTickets(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.TICKMAP.RequestTICKMAP.handleConfirmTickets()");
		Object cu[] = (Object[]) getChargeUtile();
		Object rcv[] = (Object[]) cu[0];
		boolean ok = (boolean) rcv[0];
		cs.TraceEvents(s.getRemoteSocketAddress() + "#CONFIRM_TICKETS " + ok + "#" + Thread.currentThread().getName());
		byte[] hmac = (byte[]) cu[1];
		SecretKey key = ((SecureConnection) parent).getHMACSecretKey();
		boolean hmacOK = CryptoUtilities.verifyHMAC(hmac, rcv, key);
		if (!hmacOK) response = new ResponseTICKMAP(
			ResponseTICKMAP.CONFIRM_TICKETS_FAIL, "HMAC not verified");
		else {
			if (ok) response = new ResponseTICKMAP(ResponseTICKMAP.CONFIRM_TICKETS_YES_OK, "");
			else {
				int idFlight = (int) rcv[1];
				String idPassenger = (String) rcv[2];
				DAOFactoryAirport.getInstance().getTicketDAO()
					.deleteUnpaidTicketsForFlightAndPassenger(idFlight, idPassenger);
				response = new ResponseTICKMAP(ResponseTICKMAP.CONFIRM_TICKETS_NO_OK, "");
			}
		}
	}

	/**
	 *
	 * @param s
	 * @param cs
	 */
	private void handleConfirmPayment(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.TICKMAP.RequestTICKMAP.handleConfirmPayment()");
		Object cu[] = (Object[]) getChargeUtile();
		Object rcv[] = (Object[]) cu[0];
		boolean ok = (boolean) rcv[0];
		cs.TraceEvents(s.getRemoteSocketAddress() + "#CONFIRM_PAYMENT " + ok + "#" + Thread.currentThread().getName());
		byte[] hmac = (byte[]) cu[1];
		SecretKey key = ((SecureConnection) parent).getHMACSecretKey();
		boolean hmacOK = CryptoUtilities.verifyHMAC(hmac, rcv, key);
		if (!hmacOK) response = new ResponseTICKMAP(
			ResponseTICKMAP.CONFIRM_PAYMENT_FAIL, "HMAC not verified");
		else {
			int idFlight = (int) rcv[1];
			String idPassenger = (String) rcv[2];
			if (ok) {
				DAOFactoryAirport.getInstance().getTicketDAO()
					.updateAllPaidForPassenger(idPassenger);
				response = new ResponseTICKMAP(ResponseTICKMAP.CONFIRM_PAYMENT_YES_OK, "");
			} else {
				DAOFactoryAirport.getInstance().getTicketDAO()
					.deleteUnpaidTicketsForFlightAndPassenger(idFlight, idPassenger);
				response = new ResponseTICKMAP(ResponseTICKMAP.CONFIRM_PAYMENT_NO_OK, "");
			}
		}
	}

}

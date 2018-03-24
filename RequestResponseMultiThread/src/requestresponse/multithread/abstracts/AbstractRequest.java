package requestresponse.multithread.abstracts;

import crypto.utilities.CryptoUtilities;
import db.airport.DAOFactoryAirport;
import db.airport.models.Agent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import requestresponse.multithread.BasicResponse;
import requestresponse.multithread.connection.Connection;
import requestresponse.multithread.interfaces.*;

/**
 * An abstract class that implements Request and Serializable and contains
 * constants and variables needed by all the implementations of Request
 *
 * @author Morgan
 */
public abstract class AbstractRequest implements Serializable, Request {

	//<editor-fold defaultstate="collapsed" desc="var&const">
	//Constants
	public final static int LOGOUT = 0;
	public final static int LOGIN = 1;
	//Variables
	protected int type;
	protected Object chargeUtile = null;
	protected Connection parent;
	protected ObjectOutputStream oos;
	protected AbstractResponse response;
	//</editor-fold>

	/**
	 * Constructor
	 *
	 * @param type : one of the constants defined in the Request
	 * @param chargeUtile : the "message" to transmit
	 */
	protected AbstractRequest(int type, Object chargeUtile) {
		this.type = type;
		this.chargeUtile = chargeUtile;
		oos = null;
		response = null;
	}

	/**
	 * Sends the Response back to the client.
	 *
	 * @param s : the client Socket
	 * @param res : the Response to send
	 */
	protected void send(Socket s, Response res) {
		try {
			if (oos == null) oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(res);
			oos.flush();
		} catch (IOException e) {
			System.err.println("Error - send : " + e.getMessage());
		}
	}

	//The handleXXX functions build the response to be sent to the client [the send is called in createRunnable]
	/**
	 * Builds the response to send when the type is unknown or unauthorized
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 * @param message : the reason for failure
	 */
	protected void handleUnknownOrFail(Socket s, ConsoleServer cs, String message) {
		System.out.println("requestresponse.multithread.abstracts.AbstractRequest.handleUnknownOrFail()");
		cs.TraceEvents(s.getRemoteSocketAddress().toString() + "#" + message + "#" + Thread.currentThread().getName());
		response = new BasicResponse(BasicResponse.BAD_STATE, message);
	}

	/**
	 * Verifies the login informations and builds the response to be sent to the
	 * client (expects a login, a "salted" password and the int and long that
	 * were used in the digest)
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 * @return a boolean representing the success (or not) of the login
	 */
	protected boolean handleLogin(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.multithread.abstracts.AbstractRequest.handleLogin()");
		Object cu[] = (Object[]) getChargeUtile();
		String login = (String) cu[0];
		cs.TraceEvents(s.getRemoteSocketAddress().toString() + "#LOGIN pour " + login
			+ "#" + Thread.currentThread().getName());

		byte[] saltedPwd = (byte[]) cu[1];
		Agent a = DAOFactoryAirport.getInstance().getAgentDAO().selectOne(login);
		if (a != null) {
			String pwd = a.getPassword();
			int rand = (int) cu[2];
			long time = (long) cu[3];
			if (CryptoUtilities.verifyDigest(saltedPwd, pwd, rand, time)) {
				String certificateEntry = DAOFactoryAirport.getInstance()
					.getEmployerDAO().getCertificateEntry(login);
				response = new BasicResponse(BasicResponse.LOGIN_OK, certificateEntry);
				return true;
			}
		}
		response = new BasicResponse(BasicResponse.LOGIN_FAIL, "No such Agent");
		return false;
	}

	/**
	 * Disconnect from server
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 * @return Logged state : false if logged out, true if still logged in
	 */
	protected boolean handleLogout(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.multithread.abstracts.AbstractRequest.handleLogout()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#LOGOUT#" + Thread.currentThread().getName());
		response = new BasicResponse(BasicResponse.LOGOUT_OK, "");
		return false;
	}

	//<editor-fold defaultstate="collapsed" desc="Getters&Setters">
	/**
	 *
	 * @return the type of the Request (defined constant)
	 */
	@Override
	public int getType() {
		return this.type;
	}
	
	/**
	 *
	 * @return the "message" that was set in the constructor
	 */
	public Object getChargeUtile() {
		return chargeUtile;
	}
	
	/**
	 * Gives access to the "session" variables contained in Connection.
	 *
	 * @return the requestresponse.multithread.Connection that handles this client
	 */
	public Connection getParent() {
		return parent;
	}
	
	/**
	 * Sets the Connection that handles all the Request from this client.
	 * Used in the Connection run()
	 *
	 * @param parent: a requestresponse.multithread.Connection
	 */
	public void setParent(Connection parent) {
		this.parent = parent;
	}
	//</editor-fold>

}

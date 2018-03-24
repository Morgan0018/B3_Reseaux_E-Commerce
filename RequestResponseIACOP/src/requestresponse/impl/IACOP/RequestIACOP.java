package requestresponse.impl.IACOP;

import crypto.utilities.CryptoUtilities;
import db.airport.DAOFactoryAirport;
import db.airport.models.Agent;
import db.airport.models.Ticket;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Properties;
import requestresponse.multithread.abstracts.AbstractRequest;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public class RequestIACOP extends AbstractRequest {

	//Protocol constants
	public final static int LOGIN_GROUP = 2;

	public final static String EMPLOYEE = "employee";
	public final static String TRAVELER = "traveler";
	
	public static int numClient = 0;

	//Variables
	private String host;
	private int port;

	/**
	 *
	 * @param type
	 * @param chargeUtile
	 */
	public RequestIACOP(int type, Object chargeUtile) {
		super(type, chargeUtile);
	}

	/**
	 *
	 */
	private void loadHostAndPortChat() {
		System.out.println("requestresponse.impl.IACOP.RequestIACOP.loadHostAndPortChat()");
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "iachat.properties"));
			host = p.getProperty("HOST_UDP");
			port = Integer.parseInt(p.getProperty("PORT_CHAT"));
		} catch (FileNotFoundException ex) {
			System.err.println("Error FileNotFoundException : " + ex.getMessage());
		} catch (IOException ex) {
			System.err.println("Error IOException : " + ex.getMessage());
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
				response = null;
				switch (type) {
					case LOGIN_GROUP:
						handleLoginGroup(s, cs);
						break;
					default:
						handleUnknownOrFail(s, cs, "unknown command");
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
	private void handleLoginGroup(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.IACOP.RequestIACOP.handleLoginGroup()");
		Object[] cu = (Object[]) getChargeUtile();
		String t = (String) cu[0];
		cs.TraceEvents(s.getRemoteSocketAddress().toString() + "#LOGIN_GROUP "
			+ t + "#" + Thread.currentThread().getName());
		boolean ok = false;
		if (t.equalsIgnoreCase(EMPLOYEE)) {
			String login = (String) cu[1];
			byte[] saltedPwd = (byte[]) cu[2];
			Agent a = DAOFactoryAirport.getInstance().getAgentDAO().selectOne(login);
			if (a != null) {
				String pwd = a.getPassword();
				int rand = (int) cu[3];
				long time = (long) cu[4];
				byte[] localDigest = CryptoUtilities.makeDigest(pwd, rand, time);
				if (MessageDigest.isEqual(saltedPwd, localDigest)) ok = true;
			}
		} else if (t.equalsIgnoreCase(TRAVELER)) {
			String idTicket = (String) cu[1];
			Ticket ticket = DAOFactoryAirport.getInstance().getTicketDAO().selectOne(idTicket);
			if (ticket != null) ok = true;
		}
		if (ok) {
			loadHostAndPortChat();
			String toSend = host + "#" + port + "#" + numClient++;
			response = new ResponseIACOP(ResponseIACOP.LOGIN_GROUP_OK, toSend);
		} else response = new ResponseIACOP(ResponseIACOP.LOGIN_GROUP_FAIL, "No such Agent or Ticket");
	}

}

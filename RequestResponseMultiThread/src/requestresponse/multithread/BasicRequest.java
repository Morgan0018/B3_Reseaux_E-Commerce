package requestresponse.multithread;

import java.net.Socket;
import requestresponse.multithread.abstracts.AbstractRequest;
import static requestresponse.multithread.abstracts.AbstractRequest.LOGIN;
import static requestresponse.multithread.abstracts.AbstractRequest.LOGOUT;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public class BasicRequest extends AbstractRequest {

	/**
	 * 
	 * @param type
	 * @param chargeUtile
	 */
	public BasicRequest(int type, Object chargeUtile) {
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
						default:
							handleUnknownOrFail(s, cs, "Unknown Command");
					}
				}
				send(s, response);
			}
		};
	}

}

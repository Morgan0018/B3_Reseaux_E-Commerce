package requestresponse.impl.SEBATRAP;

import java.net.Socket;
import java.util.Random;
import requestresponse.multithread.abstracts.AbstractRequest;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public class RequestSEBATRAP extends AbstractRequest {

	//Protocol constants
	public final static int VERIFY_CARD = 2;
	public final static int CHARGE_CARD = 3;

	/**
	 * 
	 * @param type
	 * @param chargeUtile 
	 */
	public RequestSEBATRAP(int type, Object chargeUtile) {
		super(type, chargeUtile);
		oos = null;
		response = null;
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
					case VERIFY_CARD:
						handleVerifyCard(s, cs);
						break;
					case CHARGE_CARD:
						handleChargeCard(s, cs);
						break;
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
	private void handleVerifyCard(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.SEBTRAP.RequestSEBTRAP.handleVerifyCard()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#VERIFY_CARD#" + Thread.currentThread().getName());
		Object rcv[] = (Object[]) getChargeUtile();
		String ownerName = (String) rcv[0];
		String cardNumber = (String) rcv[1];
		float price = (float) rcv[2];
		System.out.println("Received : " + ownerName + " | " + cardNumber + " | " + price);
		int r = (new Random()).nextInt();
		if ((r % 3) == 0) response = new ResponseSEBATRAP(ResponseSEBATRAP.VERIFY_CARD_FAIL, "");
		else response = new ResponseSEBATRAP(ResponseSEBATRAP.VERIFY_CARD_OK, "This is a placeholder");
	}

	/**
	 * 
	 * @param s
	 * @param cs 
	 */
	private void handleChargeCard(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.SEBTRAP.RequestSEBTRAP.handleChargeCard()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#CHARGE_CARD#" + Thread.currentThread().getName());
		Object token = getChargeUtile();
		System.out.println("Received : " + token);
		int r = (new Random()).nextInt();
		if ((r % 5) == 0) response = new ResponseSEBATRAP(ResponseSEBATRAP.CHARGE_CARD_FAIL, "");
		else response = new ResponseSEBATRAP(ResponseSEBATRAP.CHARGE_CARD_OK, "");
	}

}

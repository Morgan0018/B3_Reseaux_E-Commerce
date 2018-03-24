package requestresponse.impl.TICKMAP;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * @author Morgan
 */
public class ResponseTICKMAP extends AbstractResponse {
	
	//OK
	public final static int HANDSHAKE_OK = 202;
	public final static int GET_FLIGHTS_OK = 203;
	public final static int CHOOSE_FLIGHT_OK = 204;
	public final static int HOLD_TICKETS_OK = 205;
	public final static int CONFIRM_TICKETS_NO_OK = 206;
	public final static int CONFIRM_TICKETS_YES_OK = 306;
	public final static int CONFIRM_PAYMENT_NO_OK = 207;
	public final static int CONFIRM_PAYMENT_YES_OK = 307;
	//ERROR
	public final static int HANDSHAKE_FAIL = 402;
	public final static int GET_FLIGHTS_FAIL = 403;
	public final static int CHOOSE_FLIGHT_FAIL = 404;
	public final static int HOLD_TICKETS_FAIL = 405;
	public final static int CONFIRM_TICKETS_FAIL = 406;
	public final static int CONFIRM_PAYMENT_FAIL = 407;

	/**
	 * Constructor.
	 * @param returnCode : the response code (use defined constants)
	 * @param chargeUtile: the "message" to transmit
	 */
	public ResponseTICKMAP(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

package requestresponse.impl.LUGAP;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * Implementation of Response for the LUGAP protocol.
 * @author Morgan
 */
public class ResponseLUGAP extends AbstractResponse {

	//OK
	public final static int GET_FLIGHTS_OK = 202;
	public final static int CHOOSE_FLIGHT_OK = 203;
	public final static int RECEIVE_LUGGAGE_OK = 204;
	public final static int CUSTOM_CHECK_OK = 205;
	public final static int LOAD_LUGGAGE_OK = 206;
	public final static int ADD_COMMENT_OK = 207;
	//ERROR
	public final static int GET_FLIGHTS_FAIL = 402;
	public final static int CHOOSE_FLIGHT_FAIL = 403;
	public final static int RECEIVE_LUGGAGE_FAIL = 404;
	public final static int CUSTOM_CHECK_FAIL = 405;
	public final static int LOAD_LUGGAGE_FAIL = 406;
	public final static int ADD_COMMENT_FAIL = 407;

	/**
	 * Constructor.
	 * @param returnCode : the response code (use defined constants)
	 * @param chargeUtile: the "message" to transmit
	 */
	public ResponseLUGAP(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

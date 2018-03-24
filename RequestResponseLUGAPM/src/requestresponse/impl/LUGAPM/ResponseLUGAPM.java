package requestresponse.impl.LUGAPM;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * @author Morgan
 */
public class ResponseLUGAPM extends AbstractResponse {

	//OK
	public final static int CHOOSE_FLIGHT_OK = 202;
	public final static int RECEIVED_AND_LOADED_OK = 203;
	//ERROR
	public final static int CHOOSE_FLIGHT_FAIL = 402;
	public final static int RECEIVED_AND_LOADED_FAIL = 403;

	/**
	 * Constructor.
	 * @param returnCode : the response code (use defined constants)
	 * @param chargeUtile: the "message" to transmit
	 */
	public ResponseLUGAPM(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

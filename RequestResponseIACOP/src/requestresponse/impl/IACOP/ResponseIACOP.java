package requestresponse.impl.IACOP;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * @author Morgan
 */
public class ResponseIACOP extends AbstractResponse {
	
	//OK
	public final static int LOGIN_GROUP_OK = 202;
	//ERROR
	public final static int LOGIN_GROUP_FAIL = 402;

	/**
	 * Constructor
	 *
	 * @param returnCode
	 * @param chargeUtile
	 */
	public ResponseIACOP(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

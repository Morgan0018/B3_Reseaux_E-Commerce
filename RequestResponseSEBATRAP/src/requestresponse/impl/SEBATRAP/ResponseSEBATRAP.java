package requestresponse.impl.SEBATRAP;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * @author Morgan
 */
public class ResponseSEBATRAP extends AbstractResponse {
	
	//OK
	public final static int VERIFY_CARD_OK = 202;
	public final static int CHARGE_CARD_OK = 203;
	//ERROR
	public final static int VERIFY_CARD_FAIL = 402;
	public final static int CHARGE_CARD_FAIL = 403;

	/**
	 * Constructor
	 *
	 * @param returnCode
	 * @param chargeUtile
	 */
	public ResponseSEBATRAP(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

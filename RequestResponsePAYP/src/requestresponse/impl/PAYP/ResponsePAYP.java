package requestresponse.impl.PAYP;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * @author Morgan
 */
public class ResponsePAYP extends AbstractResponse {

	//OK
	public final static int MAKE_PAYMENT_OK = 202;
	//ERROR
	public final static int MAKE_PAYMENT_FAIL = 402;

	/**
	 * Constructor
	 *
	 * @param returnCode
	 * @param chargeUtile
	 */
	public ResponsePAYP(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

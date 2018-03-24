package requestresponse.impl.LUGANAPM;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * Implementation of Response for the LUGANAPM protocol.
 *
 * @author Morgan
 */
public class ResponseLUGANAPM extends AbstractResponse {

	//OK
	public final static int REG_CORR_LUG_M_OK = 202;
	public final static int ANOVA_1_LUG_M_OK = 203;
	//ERROR
	public final static int REG_CORR_LUG_M_FAIL = 402;
	public final static int ANOVA_1_LUG_M_FAIL = 403;

	/**
	 * Constructor.
	 *
	 * @param returnCode : the response code (use defined constants)
	 * @param chargeUtile: the "message" to transmit
	 */
	public ResponseLUGANAPM(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

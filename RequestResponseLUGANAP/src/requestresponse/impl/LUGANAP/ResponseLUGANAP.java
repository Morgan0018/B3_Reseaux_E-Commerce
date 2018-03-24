package requestresponse.impl.LUGANAP;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * Implementation of Response for the LUGANAP protocol.
 *
 * @author Morgan
 */
public class ResponseLUGANAP extends AbstractResponse {

	//OK
	public final static int GET_AIRLINES_OK = 202;
	public final static int REG_CORR_LUG_OK = 203;
	public final static int REG_CORR_LUG_PLUS_OK = 204;
	public final static int ANOVA_1_LUG_OK = 205;
	public final static int ANOVA_2_LUG_HF_OK = 206;
	//ERROR
	public final static int GET_AIRLINES_FAIL = 402;
	public final static int REG_CORR_LUG_FAIL = 403;
	public final static int REG_CORR_LUG_PLUS_FAIL = 404;
	public final static int ANOVA_1_LUG_FAIL = 405;
	public final static int ANOVA_2_LUG_HF_FAIL = 406;

	/**
	 * Constructor.
	 *
	 * @param returnCode : the response code (use defined constants)
	 * @param chargeUtile: the "message" to transmit
	 */
	public ResponseLUGANAP(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

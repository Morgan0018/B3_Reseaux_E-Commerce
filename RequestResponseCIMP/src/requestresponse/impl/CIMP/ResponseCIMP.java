package requestresponse.impl.CIMP;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * @author Morgan
 */
public class ResponseCIMP extends AbstractResponse {
	
	//<editor-fold defaultstate="collapsed" desc="Protocol Constant">
	//OK
	public final static int FLIGHT_INFO_OK = 202;
	public final static int CHECK_TICKET_OK = 203;
	public final static int SAVE_LUGGAGE_OK = 204;
	//ERROR
	public final static int FLIGHT_INFO_FAIL = 402;
	public final static int CHECK_TICKET_FAIL = 403;
	public final static int SAVE_LUGGAGE_FAIL = 404;
	//</editor-fold>

	/**
	 * 
	 * @param returnCode
	 * @param chargeUtile 
	 */
	public ResponseCIMP(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public String toString() {
		return returnCode + "$" + chargeUtile;
	}

}

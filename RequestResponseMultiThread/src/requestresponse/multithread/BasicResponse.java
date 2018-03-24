package requestresponse.multithread;

import requestresponse.multithread.abstracts.AbstractResponse;

/**
 * @author Morgan
 */
public class BasicResponse extends AbstractResponse {

	public BasicResponse(int returnCode, Object chargeUtile) {
		super(returnCode, chargeUtile);
	}

}

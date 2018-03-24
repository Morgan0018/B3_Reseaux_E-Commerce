package requestresponse.multithread.abstracts;

import java.io.Serializable;
import requestresponse.multithread.interfaces.Response;

/**
 * An abstract class that implements Response and Serializable and contains
 * constants and variables needed by all the implementations of Response
 * @author Morgan
 */
public abstract class AbstractResponse implements Serializable, Response {

	//OK
	public final static int LOGOUT_OK = 200;
	public final static int LOGIN_OK = 201;
	//ERROR
	public final static int LOGOUT_FAIL = 500;
	public final static int LOGIN_FAIL = 501;
	public final static int BAD_STATE = 600;
	//Variables
	protected final int returnCode;
	protected Object chargeUtile;

	/**
	 * Constructor
	 *
	 * @param returnCode : the response code (use defined constants)
	 * @param chargeUtile : the "message" to transmit
	 */
	public AbstractResponse(int returnCode, Object chargeUtile) {
		this.returnCode = returnCode;
		this.chargeUtile = chargeUtile;
	}

	/**
	 *
	 * @return an int (the code : usually one of the constants)
	 */
	@Override
	public int getCode() {
		return returnCode;
	}

	/**
	 *
	 * @return an Object (the "message")
	 */
	public Object getChargeUtile() {
		return chargeUtile;
	}

	/**
	 * 
	 * @param chargeUtile
	 */
	public void setChargeUtile(Object chargeUtile) {
		this.chargeUtile = chargeUtile;
	}

}

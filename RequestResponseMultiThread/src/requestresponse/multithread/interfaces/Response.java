package requestresponse.multithread.interfaces;

/**
 * @author Morgan
 */
public interface Response {

	/**
	 * Returns the code of the response (defined in the protocol)
	 * @return an int (usually a constant)
	 */
	public int getCode();

}

package requestresponse.multithread.interfaces;

import java.net.Socket;

/**
 * @author Morgan
 */
public interface Request {

	/**
	 * Returns the type of the request (defined in the protocol)
	 * @return an int (usually a constant)
	 */
	public int getType();

	/**
	 * 
	 * @param s : a client Socket
	 * @param cs : an Object implementing ConsoleServer
	 * @return a Runnable
	 */
	public Runnable createRunnable(Socket s, ConsoleServer cs);

}

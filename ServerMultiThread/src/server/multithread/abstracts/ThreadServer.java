package server.multithread.abstracts;

import java.net.ServerSocket;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public abstract class ThreadServer extends Thread {
	protected final  String name;
	protected final int port;
	protected final ConsoleServer applicGUI;
	protected ServerSocket serverSocket; //socket d'écoute

	/**
	 * Constructor
	 * @param name : the server name (for logging)
	 * @param port : for the ServerSocket
	 * @param applicGUI : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 */
	protected ThreadServer(String name, int port, ConsoleServer applicGUI){
		this.name = name;
		this.port = port;
		this.applicGUI = applicGUI;
	}

	/**
	 * Contains the operations necessary to stop the server cleanly
	 */
	public abstract void doStop();

}

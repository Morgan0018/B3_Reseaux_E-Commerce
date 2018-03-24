package server.multithread.extend;

import server.multithread.abstracts.ThreadServerMultiClient;
import java.net.Socket;
import requestresponse.multithread.connection.SecureConnection;
import requestresponse.multithread.interfaces.ConsoleServer;
import server.multithread.interfaces.SourceTasks;

/**
 * @author Morgan
 */
public class ThreadServerMultiConnectionSecure extends ThreadServerMultiClient {

	/**
	 * Constructor
	 *
	 * @param name
	 * @param port
	 * @param applicGUI
	 * @param toDoTasks
	 */
	public ThreadServerMultiConnectionSecure(String name, int port, ConsoleServer applicGUI, SourceTasks toDoTasks) {
		super(name, port, applicGUI, toDoTasks);
	}

	/**
	 * 
	 * @param clientSocket 
	 */
	@Override
	protected void addTaskToSource(Socket clientSocket) {
		toDoTasks.recordTask(new SecureConnection(clientSocket, applicGUI, name));
	}
	
}

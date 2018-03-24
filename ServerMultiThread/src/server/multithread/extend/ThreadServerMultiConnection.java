package server.multithread.extend;

import java.net.Socket;
import requestresponse.multithread.connection.Connection;
import requestresponse.multithread.interfaces.ConsoleServer;
import server.multithread.abstracts.ThreadServerMultiClient;
import server.multithread.interfaces.SourceTasks;

/**
 * @author Morgan
 */
public class ThreadServerMultiConnection extends ThreadServerMultiClient {

	/**
	 * 
	 * @param name
	 * @param port
	 * @param applicGUI
	 * @param toDoTasks 
	 */
	public ThreadServerMultiConnection(String name, int port, ConsoleServer applicGUI, SourceTasks toDoTasks) {
		super(name, port, applicGUI, toDoTasks);
	}

	/**
	 * 
	 * @param clientSocket 
	 */
	@Override
	protected void addTaskToSource(Socket clientSocket) {
		toDoTasks.recordTask(new Connection(clientSocket, applicGUI, name));
	}

}

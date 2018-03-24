package server.multithread.extend;

import server.multithread.abstracts.ThreadServerMultiClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import requestresponse.multithread.interfaces.ConsoleServer;
import requestresponse.multithread.interfaces.Request;
import server.multithread.interfaces.SourceTasks;

/**
 * @author Morgan
 */
public class ThreadServerMultiRequest extends ThreadServerMultiClient {

	/**
	 * Constructor
	 *
	 * @param name
	 * @param port
	 * @param applicGUI
	 * @param toDoTasks
	 */
	public ThreadServerMultiRequest(String name, int port, ConsoleServer applicGUI, SourceTasks toDoTasks) {
		super(name, port, applicGUI, toDoTasks);
	}

	/**
	 *
	 * @param clientSocket
	 */
	@Override
	protected void addTaskToSource(Socket clientSocket) {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			Request req = (Request) ois.readObject();
			toDoTasks.recordTask(req.createRunnable(clientSocket, applicGUI));
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error : " + e.getMessage());
		}
	}

}

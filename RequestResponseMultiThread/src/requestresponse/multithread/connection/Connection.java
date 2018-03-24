package requestresponse.multithread.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import requestresponse.multithread.abstracts.AbstractRequest;
import requestresponse.multithread.interfaces.ConsoleServer;
import requestresponse.multithread.interfaces.Request;

/**
 * A class that implements runnable and serves a "session" for the communication between client and server
 *
 * @author Morgan
 */
public class Connection implements Runnable {

	//<editor-fold defaultstate="collapsed" desc="Variables">
	protected final Socket clientSocket;
	protected final ConsoleServer cs;
	protected final String name;
	protected boolean loggedIn;
	//</editor-fold>

	/**
	 * Constructor
	 *
	 * @param s    : The client socket obtained with accept
	 * @param cs   : an Object implementing ConsoleServer
	 * @param name : the name of the server (used for logging)
	 */
	public Connection(Socket s, ConsoleServer cs, String name) {
		this.clientSocket = s;
		this.cs = cs;
		this.name = name;
		loggedIn = false;
	}

	/**
	 * Loops to read and run the Requests sent by the client until the client socket closes
	 */
	@Override
	public void run() {
		try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
			do {
				Request request = (Request) ois.readObject();
				System.out.println("Requete lue par le serveur " + name
					+ " , instance de " + request.getClass().getName()
					+ " | type : " + request.getType());
				((AbstractRequest) request).setParent(this);
				request.createRunnable(clientSocket, cs).run();
			} while (!clientSocket.isClosed());
		} catch (ClassNotFoundException e) {
			System.err.println("Error in Connection run : " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error in Connection (" + name + ") - run - IOException - " + e.getMessage());
		} catch (NullPointerException e) {
			System.err.println("Request was null : " + e.getMessage());
		}
		System.out.println("Server " + name + " done with " + clientSocket.getInetAddress());
	}

	//<editor-fold defaultstate="collapsed" desc="Getters&Setters">
	/**
	 *
	 * @return The logged in state as a boolean
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	/**
	 * Sets the logged in state
	 *
	 * @param loggedIn
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	//</editor-fold>

}

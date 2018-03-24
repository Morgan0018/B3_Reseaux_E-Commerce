package server.multithread.extend;

import server.multithread.abstracts.ThreadServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import requestresponse.multithread.interfaces.ConsoleServer;
import requestresponse.multithread.interfaces.Request;

/**
 * @author Morgan
 */
public class ThreadServerChat extends ThreadServer {
	
	/**
	 * 
	 * @param name
	 * @param port
	 * @param applicGUI 
	 */
	public ThreadServerChat(String name, int port, ConsoleServer applicGUI) {
		super(name, port, applicGUI);
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			applicGUI.TraceEvents("Server " + name + "#" + serverSocket.getLocalSocketAddress() + "#ThreadServer.run()");
		} catch (IOException e) {
			System.err.println("Error on listening port : " + e.getMessage());
			System.exit(1);
		}	
		//Wait for client
		Socket clientSocket = null;
		while (!isInterrupted()) {
			try {
				System.out.println("Server " + name + " waiting...");
				clientSocket = serverSocket.accept();
				applicGUI.TraceEvents(clientSocket.getRemoteSocketAddress().toString() + "#accept : "
					+ clientSocket.getRemoteSocketAddress() + "#thread server " + name);
			} catch (IOException e) {
				System.err.println("Accept error : " + e.getMessage());
				break;
			}
			try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
				Request req = (Request) ois.readObject();
				req.createRunnable(clientSocket, applicGUI).run();
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error : " + e.getMessage());
				break;
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public void doStop() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Error : " + e.getMessage());
		}
		System.out.println("Server " + name + " disconnected");
		this.interrupt();
	}

}

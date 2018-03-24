package server.multithread.abstracts;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Vector;
import requestresponse.multithread.interfaces.ConsoleServer;
import server.multithread.ThreadClient;
import server.multithread.interfaces.SourceTasks;

/**
 * A class the waits for clients to connect and delegates the work to a ClientThread from a pool
 * @author Morgan
 */
public abstract class ThreadServerMultiClient extends ThreadServer {

	protected final SourceTasks toDoTasks;
	protected final Vector<ThreadClient> pool;
	protected int poolSize;
	protected Properties p;

	/**
	 * Constructor
	 * Reads the thread pool size from "servermultithread.properties" situated in the current directory
	 * @param name : the server name (for logging)
	 * @param port : for the ServerSocket
	 * @param applicGUI : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 * @param toDoTasks : an Object implementing server.multithread.interfaces.SourceTasks
	 */
	public ThreadServerMultiClient(String name, int port, ConsoleServer applicGUI, SourceTasks toDoTasks) {
		super(name, port, applicGUI);
		this.toDoTasks = toDoTasks;
		this.pool = new Vector<>();
		try {
			p = new Properties();
			String sep = System.getProperty("file.separator");
			p.load(new FileInputStream(System.getProperty("user.dir") + sep + ".."
				+ sep + "ServerMultiThread" + sep + "servermultithread.properties"));
			poolSize = Integer.parseInt(p.getProperty("POOL_SIZE"));
		} catch (IOException | NumberFormatException e) {
			System.err.println("Error : " + e.getMessage());
		}
	}

	/**
	 * Creates the ServerSocket and the pool of ClientThread.
	 * Loops on accept and hands the client to a thread of the pool via the SourceTasks
	 * The Object placed in the SourceTasks is a requestresponse.multithread.Connection
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
		//Start pool
		poolStart();
		//Wait for client(s)
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
			//Add task to list for ThreadClient
			addTaskToSource(clientSocket);
		}
	}
	
	/**
	 * 
	 */
	protected void poolStart() {
		for (int i = 0; i < poolSize; i++) {
			ThreadClient threadClient = new ThreadClient(toDoTasks, "PoolThread " + name + " n°" + i);
			pool.add(threadClient);
			threadClient.start();
		}
	}
	
	/**
	 * 
	 */
	protected void poolStop() {
		for (ThreadClient threadClient : pool) {
			threadClient.interrupt();
			try {
				threadClient.join();
			} catch (InterruptedException e) {
				System.err.println("Interruption : " + e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @param clientSocket 
	 */
	protected abstract void addTaskToSource(Socket clientSocket);

	/**
	 * Interrupts all the pool threads before closing the ServerSocket
	 */
	@Override
	public void doStop() {
		poolStop();
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Error : " + e.getMessage());
		}
		System.out.println("Server " + name + " disconnected");
		this.interrupt();
	}

}

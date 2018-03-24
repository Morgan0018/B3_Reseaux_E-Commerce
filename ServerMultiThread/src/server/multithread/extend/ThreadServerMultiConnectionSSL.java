package server.multithread.extend;

import crypto.utilities.SSLUtilities;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.*;
import requestresponse.multithread.connection.Connection;
import requestresponse.multithread.interfaces.ConsoleServer;
import server.multithread.abstracts.ThreadServerMultiClient;
import server.multithread.interfaces.SourceTasks;

/**
 * @author Morgan
 */
public class ThreadServerMultiConnectionSSL extends ThreadServerMultiClient {
	
	private SSLServerSocket sslServerSocket;
	private final char[] pwd = "azerty".toCharArray();

	/**
	 * 
	 * @param name
	 * @param port
	 * @param applicGUI
	 * @param toDoTasks 
	 */
	public ThreadServerMultiConnectionSSL(String name, int port, ConsoleServer applicGUI, SourceTasks toDoTasks) {
		super(name, port, applicGUI, toDoTasks);
		this.sslServerSocket = null;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		String ksFileName = System.getProperty("user.dir") +
			System.getProperty("file.separator") + p.getProperty("KEYSTORE");
		//Create SSLServerSocket
		sslServerSocket = SSLUtilities.getSSLServerSocket(ksFileName, pwd, port);
		applicGUI.TraceEvents("Server " + name + "#" + sslServerSocket
			.getLocalSocketAddress() + "#ThreadServer.run()");
		//Start pool
		poolStart();
		//Wait for client(s)
		SSLSocket sslClientSocket = null;
		while (!isInterrupted()) {
			try {
				System.out.println("Server " + name + " waiting...");
				sslClientSocket = (SSLSocket) sslServerSocket.accept();
				applicGUI.TraceEvents(sslClientSocket.getRemoteSocketAddress().toString()
					+ "#accept : " + sslClientSocket.getRemoteSocketAddress()
					+ "#thread server " + name);
			} catch (IOException e) {
				System.err.println("Accept error : " + e.getMessage());
				break;
			}
			//Add task to list for ThreadClient
			addTaskToSource(sslClientSocket);
		}
	}

	/**
	 * 
	 * @param clientSocket 
	 */
	@Override
	protected void addTaskToSource(Socket clientSocket) {
		toDoTasks.recordTask(new Connection(clientSocket, applicGUI, name));
	}

	/**
	 * 
	 */
	@Override
	public void doStop() {
		poolStop();
		try {
			sslServerSocket.close();
		} catch (IOException e) {
			System.err.println("Error : " + e.getMessage());
		}
		System.out.println("Server " + name + " disconnected");
		this.interrupt();
	}

}

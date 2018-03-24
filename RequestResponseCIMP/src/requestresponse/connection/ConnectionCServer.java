package requestresponse.connection;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import requestresponse.impl.CIMP.RequestCIMP;
import requestresponse.multithread.connection.Connection;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public class ConnectionCServer extends Connection {
	
	private DataOutputStream dos;

	/**
	 *
	 * @param s
	 * @param cs
	 * @param name
	 */
	public ConnectionCServer(Socket s, ConsoleServer cs, String name) {
		super(s, cs, name);
	}

	/**
	 * 
	 * @return 
	 */
	public DataOutputStream getDataOutputStream() {
		return dos;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		try (DataInputStream dis = new DataInputStream(
			new BufferedInputStream(clientSocket.getInputStream()))) {
			dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			
			StringBuilder sb = new StringBuilder();
			String msg;
			StringTokenizer st;
			do {
				sb.setLength(0);
				byte b = 0;
				//receive data
				while ((b = dis.readByte()) != (byte) '\n') {
					if (b != '\n') sb.append((char) b);
				}
				msg = sb.toString().trim();
				System.out.println("Received : " + msg);
				//transform data
				st = new StringTokenizer(msg, "$");
				int code = Integer.parseInt(st.nextToken());
				String cu = st.nextToken();
				//create & run request
				RequestCIMP req = new RequestCIMP(code, cu);
				req.setParent(this);
				req.createRunnable(clientSocket, cs).run();
			} while (!clientSocket.isClosed());
		} catch (IOException ex) {
			System.err.println("Error in ConnectionCServer (" + name + ") - IOException - " + ex.getMessage());
		}
		System.out.println("Server " + name + " done with " + clientSocket.getInetAddress());
	}

}

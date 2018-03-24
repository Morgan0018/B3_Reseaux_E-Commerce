package application.jiachat;

import java.awt.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * @author Morgan
 */
public class ThreadRcv extends Thread {
	
	private final String name;
	private final MulticastSocket groupSocket;
	private final List msgReceived;

	/**
	 * 
	 * @param name
	 * @param groupSocket
	 * @param msgReceived 
	 */
	public ThreadRcv(String name, MulticastSocket groupSocket, List msgReceived) {
		this.name = name;
		this.groupSocket = groupSocket;
		this.msgReceived = msgReceived;
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		boolean running = true;
		while (running) {			
			try {
				byte[] buf = new byte[1000];
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				groupSocket.receive(dp);
				msgReceived.add(new String(buf).trim());
			} catch (IOException e) {
				System.err.println("Error in ThreadRcv : " + e.getMessage());
				running = false;
			}
		}
	}

}

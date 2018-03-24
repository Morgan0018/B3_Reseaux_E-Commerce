package requestresponse.multithread.connection;

import java.net.Socket;
import javax.crypto.SecretKey;
import requestresponse.multithread.interfaces.ConsoleServer;

/**
 * @author Morgan
 */
public class SecureConnection extends Connection {
	
	private SecretKey HMACSecretKey, cryptSecretKey;

	/**
	 * Constructor
	 *
	 * @param s    : The client socket obtained with accept
	 * @param cs   : an Object implementing ConsoleServer
	 * @param name : the name of the server (used for logging)
	 */
	public SecureConnection(Socket s, ConsoleServer cs, String name) {
		super(s, cs, name);
	}

	//<editor-fold defaultstate="collapsed" desc="Getters&Setters">
	public SecretKey getHMACSecretKey() {
		return HMACSecretKey;
	}
	
	public void setHMACSecretKey(SecretKey HMACSecretKey) {
		this.HMACSecretKey = HMACSecretKey;
	}
	
	public SecretKey getCryptSecretKey() {
		return cryptSecretKey;
	}
	
	public void setCryptSecretKey(SecretKey cryptSecretKey) {
		this.cryptSecretKey = cryptSecretKey;
	}
	//</editor-fold>
	
}

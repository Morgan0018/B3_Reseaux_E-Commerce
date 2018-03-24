package requestresponse.impl.PAYP;

import crypto.utilities.CryptoUtilities;
import crypto.utilities.SSLUtilities;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.Properties;
import javax.net.ssl.SSLSocket;
import requestresponse.impl.SEBATRAP.RequestSEBATRAP;
import requestresponse.impl.SEBATRAP.ResponseSEBATRAP;
import requestresponse.multithread.interfaces.ConsoleServer;
import requestresponse.multithread.interfaces.Request;
import requestresponse.multithread.interfaces.Response;

/**
 * @author Morgan
 */
public class RequestPAYP implements Serializable, Request {

	//Protocol constants
	public final static int MAKE_PAYMENT = 2;

	//<editor-fold defaultstate="collapsed" desc="Variables">
	//Crypto
	private final String alias = "SerPay";
	private final char[] pwd = "azerty".toCharArray();
	private final String signAlgorithm = "SHA1withRSA";
	private final String asymetricAlgorithm = "RSA/ECB/PKCS1Padding";
	//
	protected int type;
	protected Object chargeUtile;
	protected ResponsePAYP response;
	//SEBTRAP
	private String ipAddrSBTP;
	private int portSBTP;
	private SSLSocket ssls;
	private ObjectOutputStream oos;
	//</editor-fold>

	/**
	 * Constructor
	 *
	 * @param type
	 * @param chargeUtile
	 */
	public RequestPAYP(int type, Object chargeUtile) {
		this.type = type;
		this.chargeUtile = chargeUtile;
		response = null;
		ssls = null;
	}

	//<editor-fold defaultstate="collapsed" desc="Getters">
	/**
	 *
	 * @return the type of the Request (defined constant)
	 */
	@Override
	public int getType() {
		return this.type;
	}

	/**
	 *
	 * @return the "message" that was set in the constructor
	 */
	public Object getChargeUtile() {
		return chargeUtile;
	}
	//</editor-fold>

	/**
	 * Sends the Response back to the client.
	 *
	 * @param s   : the client Socket
	 * @param res : the Response to send
	 */
	protected void send(Socket s, Response res) {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream())) {
			outputStream.writeObject(res);
			outputStream.flush();
		} catch (IOException e) {
			System.err.println("Error - send : " + e.getMessage());
		}
	}

	/**
	 * Creates a Runnable that will call a different method depending on the type
	 *
	 * @param s  : the client Socket
	 * @param cs : an Object implementing requestresponse.multithread.interfaces.ConsoleServer
	 *
	 * @return a Runnable
	 */
	@Override
	public Runnable createRunnable(Socket s, ConsoleServer cs) {
		return new Runnable() {
			@Override
			public void run() {
				response = null;
				switch (type) {
					case MAKE_PAYMENT:
						handleMakePayment(s, cs);
						break;
				}
				send(s, response);
			}
		};
	}

	/**
	 *
	 * @param s
	 * @param cs
	 */
	@SuppressWarnings("ImplicitArrayToString")
	private void handleMakePayment(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.PAYP.RequestPAYP.handleMakePayment()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#MAKE_PAYMENT#" + Thread.currentThread().getName());
		Object rcv[] = (Object[]) getChargeUtile();
		Object cu[] = (Object[]) rcv[0];
		byte[] cryptedCardNumber = (byte[]) cu[0];
		String ownerName = (String) cu[1];
		float price = (float) cu[2];
		String certificateEntry = (String) cu[3];
		System.out.println("Received : " + cryptedCardNumber + " | " + ownerName
			+ " | " + price + " | " + certificateEntry);
		String ksFileName = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "ksSerPay";
		PublicKey pk = CryptoUtilities
			.getPublicKeyfromKeystoreCertificate(ksFileName, pwd, certificateEntry);
		byte[] sign = (byte[]) rcv[1];
		if (CryptoUtilities.verifySignature(sign, cu, signAlgorithm, pk)) {
			PrivateKey prk = CryptoUtilities.getPrivateKeyFromKeystore(ksFileName, pwd, alias);
			String cardNumber = (String) CryptoUtilities
				.asymmetricDecryption(cryptedCardNumber, asymetricAlgorithm, prk);
			if (cardNumber == null) response = new ResponsePAYP(
					ResponsePAYP.MAKE_PAYMENT_FAIL, "Failed to decrypt card number");
			else {
				System.out.println("Decrypted card number : " + cardNumber);
				if (verifyCard(ownerName, cardNumber, price))
					response = new ResponsePAYP(ResponsePAYP.MAKE_PAYMENT_OK, "");
				else response = new ResponsePAYP(ResponsePAYP.MAKE_PAYMENT_FAIL,
						"Payment Declined");
			}
		} else response = new ResponsePAYP(ResponsePAYP.MAKE_PAYMENT_FAIL,
				"Signature verification failed");
	}

	//<editor-fold defaultstate="collapsed" desc="SEBTRAP">
	/**
	 *
	 * @param req
	 *
	 * @return
	 */
	private ResponseSEBATRAP connectToServerMastercard(RequestSEBATRAP req) {
		if (ipAddrSBTP == null) try { //Sets the HOST & PORT for server_mastercard
			Properties properties = new Properties();
			properties.load(new FileInputStream(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "serverpayment.properties"));
			ipAddrSBTP = properties.getProperty("HOST_MASTERCARD");
			portSBTP = Integer.parseInt(properties.getProperty("PORT_MASTERCARD"));
		} catch (IOException | NumberFormatException e) {
			System.err.println("Property file error : " + e.getMessage());
		}
		String ksFileName = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "ksSerPay";
		if (ssls == null) //Connection to the server
			ssls = SSLUtilities.getSSLSocket(ksFileName, pwd, ipAddrSBTP, portSBTP);
		try { //Send request
			if (oos == null) oos = new ObjectOutputStream(ssls.getOutputStream());
			oos.writeObject(req);
			oos.flush();
			//Read response
			ObjectInputStream ois = new ObjectInputStream(ssls.getInputStream());
			return (ResponseSEBATRAP) ois.readObject();
		} catch (IOException e) {
			System.err.println("IO Error : " + e.getMessage());
		} catch (ClassNotFoundException ex) {
			System.err.println("Receive Error - ClassNotFound : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param ownerName
	 * @param cardNumber
	 * @param price
	 *
	 * @return
	 */
	private boolean verifyCard(String ownerName, String cardNumber, float price) {
		Object cu[] = {ownerName, cardNumber, price};
		ResponseSEBATRAP res = connectToServerMastercard(new RequestSEBATRAP(RequestSEBATRAP.VERIFY_CARD, cu));
		if (res != null) {
			if (res.getCode() == ResponseSEBATRAP.VERIFY_CARD_FAIL) return false;
			else return chargeCard(res.getChargeUtile());
		}
		return false;
	}

	/**
	 *
	 * @param token
	 *
	 * @return
	 */
	private boolean chargeCard(Object token) {
		ResponseSEBATRAP res = connectToServerMastercard(new RequestSEBATRAP(RequestSEBATRAP.CHARGE_CARD, token));
		if (res != null) return res.getCode() == ResponseSEBATRAP.CHARGE_CARD_OK;
		return false;
	}
	//</editor-fold>

}

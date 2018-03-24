package crypto.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

/**
 * @author Morgan
 */
public class SSLUtilities {
	
	/**
	 * 
	 * @param ksFileName
	 * @param pwd
	 * @return 
	 */
	private static SSLContext getSSLContext(String ksFileName, char[] pwd) {
		try {
			//Keystore
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksFileName), pwd);
			//Context
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, pwd);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);
			SSLContext sslc = SSLContext.getInstance("SSLv3");
			sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			return sslc;
		} catch (IOException e) {
			System.err.println("Error IO : " + e.getMessage());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
			| UnrecoverableKeyException | KeyManagementException ex) {
			System.err.println("SSL Error : " + ex.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param ksFileName
	 * @param pwd
	 * @param port
	 * @return 
	 */
	public static SSLServerSocket getSSLServerSocket(String ksFileName,
		char[] pwd, int port) {
		try {
			SSLContext sslc = getSSLContext(ksFileName, pwd);
			SSLServerSocketFactory sslssf = sslc.getServerSocketFactory();
			return (SSLServerSocket) sslssf.createServerSocket(port);
		} catch (IOException e) {
			System.err.println("Error IO : " + e.getMessage());
		} catch (NullPointerException e) {
			System.err.println("NULL Error : " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param ksFileName
	 * @param pwd
	 * @param ipAddr
	 * @param port
	 * @return 
	 */
	public static SSLSocket getSSLSocket(String ksFileName, char[] pwd,
		String ipAddr, int port) {
		try {
			SSLContext sslc = getSSLContext(ksFileName, pwd);
			SSLSocketFactory sslsf = sslc.getSocketFactory();
			return (SSLSocket) sslsf.createSocket(ipAddr, port);
		} catch (IOException e) {
			System.err.println("Error IO : " + e.getMessage());
		} catch (NullPointerException e) {
			System.err.println("NULL Error : " + e.getMessage());
		}
		return null;
	}

}

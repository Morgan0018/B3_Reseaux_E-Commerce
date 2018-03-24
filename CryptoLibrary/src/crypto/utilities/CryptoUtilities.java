package crypto.utilities;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;

/**
 * @author Morgan
 */
public class CryptoUtilities {

	//<editor-fold defaultstate="collapsed" desc="Digest">
	/**
	 * Uses the java.security.MessageDigest to hash the message
	 *
	 * @param msg    : the message to hash
	 * @param random : an int that will be used in the hash
	 * @param time   : a long that will be used in the hash
	 *
	 * @return the hash of the message as a byte[]
	 */
	public static byte[] makeDigest(String msg, int random, long time) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			//Create the digest
			MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
			md.update(msg.getBytes()); //Add the message
			//transform int and long to bytes
			try (DataOutputStream dos = new DataOutputStream(baos)) {
				dos.writeInt(random);
				dos.writeLong(time);
			}
			md.update(baos.toByteArray()); //Add int and long
			return md.digest();
		} catch (IOException e) {
			System.err.println("Error IO : " + e.getMessage());
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			System.err.println("Digest error : " + e.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param original : the original hash to compare
	 * @param msg      : the message to verify
	 * @param random   : an int that will be used in the hash
	 * @param time     : a long that will be used in the hash
	 *
	 * @return
	 */
	public static boolean verifyDigest(byte[] original, String msg, int random, long time) {
		return MessageDigest.isEqual(original, makeDigest(msg, random, time));
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="HMAC">
	/**
	 * Hash a message with a secret (symmetric) key to provide authentication
	 *
	 * @param toHash : the Object to hash
	 * @param key    : the SecretKey to use in the hash
	 *
	 * @return the hash as a table of bytes
	 */
	public static byte[] makeHMAC(Object toHash, SecretKey key) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(toHash);

				Mac hMac = Mac.getInstance("HMAC-MD5", "BC");
				hMac.init(key);
				hMac.update(baos.toByteArray());
				return hMac.doFinal();
			} catch (NoSuchAlgorithmException | NoSuchProviderException |
				InvalidKeyException ex) {
				System.err.println("Error : " + ex.getMessage());
			}
		} catch (IOException ex) {
			System.err.println("Error IO : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param original : the original hash as a table of bytes
	 * @param toCheck  : the Object to authenticate
	 * @param key      : the SecretKey to use in the hash
	 *
	 * @return a boolean
	 */
	public static boolean verifyHMAC(byte[] original, Object toCheck, SecretKey key) {
		return MessageDigest.isEqual(original, makeHMAC(toCheck, key));
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Signature">
	/**
	 * Sign a message with a private (asymmetric) key to provide authentication
	 *
	 * @param toSign    : the Object to sign
	 * @param algorithm : the algorithms to use for the Signature
	 * @param key       : the PrivateKey to use to sign
	 *
	 * @return the signature as a table of bytes
	 */
	public static byte[] sign(Object toSign, String algorithm, PrivateKey key) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(toSign);
			}
			Signature s = Signature.getInstance(algorithm, "BC");
			s.initSign(key);
			s.update(baos.toByteArray());
			return s.sign();
		} catch (IOException ex) {
			System.err.println("Error - IOException : " + ex.getMessage());
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			InvalidKeyException | SignatureException ex) {
			System.err.println("Error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param signature : the signature as a table of bytes
	 * @param toVerify  : the Object to authenticate
	 * @param algorithm : the algorithms to use for the Signature
	 * @param key       : the PublicKey to use for the verification
	 *
	 * @return a boolean
	 */
	public static boolean verifySignature(byte[] signature, Object toVerify,
		String algorithm, PublicKey key) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(toVerify);
			}
			Signature s = Signature.getInstance(algorithm, "BC");
			s.initVerify(key);
			s.update(baos.toByteArray());
			return s.verify(signature);
		} catch (IOException ex) {
			System.err.println("Error - IOException : " + ex.getMessage());
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			InvalidKeyException | SignatureException ex) {
			System.err.println("Error : " + ex.getMessage());
		}
		return false;
	}
	//</editor-fold>

	/**
	 * Generates a javax.crypto.SecretKey for the provided algorithm
	 * (using the provider BouncyCastle)
	 *
	 * @param algorithm : the algorithm to use for the key
	 *
	 * @return a javax.crypto.SecretKey
	 */
	public static SecretKey generateSecretKey(String algorithm) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance(algorithm, "BC");
			kg.init(new SecureRandom());
			return kg.generateKey();
		} catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
			System.err.println("Error : " + ex.getMessage());
			return null;
		}
	}

	//<editor-fold defaultstate="collapsed" desc="KeyStore">
	/**
	 * Opens the provided keystore file to return the PublicKey
	 * (Keystore is JKS with SUN provider)
	 *
	 * @param keystoreFilePath : the file path to the keystore
	 * @param pwd              : the keystore password
	 * @param alias            : the entry to access in the keystore
	 *
	 * @return the PublicKey found in the TrustedCertEntry
	 */
	public static PublicKey getPublicKeyfromKeystoreCertificate(String keystoreFilePath,
		char[] pwd, String alias) {
		try {
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			ks.load(new FileInputStream(keystoreFilePath), pwd);
			X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
			return certificate.getPublicKey();
		} catch (KeyStoreException | NoSuchProviderException | IOException |
			NoSuchAlgorithmException | CertificateException ex) {
			System.err.println("Error : " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Opens the provided keystore file to return the PrivateKey
	 * (Keystore is JKS with SUN provider)
	 *
	 * @param keystoreFilePath : the file path to the keystore
	 * @param pwd              : the keystore password
	 * @param alias            : the entry to access in the keystore
	 *
	 * @return the PrivateKey found in the PrivateKeyEntry
	 */
	public static PrivateKey getPrivateKeyFromKeystore(String keystoreFilePath,
		char[] pwd, String alias) {
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(keystoreFilePath), pwd);
			return (PrivateKey) ks.getKey(alias, pwd);
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException |
			CertificateException | UnrecoverableKeyException ex) {
			System.err.println("Error : " + ex.getMessage());
			return null;
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Crypt keys">
	/**
	 * "wraps" a Key with the provided PublicKey and algorithm
	 *
	 * @param algorithm
	 * @param pk
	 * @param toCrypt
	 *
	 * @return
	 */
	public static byte[] wrapKey(String algorithm, PublicKey pk, Key toCrypt) {
		try {
			Cipher c = Cipher.getInstance(algorithm, "BC");
			c.init(Cipher.WRAP_MODE, pk);
			return c.wrap(toCrypt);
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException ex) {
			System.err.println("Error : " + ex.getMessage());
			return null;
		}
	}
	
	/**
	 *
	 * @param algorithm
	 * @param prk
	 * @param toUnwrap
	 * @param keyAlgorithm
	 * @param keyType
	 *
	 * @return
	 */
	public static Key unwrapKey(String algorithm, PrivateKey prk, byte[] toUnwrap,
		String keyAlgorithm, int keyType) {
		try {
			Cipher c = Cipher.getInstance(algorithm, "BC");
			c.init(Cipher.UNWRAP_MODE, prk);
			return c.unwrap(toUnwrap, keyAlgorithm, keyType);
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			NoSuchPaddingException | InvalidKeyException ex) {
			System.err.println("Error : " + ex.getMessage());
			return null;
		}
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Symmetric en/decryption">
	/**
	 *
	 * @param toCrypt
	 * @param algoritm
	 * @param key
	 *
	 * @return
	 */
	public static byte[] symmetricEncryption(Object toCrypt, String algoritm, SecretKey key) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(toCrypt);
			}
			Cipher c = Cipher.getInstance(algoritm, "BC");
			c.init(Cipher.ENCRYPT_MODE, key);
			return c.doFinal(baos.toByteArray());
		} catch (IOException e) {
			System.err.println("Error - IOException : " + e.getMessage());
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			NoSuchPaddingException | InvalidKeyException |
			IllegalBlockSizeException | BadPaddingException ex) {
			System.err.println("Crypt Error : " + ex.getMessage());
		}
		return null;
	}
	
	/**
	 *
	 * @param toDecrypt
	 * @param algorithm
	 * @param key
	 *
	 * @return
	 */
	public static Object symmetricDecryption(byte[] toDecrypt, String algorithm, SecretKey key) {
		try {
			Cipher c = Cipher.getInstance(algorithm, "BC");
			c.init(Cipher.DECRYPT_MODE, key);
			return (new ObjectInputStream(new ByteArrayInputStream(
				c.doFinal(toDecrypt)))).readObject();
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			NoSuchPaddingException | InvalidKeyException |
			IllegalBlockSizeException | BadPaddingException ex) {
			System.err.println("Crypt Error : " + ex.getMessage());
		} catch (IOException | ClassNotFoundException ex) {
			System.err.println("Error : " + ex.getMessage());
		}
		return null;
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Asymmetric en/decryption">
	/**
	 *
	 * @param toCrypt
	 * @param algorithm
	 * @param key
	 *
	 * @return
	 */
	public static byte[] asymmetricEncryption(Object toCrypt, String algorithm, PublicKey key) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(toCrypt);
			}
			Cipher c = Cipher.getInstance(algorithm, "BC");
			c.init(Cipher.ENCRYPT_MODE, key);
			return c.doFinal(baos.toByteArray());
		} catch (IOException ex) {
			System.err.println("Error - IOException : " + ex.getMessage());
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			NoSuchPaddingException | InvalidKeyException |
			IllegalBlockSizeException | BadPaddingException ex) {
			System.err.println("Error : " + ex.getMessage());
		}
		return null;
	}
	
	/**
	 *
	 * @param toDecrypt
	 * @param algorithm
	 * @param key
	 *
	 * @return
	 */
	public static Object asymmetricDecryption(byte[] toDecrypt, String algorithm, PrivateKey key) {
		try {
			Cipher c = Cipher.getInstance(algorithm, "BC");
			c.init(Cipher.DECRYPT_MODE, key);
			return (new ObjectInputStream(new ByteArrayInputStream(
				c.doFinal(toDecrypt)))).readObject();
		} catch (NoSuchAlgorithmException | NoSuchProviderException |
			NoSuchPaddingException | InvalidKeyException |
			IllegalBlockSizeException | BadPaddingException ex) {
			System.err.println("Crypt Error : " + ex.getMessage());
		} catch (IOException | ClassNotFoundException ex) {
			System.err.println("Error : " + ex.getMessage());
		}
		return null;
	}
	//</editor-fold>
	
}

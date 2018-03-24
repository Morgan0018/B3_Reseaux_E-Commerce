package web.application.tickets;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Morgan
 */
public class SendMail {

	private final Session sess;
	
	/**
	 * 
	 */
	public SendMail() {
		Properties prop = System.getProperties();
		prop.put("file.encoding", "iso-8859-1");
		//prop.put("mail.smtp.host", "InPrES-Airport.be");
		sess = Session.getDefaultInstance(prop, null);
	}
	
	/**
	 * 
	 * @param confirm
	 * @param address
	 * @return 
	 */
	boolean sendMail(boolean confirm, String address) {
		String subject = confirm ? "Paiement effectué" : "Paiement échoué";
		String content = confirm
			? "Votre paiement a été effectué. Vos billets sont enregistrés."
			: "Le paiement a échoué. Les billets ne sont pas enregistré.";
		try {
			MimeMessage mm = new MimeMessage(sess);
			mm.setFrom(new InternetAddress("morgandreze@gmail.com")); //FIXME ????
			mm.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
			mm.setSubject(subject);
			mm.setText(content);
			Transport.send(mm);
			return true;
		} catch (AddressException ex) {
			System.err.println("Address Error : " + ex.getMessage());
		} catch (MessagingException ex) {
			System.err.println("Messaging Error : " + ex.getMessage());
		}
		return false;
	}
	
}

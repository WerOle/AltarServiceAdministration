package asa.address.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

/**
 * Handling sending an e-mail
 * 
 * @author Ole Werger
 */
public class Mailer {

	public Mailer() {

	}

	static String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void postMail(String recipient, String subject, String message, String from, String anh1, String anh2,
			String anh3) throws MessagingException {

		if (anh1.equals("") && anh2.equals("") && anh3.equals("")) {

			@SuppressWarnings("unused")
			String encoding = "8bit";
			@SuppressWarnings("unused")
			String charset = "UTF-8";
			@SuppressWarnings("unused")
			String contentType = "text/plain";
			@SuppressWarnings("unused")
			String subjectEncoding = "Q";

			Properties props = new Properties();
			props.put("mail.debug", "true");
			props.put("mail.smtp.host", "mail.gmx.net");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.trust", "mail.gmx.net");
			props.put("mail.smtp.port", "587");
			props.put("mail.user", from);

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					if (password.equals(null)) {
						JOptionPane.showMessageDialog(null, "no password entered!");
					}
					return new PasswordAuthentication(from, password);
				}
			});

			Message msg = new MimeMessage(session);
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);
			InternetAddress addressTo = new InternetAddress(recipient);
			msg.setRecipient(Message.RecipientType.TO, addressTo);

			try {
				msg.setSubject(MimeUtility.encodeText(subject, "iso-8859-15", null));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			msg.setContent(message, "text/plain; charset=\"iso-8859-15\"");

			Transport.send(msg);
		} else {
			@SuppressWarnings("unused")
			String encoding = "8bit";
			@SuppressWarnings("unused")
			String charset = "UTF-8";
			@SuppressWarnings("unused")
			String contentType = "text/plain";
			@SuppressWarnings("unused")
			String subjectEncoding = "Q";

			Properties props = new Properties();
			props.put("mail.debug", "true");

			props.put("mail.smtp.host", "mail.gmx.net");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.trust", "mail.gmx.net");
			props.put("mail.smtp.port", "587");
			props.put("mail.user", from);

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					if (password.equals(null)) {
						JOptionPane.showMessageDialog(null, "no password entered!");
					}
					return new PasswordAuthentication(from, password);
				}
			});

			MimeMultipart content = new MimeMultipart("mixed");

			MimeBodyPart text = new MimeBodyPart();
			text.setText(message);
			content.addBodyPart(text);

			if (!anh1.equals("")) {
				MimeBodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setDataHandler(new DataHandler(new FileDataSource(anh1)));
				messageBodyPart1.setFileName(new File(anh1).getName());
				content.addBodyPart(messageBodyPart1);
			}

			if (!anh2.equals("")) {
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				messageBodyPart2.setDataHandler(new DataHandler(new FileDataSource(anh2)));
				messageBodyPart2.setFileName(new File(anh2).getName());
				content.addBodyPart(messageBodyPart2);
			}

			if (!anh3.equals("")) {
				MimeBodyPart messageBodyPart3 = new MimeBodyPart();
				messageBodyPart3.setDataHandler(new DataHandler(new FileDataSource(anh3)));
				messageBodyPart3.setFileName(new File(anh3).getName());
				content.addBodyPart(messageBodyPart3);
			}

			Message msg = new MimeMessage(session);

			InternetAddress addressTo = new InternetAddress(recipient);
			msg.setRecipient(Message.RecipientType.TO, addressTo);

			msg.setSubject(subject);
			msg.setContent(content);

			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);

			try {
				msg.setSubject(MimeUtility.encodeText(subject, "iso-8859-15", null));
			} catch (UnsupportedEncodingException e) {
				asa.address.logger.Logger.logAdd(e.getMessage());
				e.printStackTrace();
			}

			Transport.send(msg);
		}

	}
}
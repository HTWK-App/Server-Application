package com.htwk.app.repository.helper.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.htwk.app.model.mail.Mail;
import com.htwk.app.model.mail.MailAttachment;
import com.htwk.app.model.mail.MailCredentials;
import com.htwk.app.utils.Utils;

public class EmailReceiver {

	private static final Logger logger = LoggerFactory.getLogger(EmailReceiver.class);

	/**
	 * Returns a Properties object which is configured for a POP3/IMAP server
	 * 
	 * @param protocol
	 *            either "imap" or "pop3"
	 * @param host
	 * @param port
	 * @return a Properties object
	 */
	private Properties getServerProperties(String protocol, String host, String port) {
		Properties properties = new Properties();
		// server setting
		properties.put(String.format("mail.%s.host", protocol), host);
		properties.put(String.format("mail.%s.port", protocol), port);

		// SSL setting
		properties
				.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
		properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "true");
		properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

		return properties;
	}

	/**
	 * Downloads new messages and fetches details for each message.
	 * 
	 * @param protocol
	 * @param host
	 * @param port
	 * @param offset
	 * @param userName
	 * @param password
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public List<Mail> getEmails(MailCredentials credentials, int offset) throws MessagingException, IOException {
		Properties properties = getServerProperties(credentials.getProtocol(), credentials.getHost(),
				"" + credentials.getPort());
		Session session = Session.getDefaultInstance(properties);

		// connects to the message store
		Store store = session.getStore(credentials.getProtocol());
		store.connect(credentials.getUsername(), credentials.getPassword());

		// opens the inbox folder
		Folder folderInbox = store.getFolder("INBOX");
		folderInbox.open(Folder.READ_ONLY);

		logger.debug("unread:" + folderInbox.getUnreadMessageCount() + ", new:" + folderInbox.getNewMessageCount());

		// fetches new messages from server
		int mailCount = (folderInbox.getMessageCount() <= offset) ? 1 : folderInbox.getMessageCount() - offset;
		Message[] messages = folderInbox.getMessages((mailCount), folderInbox.getMessageCount());

		List<Mail> mails = getMails(messages);

		// disconnect
		folderInbox.close(false);
		store.close();
		return Lists.reverse(mails);
	}

	public List<Mail> getNewEmails(MailCredentials credentials) throws MessagingException, IOException {
		Properties properties = getServerProperties(credentials.getProtocol(), credentials.getHost(),
				"" + credentials.getPort());
		Session session = Session.getDefaultInstance(properties);

		// connects to the message store
		Store store = session.getStore(credentials.getProtocol());
		store.connect(credentials.getUsername(), credentials.getPassword());

		logger.debug("url:" + store.getURLName());
		// opens the inbox folder
		Folder folderInbox = store.getFolder("INBOX");
		folderInbox.open(Folder.READ_ONLY);

		logger.debug("unread:" + folderInbox.getUnreadMessageCount() + ", new:" + folderInbox.getNewMessageCount());

		// fetches new messages from server
		FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		Message messages[] = folderInbox.search(ft);

		List<Mail> mails = getMails(messages);

		// disconnect
		folderInbox.close(false);
		store.close();
		return Lists.reverse(mails);
	}

	public Mail getEmail(int mailId, MailCredentials credentials, String salt) throws MessagingException, IOException {
		Properties properties = getServerProperties(credentials.getProtocol(), credentials.getHost(),
				"" + credentials.getPort());
		Session session = Session.getDefaultInstance(properties);

		// connects to the message store
		Store store = session.getStore(credentials.getProtocol());
		store.connect(credentials.getUsername(), credentials.getPassword());

		// opens the inbox folder
		Folder folderInbox = store.getFolder("INBOX");
		folderInbox.open(Folder.READ_ONLY);

		// fetches new messages from server
		int msgnum[] = new int[] { mailId };
		Message[] messages = folderInbox.getMessages(msgnum);

		Mail mail = getMails(messages).get(0);

		// disconnect
		folderInbox.close(false);
		store.close();
		return mail;
	}

	public void sendMail(Mail mail, MailCredentials credentials) throws MessagingException {
		Properties properties = getServerProperties(credentials.getProtocol(), credentials.getHost(),
				"" + credentials.getPort());
		properties.put(String.format("mail.%s.auth", credentials.getProtocol()), "true");
		properties.put(String.format("mail.%s.starttls.enable", credentials.getProtocol()), "true");
		// Properties properties = new Properties();
		// Authentifizierung aktivieren
		// properties.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(properties);

		Transport transport = session.getTransport(credentials.getProtocol());
		transport.connect(credentials.getHost(), credentials.getPort(), credentials.getUsername(),
				credentials.getPassword());

		Address[] addresses = InternetAddress.parse(Joiner.on(",").join(mail.getToList()));
		// [a-zA-Z0-9\\._\\-]{3,}\\@[a-zA-Z0-9\\._\\-]{3,}\\.[a-zA-Z]{2,6}

		Message message = new MimeMessage(session);
		String from = mail.getFrom().replace(" ", ".").toLowerCase();
		message.setFrom(new InternetAddress(mail.getFrom() + " <" + from + "@stud.htwk-leipzig.de>"));
		message.setRecipients(Message.RecipientType.TO, addresses);

		if (!mail.getCcList().isEmpty()) {
			addresses = InternetAddress.parse(Joiner.on(",").join(mail.getCcList()));
			message.setRecipients(Message.RecipientType.CC, addresses);
		}

		message.setSubject(mail.getSubject());
		message.setText(mail.getMessage());

		transport.addTransportListener(new TransportListener() {

			public void messageDelivered(TransportEvent e) {
				logger.debug("Message delivered for:");
				if (e != null) {
					Address[] a = e.getValidSentAddresses();
					if (a != null && a.length > 0) {
						for (int i = 0; i < a.length; i++) {
							logger.debug(((InternetAddress) a[i]).getAddress());
						}
					}
				}
			}

			public void messageNotDelivered(TransportEvent e) {
				logger.debug("Message not delivered for:");
				if (e != null) {
					Address[] a = e.getValidUnsentAddresses();
					if (a != null && a.length > 0) {
						for (int i = 0; i < a.length; i++) {
							logger.debug(((InternetAddress) a[i]).getAddress());
						}
					}
				}
			}

			public void messagePartiallyDelivered(TransportEvent e) {
				logger.debug("These addresses are invalid:");
				if (e != null) {
					Address[] a = e.getInvalidAddresses();
					if (a != null && a.length > 0) {
						for (int i = 0; i < a.length; i++) {
							logger.debug(((InternetAddress) a[i]).getAddress());
						}
					}
				}
			}
		});
		transport.sendMessage(message, addresses);
		logger.debug("E-Mail gesendet");
		transport.close();
	}

	private MailAttachment getAttachmentDescription(BodyPart bodyPart) throws MessagingException {
		MailAttachment attachment = new MailAttachment();
		attachment.setName(bodyPart.getFileName());
		attachment.setType(bodyPart.getContentType());
		attachment.setLength(bodyPart.getSize());
		attachment.setLengthFormated(Utils.toReadable(bodyPart.getSize(), false));
		return attachment;
	}

	private List<Mail> getMails(Message messages[]) throws MessagingException, IOException {
		List<Mail> mails = new ArrayList<Mail>();

		for (int i = 0; i < messages.length; i++) {
			Message msg = messages[i];
			Address[] fromAddress = msg.getFrom();
			Mail mail = new Mail();
			mail.setId(msg.getMessageNumber());
			mail.setFrom(fromAddress[0].toString());
			mail.setSubject(msg.getSubject());
			mail.getToList().addAll(Arrays.asList(parseAddresses(msg.getRecipients(RecipientType.TO))));
			mail.getCcList().addAll(Arrays.asList(parseAddresses(msg.getRecipients(RecipientType.CC))));
			mail.setSendDate(msg.getSentDate().toString());
			mail.setFlags(getFlags(msg));

			String messageContent = "";

			Object msgContent = msg.getContent();
			/* Check if content is pure text/html or in parts */
			if (msgContent instanceof Multipart) {

				Multipart multipart = (Multipart) msgContent;
				for (int j = 0; j < multipart.getCount(); j++) {
					BodyPart bodyPart = multipart.getBodyPart(j);
					String disposition = bodyPart.getDisposition();
					if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
						mail.getAttachments().add(getAttachmentDescription(bodyPart));
					} else if (disposition != null && (disposition.equals(BodyPart.INLINE))) {
						if (bodyPart.isMimeType("text/*")) {
							messageContent += (String) bodyPart.getContent();
						}
					} else {
						messageContent += bodyPart.getContent().toString();
					}
				}
			} else {
				messageContent = messages[i].getContent().toString();
			}

			// escape javascript
			Document doc = Jsoup.parse(messageContent);
			doc.removeAttr("script");
			mail.setMessage(doc.html());
			mails.add(mail);
		}
		return mails;
	}

	private List<String> getFlags(Message msg) throws MessagingException {
		List<String> returnedFlags = new ArrayList<String>();
		Flags flags = msg.getFlags();
		if (flags.contains(Flag.ANSWERED)) {
			returnedFlags.add("answered");
		} else if (flags.contains(Flag.DELETED)) {
			returnedFlags.add("deleted");
		} else if (flags.contains(Flag.DRAFT)) {
			returnedFlags.add("draft");
		} else if (flags.contains(Flag.RECENT)) {
			returnedFlags.add("new");
		} else if (flags.contains(Flag.SEEN)) {
			returnedFlags.add("seen");
		}
		return returnedFlags;

	}

	/**
	 * Returns a list of addresses in String format separated by comma
	 * 
	 * @param address
	 *            an array of Address objects
	 * @return a string represents a list of addresses
	 */
	private String parseAddresses(Address[] address) {
		String listAddress = "";

		if (address != null) {
			for (int i = 0; i < address.length; i++) {
				listAddress += address[i].toString() + ", ";
			}
		}
		if (listAddress.length() > 1) {
			listAddress = listAddress.substring(0, listAddress.length() - 2);
		}

		return listAddress;
	}

	public ResponseEntity<byte[]> downloadAttachment(int mail, String attachmentName, MailCredentials credentials)
			throws MessagingException, IOException {
		Properties properties = getServerProperties(credentials.getProtocol(), credentials.getHost(),
				"" + credentials.getPort());
		Session session = Session.getDefaultInstance(properties);

		// connects to the message store
		Store store = session.getStore(credentials.getProtocol());
		store.connect(credentials.getUsername(), credentials.getPassword());

		// opens the inbox folder
		Folder folderInbox = store.getFolder("INBOX");
		folderInbox.open(Folder.READ_ONLY);

		logger.debug("unread:" + folderInbox.getUnreadMessageCount() + ", new:" + folderInbox.getNewMessageCount());

		// fetches new messages from server
		int msgnum[] = new int[] { mail };
		Message[] messages = folderInbox.getMessages(msgnum);
		ResponseEntity<byte[]> response;
		try {
			response = getAttachment(messages[0], attachmentName);
		} finally {
			folderInbox.close(false);
			store.close();
		}
		return response;
	}

	private ResponseEntity<byte[]> getAttachment(Message message, String attachmentName) throws MessagingException,
			IOException {
		Multipart multipart = (Multipart) message.getContent();

		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart bodyPart = multipart.getBodyPart(i);
			if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
					&& StringUtils.isEmpty(bodyPart.getFileName())) {
				continue; // dealing with attachments only
			}
			// bodyPart.getContentType()
			String fileName = bodyPart.getFileName();
			if (fileName.equals(attachmentName)) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(bodyPart.getContentType()));
				headers.set("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
				// headers.set("Content-Disposition", "" +
				// bodyPart.getContentType());
				return new ResponseEntity<byte[]>(IOUtils.toByteArray(bodyPart.getInputStream()), headers,
						HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<byte[]>(null, null, HttpStatus.NO_CONTENT);
	}

	public void changeMailStatus(int mailId, Flag flag, MailCredentials credentials) throws MessagingException {
		Properties properties = getServerProperties(credentials.getProtocol(), credentials.getHost(),
				"" + credentials.getPort());
		Session session = Session.getDefaultInstance(properties);

		// connects to the message store
		Store store = session.getStore(credentials.getProtocol());
		store.connect(credentials.getUsername(), credentials.getPassword());

		// opens the inbox folder
		Folder folderInbox = store.getFolder("INBOX");
		folderInbox.open(Folder.READ_WRITE);

		logger.debug("unread:" + folderInbox.getUnreadMessageCount() + ", new:" + folderInbox.getNewMessageCount());

		// fetches new messages from server
		int msgnum[] = new int[] { mailId };
		Message[] messages = folderInbox.getMessages(msgnum);

		if (messages.length == 1 && messages[0] != null) {
			messages[0].setFlag(flag, true);
		}

		folderInbox.close(false);
		store.close();
	}
}
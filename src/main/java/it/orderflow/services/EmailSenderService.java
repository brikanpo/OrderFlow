package it.orderflow.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import it.orderflow.ConfigManager;
import it.orderflow.exceptions.EmailNotSentException;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class EmailSenderService {

    private static final String APPLICATION_NAME = "OrderFlow";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private final boolean active;

    public EmailSenderService() {
        ConfigManager configManager = ConfigManager.getInstance();

        active = Boolean.parseBoolean(configManager.getProperty("emailService.active"));
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport)
            throws IOException {
        InputStream in = EmailSenderService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private MimeMessage createEmail(String toEmailAddress,
                                    String subject,
                                    String bodyText)
            throws MessagingException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress("me"));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    private MimeMessage createEmailWithAttachment(String toEmailAddress,
                                                  String subject,
                                                  String bodyText,
                                                  File file)
            throws MessagingException {

        MimeMessage email = createEmail(toEmailAddress, subject, bodyText);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());
        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        return email;
    }

    private Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private void sendEmail(String toEmailAddress,
                           String subject,
                           String bodyText)
            throws GeneralSecurityException, IOException, MessagingException {
        this.sendEmail(toEmailAddress, subject, bodyText, null);
    }

    private void sendEmail(String toEmailAddress,
                           String subject,
                           String bodyText,
                           File file)
            throws GeneralSecurityException, IOException, MessagingException {

        if (!this.active) {
            return;
        }

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();

        MimeMessage emailContent;

        if (file == null) {
            emailContent = this.createEmail(toEmailAddress, subject, bodyText);
        } else {
            emailContent = this.createEmailWithAttachment(toEmailAddress, subject, bodyText, file);
        }

        Message message = this.createMessageWithEmail(emailContent);

        try {
            service.users().messages().send("me", message).execute();
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            }
        }
    }

    public void sendEmailNotification(EmailType emailType, String toEmailAddress, String moreInfo)
            throws EmailNotSentException {
        try {
            switch (emailType) {
                case CHANGE_ROLE -> this.sendEmail(toEmailAddress,
                        "Change role",
                        "Your role was changed to " + moreInfo + ".");

                case NEW_EMPLOYEE -> this.sendEmail(toEmailAddress,
                        "OrderFlow Registration",
                        "You have been registered in OrderFlow as a " + moreInfo + ".\n" +
                                "Your password for your first login is your email address.");

                case NEW_CLIENT_ORDER -> this.sendEmail(toEmailAddress,
                        "New client order",
                        "There is a new client order (Registration Date - Client Name : " + moreInfo + ") to prepare.");

                case NEW_READY_ORDER -> this.sendEmail(toEmailAddress,
                        "New ready order",
                        "A client order (Registration Date - Client Name : " + moreInfo + ") is ready to be delivered.");

                default -> throw new UnsupportedOperationException();
            }
        } catch (Exception e) {
            throw new EmailNotSentException(toEmailAddress, e);
        }
    }

    public void sendEmailWithAttachment(EmailType emailType, String toEmailAddress, File file)
            throws EmailNotSentException {
        try {
            if (emailType == EmailType.CLOSED_CLIENT_ORDER) {
                this.sendEmail(toEmailAddress,
                        "Invoice order",
                        "Dear client,\n" +
                                "Here is the invoice of your order.",
                        file);
            } else if (emailType == EmailType.NEW_SUPPLIER_ORDER) {
                this.sendEmail(toEmailAddress,
                        "New order",
                        "Dear supplier,\n" +
                                "Here is my new order.",
                        file);
            }
        } catch (Exception e) {
            throw new EmailNotSentException(toEmailAddress, e);
        }
    }

    public enum EmailType {
        CHANGE_ROLE,
        CLOSED_CLIENT_ORDER,
        CLOSED_SUPPLIER_ORDER,
        NEW_EMPLOYEE,
        NEW_CLIENT_ORDER,
        NEW_READY_ORDER,
        NEW_SUPPLIER_ORDER,
    }
}

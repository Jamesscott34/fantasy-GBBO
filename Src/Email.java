import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;



public class Email {
    public static void sendEmail(String from, String password, String to, String subject, String messageBody) {
        // Email configuration (change according to your email provider)
        String host = "smtp.gmail.com"; // For example, smtp.gmail.com for Gmail
        int port = 587; // Adjust this according to your email provider's settings

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            ExceptionHandler.handleMessagingException(e);
        }
    }
}

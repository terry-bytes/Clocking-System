package ateam.ServiceImpl;

import ateam.Models.Email;
import ateam.Service.EmailService;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


public class EmailServiceImpl implements EmailService {

    private Email email;

    public EmailServiceImpl(Email email) {
        this.email = email;
    }

    public EmailServiceImpl() {
        this.email = new Email("vmcplanettv40@gmail.com", "bdhj gmli rvfh nbwp");
    }

    @Override
    public void sendMail(Email email) {
        final String USERNAME = email.getSender();
        final String PASSWORD = email.getPassword();
        String receiver = email.getReceiver().trim();
        String subject = email.getSubject();
        String content = email.getMessage();

        if (!isValidEmail(receiver)) {
            System.err.println("Invalid email address: " + receiver);
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("Email sent successfully to " + receiver);

        } catch (AddressException e) {
            System.err.println("Invalid email address: " + e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPasswordResetMail(String email, String otp) {
        final String from = "vmcplanettv40@gmail.com";
        final String password = "bdhj gmli rvfh nbwp";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);

            Transport.send(message);
            System.out.println("OTP sent successfully to " + email);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}

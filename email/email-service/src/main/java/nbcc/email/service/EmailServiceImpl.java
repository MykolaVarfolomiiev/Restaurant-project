package nbcc.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nbcc.email.domain.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean sendEmail(EmailRequest emailRequest) {
        try {
            logger.debug("Attempting to send email to recipients: {}", Arrays.toString(emailRequest.getRecipients()));

            if (emailRequest.getFrom() == null || emailRequest.getFrom().isBlank()) {
                emailRequest.setFrom(fromEmail);
            }

            if (emailRequest.isHtml()) {
                sendHtmlEmail(emailRequest);
            } else {
                sendSimpleEmail(emailRequest);
            }

            logger.debug("Email sent successfully to recipients: {}", Arrays.toString(emailRequest.getRecipients()));
            return true;
        } catch (Exception e) {
            logger.error("Failed to send email to recipients: {}", Arrays.toString(emailRequest.getRecipients()), e);
            return false;
        }
    }

    private void sendSimpleEmail(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);

        if (emailRequest.getTo() != null) {
            message.setTo(emailRequest.getTo());
        }
        if (emailRequest.getCc() != null) {
            message.setCc(emailRequest.getCc());
        }
        if (emailRequest.getBcc() != null) {
            message.setBcc(emailRequest.getBcc());
        }

        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());

        if (emailRequest.getFrom() != null && !emailRequest.getFrom().isBlank()) {
            message.setReplyTo(emailRequest.getFrom());
        } else {
            message.setReplyTo(fromEmail);
        }

        mailSender.send(message);
    }

    private void sendHtmlEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);

        if (emailRequest.getTo() != null) {
            helper.setTo(emailRequest.getTo());
        }
        if (emailRequest.getCc() != null) {
            helper.setCc(emailRequest.getCc());
        }
        if (emailRequest.getBcc() != null) {
            helper.setBcc(emailRequest.getBcc());
        }

        helper.setSubject(emailRequest.getSubject());
        helper.setText(emailRequest.getBody(), true);

        if (emailRequest.getFrom() != null && !emailRequest.getFrom().isBlank()) {
            helper.setReplyTo(emailRequest.getFrom());
        } else {
            helper.setReplyTo(fromEmail);
        }

        mailSender.send(message);
    }
}

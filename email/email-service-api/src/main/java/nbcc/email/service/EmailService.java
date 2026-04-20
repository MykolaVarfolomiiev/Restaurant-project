package nbcc.email.service;

import nbcc.email.domain.EmailRequest;

public interface EmailService {

    boolean sendEmail(EmailRequest emailRequest);
}

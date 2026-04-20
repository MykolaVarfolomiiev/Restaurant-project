package nbcc.email.domain;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class EmailRequest {

    private String from;
    private String[] to;
    private String[] bcc;
    private String[] cc;
    private String subject;
    private String body;
    private boolean html;

    public EmailRequest() {
        subject = "";
    }

    public String[] getTo() {
        return to;
    }

    public EmailRequest setTo(String... to) {
        this.to = to;
        return this;
    }

    public String[] getBcc() {
        return bcc;
    }

    public EmailRequest setBcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }

    public String[] getCc() {
        return cc;
    }

    public EmailRequest setCc(String... cc) {
        this.cc = cc;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EmailRequest setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public EmailRequest setBody(String body) {
        this.body = body;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public EmailRequest setFrom(String from) {
        this.from = from;
        return this;
    }

    public boolean isHtml() {
        return html;
    }

    public EmailRequest setHtml(boolean html) {
        this.html = html;
        return this;
    }

    public String[] getRecipients() {
        return Stream.of(getTo(), getBcc(), getCc())
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
    }
}

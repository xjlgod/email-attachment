package org.xjl.email.sender;

import lombok.Data;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MyMail {
    private Session session;
    private String user;
    private MimeMessage msg;
    private String text;
    private List<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();

    public MyMail(Session session, String user) {
        this.session = session;
        this.user = user;
        this.msg = new MimeMessage(session);
    }

    public void setFrom(String nickname, String user) throws UnsupportedEncodingException, MessagingException {
        String encodeNickName = MimeUtility.encodeText(nickname);
        msg.setFrom(new InternetAddress(encodeNickName + " <" + user + ">"));
    }

    public void setSubject(String subject) throws MessagingException {
        this.msg.setSubject(subject);
    }

    public void addAttachment(File file, String fileName) throws MessagingException, UnsupportedEncodingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource fds  = new FileDataSource(file);
        attachmentPart.setDataHandler(new DataHandler(fds));
        attachmentPart.setFileName(null == fileName ? MimeUtility.encodeText(fds.getName()) : MimeUtility.encodeText(fileName));
        attachments.add(attachmentPart);
    }

    public void setToRecipient(String recipient) throws MessagingException {
        this.msg.setRecipients(Message.RecipientType.TO, recipient);
    }

    public void setText(String text)  {
        this.text = text;
    }

    public void send() throws Exception {
        MimeMultipart content = new MimeMultipart("mixed");

        try {
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(text);
            content.addBodyPart(textPart);
            for (MimeBodyPart attachment : attachments) {
                content.addBodyPart(attachment);
            }
            this.msg.setContent(content);
            this.msg.setSentDate(new Date());
            Transport.send(this.msg);
        } catch (Exception e) {
            throw e;
        }
    }
}

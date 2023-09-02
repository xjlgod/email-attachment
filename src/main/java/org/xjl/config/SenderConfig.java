package org.xjl.config;

import lombok.Data;
import lombok.Getter;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class SenderConfig {
    private Session session;
    private String filesDir;
    private String recipientAccount;
    private String senderAccount;
    private String password;
    private Properties properties;

    private static Properties initProperties(Boolean debug) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.debug", null != debug ? debug.toString() : "false");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "465");
        return properties;
    }

    public SenderConfig() throws IOException {
        Properties props = initProperties(false);
        MailType.SMTP_QQ(props);
        this.properties = props;
    }

    public void configByProperties(Properties properties) {
        recipientAccount = properties.getProperty("recipient.account");
        senderAccount = properties.getProperty("sender.account");
        password = properties.getProperty("sender.password");
        filesDir = properties.getProperty("filesDir");
        session = Session.getInstance(this.properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderAccount, password);
            }
        });
    }
}

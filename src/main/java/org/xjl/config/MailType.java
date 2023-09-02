package org.xjl.config;

import java.util.Properties;

public class MailType {
    public static Properties SMTP_QQ(Properties properties) {
        properties.put("mail.smtp.host", "smtp.qq.com");
        return properties;
    }
}

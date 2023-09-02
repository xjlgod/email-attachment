package org.xjl;

import org.xjl.config.SenderConfig;
import org.xjl.email.sender.Sender;

import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        SenderConfig senderConfig = new SenderConfig();
        Properties props = new Properties();
        // 收件人邮箱
        props.put("recipient.account", "xxxx@xxx.com");
        // 发件人邮箱
        props.put("sender.account", "xxxxx@qq.com");
        // 发件人邮箱密码，注意这里不是qq密码，而是开启SMTP服务后的密码
        props.put("sender.password", "xxxxx");
        // 存放文件的文件夹目录，批量发送此文件夹下所有文件
        props.put("filesDir", "files");
        senderConfig.configByProperties(props);

        Sender sender = new Sender();
        sender.setConfig(senderConfig);
        // 参数：邮件名，每次邮件发送间隔毫秒，每次批量发送最大附件
        sender.sendAttachments("test", 2000, 2);
    }
}

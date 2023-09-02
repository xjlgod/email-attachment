package org.xjl.email.sender;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.xjl.config.SenderConfig;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Data
@Slf4j
public class Sender {
    private SenderConfig config;

    /**
     * 根据配置批量发送带有附件的邮件
     * @param mailTitle 邮件主题
     * @param millis 每次发送的间隔时间
     * @param perCount 每次发送的附件数量
     */
    public void sendAttachments(String mailTitle, int millis, int perCount) {
        log.info("开始进行发送, 配置为");
        log.info(config.toString());
        File sourceFolder = new File(config.getFilesDir());
        if(!sourceFolder.isDirectory()) {
            return;
        }
        log.info(String.format("正在读取目录：%s", sourceFolder.getPath()));

        try {
            int times = 1;
            File[] files = sourceFolder.listFiles();
            MyMail myMail = new MyMail(config.getSession(), config.getSenderAccount());

            for (int count = 1; count <= files.length; count++) {
                File file = files[count - 1];
                myMail.addAttachment(file, file.getName());

                if (count % perCount == 0) {
                    myMail.setFrom("email-attachment", config.getSenderAccount());
                    myMail.setToRecipient(config.getRecipientAccount());
                    String title = String.format("%s-%d", mailTitle, times);
                    myMail.setSubject(title);
                    myMail.setText("this is a message sent by email-attachment");
                    log.info(String.format("正在发送邮件：%s", title));
                    myMail.send();

                    Thread.sleep(millis);
                    times++;
                    myMail = new MyMail(config.getSession(), config.getSenderAccount());
                }
            }

            if (files.length % perCount != 0) {
                myMail.setFrom("email-attachment", config.getSenderAccount());
                myMail.setToRecipient(config.getRecipientAccount());
                String title = String.format("%s-%d", mailTitle, times);
                myMail.setSubject(title);
                myMail.setText("this is a message sent by email-attachment");
                log.info(String.format("正在发送邮件：%s", title));
                myMail.send();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.clouyun.charge.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailUtils {
    private static Logger logger = LoggerFactory.getLogger(MailUtils.class);
    /**
     * 发送邮件
     *
     * @param smtp     SMTP服务器
     * @param user     用户名
     * @param password 密码
     * @param subject  标题
     * @param content  邮件内容
     * @param from     发件人邮箱
     * @param to       收件人邮箱
     */
    public static void send(String smtp, final String user,
                            final String password, String subject, String content, String from,
                            String to) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtp);
            props.put("mail.smtp.auth", "true");
            Session ssn = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });
            MimeMessage message = new MimeMessage(ssn);
            // 由邮件会话新建一个消息对象
            InternetAddress fromAddress = new InternetAddress(from);
            // 发件人的邮件地址
            message.setFrom(fromAddress);
            // 设置发件人
            InternetAddress toAddress = new InternetAddress(to);
            // 收件人的邮件地址
            message.addRecipient(Message.RecipientType.TO, toAddress);
            // 设置收件人
            message.setSubject(subject);
            // 设置标题
//			message.setText(content);
            message.setContent(content, "text/html;charset = gbk");
            // 设置内容
            message.setSentDate(new Date());
            // 设置发信时间
            Transport transport = ssn.getTransport("smtp");
            transport.connect(smtp, user, password);
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
             //transport.send(message);
            transport.close();
            logger.info("邮件发送成功，发送至："+ to);
        } catch (Exception e) {
            logger.error("邮件发送失败，失败信息：[smtp={},user={},password={},subject={},content={},form={},to={}]", smtp, user, password, subject, content, from, to);
        }
    }

    public static void main(String args[]) {

        String content = "测试消息.";

        String wangyi = "13172486525@163.com";
        String qq = "2439453924@qq.com";
        String kelu = "liqiu@szclou.com";
        send("smtp.163.com", "13277033835@163.com", "421182", "hello,world", content, "13277033835@163.com", qq);
    }
}
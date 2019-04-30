package com.xh.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @Name MailService
 * @Description
 * @Author wen
 * @Date 2019-04-30
 */
@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender sender;

    /**
    * @Name sendSimpleMail
    * @Description 发送简单的邮件
    * @Author wen
    * @Date 2019/4/30
    * @param to
    * @param title
    * @param content
    * @return void
    */
    public void sendSimpleMail(String to, String title, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        message.setFrom(from);
        sender.send(message);
    }

    /**
    * @Name sendHtmlMail
    * @Description 发送富文本内容邮件
    * @Author wen
    * @Date 2019/4/30
    * @param to
    * @param title
    * @param content
    * @return void
    */
    public void sendHtmlMail(String to, String title, String content) throws MessagingException {
        sender.send(this.getMimeMessage(to, title, content).getMessage());
    }

    /**
    * @Name sendAttachmentMail
    * @Description 发送多附件邮件
    * @Author wen
    * @Date 2019/4/30
    * @param to
    * @param title
    * @param content
    * @param filePaths
    * @return void
    */
    public void sendAttachmentMail(String to, String title, String content, String[] filePaths) throws MessagingException {
        HelperBean helperBean = this.getMimeMessage(to, title, content);
        for (String filePath : filePaths) {
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helperBean.getHelper().addAttachment(file.getFilename(), file);
        }
        sender.send(helperBean.getMessage());
    }

    /**
    * @Name sendInlinResourceMail
    * @Description 发送图片邮件
    * @Author wen
    * @Date 2019/4/30
    * @param to
    * @param title
    * @param content
    * @param rscPath
    * @param rscId
    * @return void
    */
    public void sendInlinResourceMail(String to, String title, String content, String rscPath, String rscId) throws MessagingException {
        HelperBean helperBean = this.getMimeMessage(to, title, content);
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helperBean.getHelper().addInline(rscId, res);
        sender.send(helperBean.getMessage());
    }

    /**
    * @Name getMimeMessage
    * @Description 公共方法部分
    * @Author wen
    * @Date 2019/4/30
    * @param to
    * @param title
    * @param content
    * @return javax.mail.internet.MimeMessage
    */
    private HelperBean getMimeMessage(String to, String title, String content) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(title);
        helper.setText(content, true);
        helper.setFrom(from);
        return new HelperBean(message, helper);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class HelperBean{
        private MimeMessage message;
        private MimeMessageHelper helper;
    }

}

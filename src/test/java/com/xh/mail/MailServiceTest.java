package com.xh.mail;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * @Name MailServiceTest
 * @Description
 * @Author wen
 * @Date 2019-04-30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Value("${spring.mail.username}")
    private String to;

    @Autowired
    private MailService mailService;
    @Resource
    private TemplateEngine templateEngine;

    @Test
    public void sendSimpleMailTest() {
        mailService.sendSimpleMail(to, "springboot send a email", "你好我的邮件！");
    }

    @Test
    public void sendHtmlMailTest() throws MessagingException {
        String content = "<html>\n" +
                "<body>\n" +
                    "<h2>这是一份富文本邮件</h2>\n" +
                "</body>\n" +
                "<html>";
        mailService.sendHtmlMail(to, "springboot send a email-2", content);
    }

    @Test
    public void sendAttachmentMailTest() throws MessagingException {
        String[] filePaths = {"C:\\Users\\Administrator\\Desktop\\zxing-2.2.jar", "C:\\Users\\Administrator\\Desktop\\grass.rar"};
        mailService.sendAttachmentMail(to, "springboot send a email-3", "你好我的附件邮件！", filePaths);
    }

    @Test
    public void sendInlinResourceMailTest() throws MessagingException {
        String picPath = "C:\\Users\\Administrator\\Desktop\\grass.png";
        String rscId = "pic_001";
        String content = "<html>\n" +
                "<body>\n" +
                "<h2>这是一份图片邮件：</h2><img src=\'cid:" + rscId + "\'></img>\n" +
                "</body>\n" +
                "<html>";
        mailService.sendInlinResourceMail(to, "springboot send a email-3", content, picPath, rscId);
    }

    @Test
    public void sendTemplateMailTest() throws MessagingException {
        Context context = new Context();
        context.setVariable("id", "6");
        String emailContent = templateEngine.process("mail_template", context);
        mailService.sendHtmlMail(to, "springboot send a email-4", emailContent);
    }
}

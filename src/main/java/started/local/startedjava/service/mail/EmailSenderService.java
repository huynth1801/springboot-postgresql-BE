package started.local.startedjava.service.mail;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import freemarker.template.Configuration;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private final Configuration freemakerConfiguration;
    private final JavaMailSender javaMailSender;

    public void sendVerificationToken(String toEmail, Map<String, Object> attributes) {
        String text = getEmailContent("verify-email.ftlh", attributes);
        sendEmail(toEmail, "[Electro Shop] Xác thực email", text);
    }

    public void sendForgetPasswordToken(String toEmail, Map<String, Object> attributes) {
        String text = getEmailContent("forget-password-email.ftlh", attributes);
        sendEmail(toEmail, "[Electro Shop] Đặt lại mật khẩu", text);
    }

    private String getEmailContent(String templateName, Map<String, Object> model) {
        try (StringWriter writer = new StringWriter()) {
            freemakerConfiguration.getTemplate(templateName).process(model, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("Không thể load template email", e);
        }
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi email", e);
        }
    }
}

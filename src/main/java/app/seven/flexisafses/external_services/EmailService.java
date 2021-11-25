package app.seven.flexisafses.external_services;

import app.seven.flexisafses.models.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(@Autowired JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sentHappyBirthDayEmail(Student student) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@flexisaf.com");
            message.setTo(student.getEmail());
            message.setSubject("Happy birthday");
            message.setText("Use the verification code $code to complete your signup");
            mailSender.send(message);
        }catch (Exception e){
            //log error
            log.error(e.getMessage(), e);
        }
    }
}

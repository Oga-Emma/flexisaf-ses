package app.seven.flexisafses.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class JavaMailerConfiguration {
    public JavaMailerConfiguration(@Autowired EmailConfiguration configuration) {
        this.configuration = configuration;
    }

    EmailConfiguration configuration;

    @Bean
    public JavaMailSender getJavaMailSender()  {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(configuration.getHost());
        mailSender.setPort(configuration.getPort());
        mailSender.setUsername(configuration.getUsername());
        mailSender.setPassword(configuration.getPassword());
        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.enable", false);
        return mailSender;
    }
}

package com.software.grey.utils.emailsender;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EmailConfigurations {
    @Value("${grey.from}")
    private String username;
    @Value("${grey.password}")
    private String password;
    @Value("${grey.email.port}")
    private int emailPort;
    @Value("${grey.email.host}")
    private String host;

    @Bean
    public Mailer mailer() {
        return MailerBuilder
                .withSMTPServer(host, emailPort, username, password)
                .withSessionTimeout(120000)
                .async()
                .buildMailer();
    }
}

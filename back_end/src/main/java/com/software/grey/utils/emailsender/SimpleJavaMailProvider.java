package com.software.grey.utils.emailsender;

import lombok.AllArgsConstructor;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


//@Import(SimpleJavaMailSpringSupport.class)
@AllArgsConstructor
@Component
public class SimpleJavaMailProvider implements EmailProvider{

//    private Mailer mailer;
    @Override
    public void send(EmailDetails emailDetails) {
        Email email = EmailBuilder.startingBlank()
                .from(emailDetails.getFrom())
                .to(emailDetails.getTo())
                .withSubject(emailDetails.getSubject())
                .withPlainText(emailDetails.getContent())
                .buildEmail();

        Mailer inhouseMailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "greynoreply@gmail.com", "xkjhaauagkxaqhhp")
                .async()
                .buildMailer();
        inhouseMailer.sendMail(email, true);
    }
}

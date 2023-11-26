package com.software.grey.utils.emailsender;

import lombok.AllArgsConstructor;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class SimpleJavaMailProvider implements EmailProvider{

    private Mailer mailer;

    @Override
    public void send(EmailDetails emailDetails) {
        Email email = EmailBuilder.startingBlank()
                .from(emailDetails.getFrom())
                .to(emailDetails.getTo())
                .withSubject(emailDetails.getSubject())
                .withPlainText(emailDetails.getContent())
                .buildEmail();

        mailer.sendMail(email, true);
    }
}

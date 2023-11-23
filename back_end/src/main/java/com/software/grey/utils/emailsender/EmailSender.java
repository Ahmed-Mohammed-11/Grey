package com.software.grey.utils.emailsender;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailSender {
    private SimpleJavaMailProvider emailProvider;

    public void send(EmailDetails emailDetails){
        emailProvider.send(emailDetails);
    }
}

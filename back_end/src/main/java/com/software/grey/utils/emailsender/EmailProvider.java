package com.software.grey.utils.emailsender;

import org.springframework.stereotype.Component;

@Component
public interface EmailProvider {
    void send(EmailDetails emailDetails);
}

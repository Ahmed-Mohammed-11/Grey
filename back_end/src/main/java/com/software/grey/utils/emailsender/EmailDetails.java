package com.software.grey.utils.emailsender;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailDetails {
    private String to;
    private String from;
    private String subject;
    private String content;
}

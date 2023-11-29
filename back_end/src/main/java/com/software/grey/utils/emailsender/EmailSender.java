package com.software.grey.utils.emailsender;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.utils.EndPoints;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private SimpleJavaMailProvider emailProvider;
    private BasicUserRepo basicUserRepo;

    @Value("${grey.from}")
    private String fromAddress;
    @Value("${back.url}")
    private String backendURL;

    public EmailSender(SimpleJavaMailProvider emailProvider, BasicUserRepo basicUserRepo) {
        this.emailProvider = emailProvider;
        this.basicUserRepo = basicUserRepo;
    }

    public void send(EmailDetails emailDetails) {
        emailProvider.send(emailDetails);
    }

    public void send(UserDTO userDTO, String confirmationCode) {
        EmailDetails emailDetails = EmailDetails.builder()
                .from(fromAddress)
                .to(userDTO.email)
                .subject("Welcome to Grey!")
                .content(populateEmailContentMessage(basicUserRepo.findByEmail(userDTO.email).getId(), confirmationCode))
                .build();

        send(emailDetails);
    }

    private String populateEmailContentMessage(String userID, String confirmationCode) {
        String verificationURL = backendURL + EndPoints.VERIFY_REGISTERATION + "?userID=" + userID +
                "&registrationCode=" + confirmationCode;

        return "Please click the following link to verify email: " + verificationURL;
    }
}

package com.smartrevision.scheduler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final boolean mailEnabled;
    private final String mailProvider;
    private final String fromAddress;
    private final String fromName;
    private final String brevoApiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.enabled}") boolean mailEnabled,
            @Value("${app.mail.provider}") String mailProvider,
            @Value("${app.mail.from}") String fromAddress,
            @Value("${app.mail.from-name}") String fromName,
            @Value("${brevo.api-key}") String brevoApiKey
    ) {
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        this.mailProvider = mailProvider;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
        this.brevoApiKey = brevoApiKey;
    }

    public boolean sendOtp(String email, String otp, String purposeLabel) {
        if (!mailEnabled) {
            log.info("Development {} OTP for {} is {}", purposeLabel, email, otp);
            return false;
        }

        if ("brevo-api".equalsIgnoreCase(mailProvider)) {
            return sendOtpWithBrevoApi(email, otp, purposeLabel);
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(email);
            message.setSubject("Your Smart Revision " + purposeLabel + " OTP");
            message.setText("Your OTP is " + otp + ". It expires in 10 minutes.");
            mailSender.send(message);
            return true;
        } catch (MailException exception) {
            log.warn("Failed to send {} OTP to {}", purposeLabel, email, exception);
            return false;
        }
    }

    private boolean sendOtpWithBrevoApi(String email, String otp, String purposeLabel) {
        if (brevoApiKey == null || brevoApiKey.isBlank()) {
            log.warn("Brevo API mail provider is enabled, but BREVO_API_KEY is missing");
            return false;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);

            Map<String, Object> body = Map.of(
                    "sender", Map.of("name", fromName, "email", fromAddress),
                    "to", List.of(Map.of("email", email)),
                    "subject", "Your Smart Revision " + purposeLabel + " OTP",
                    "textContent", "Your OTP is " + otp + ". It expires in 10 minutes."
            );

            restTemplate.postForEntity(
                    "https://api.brevo.com/v3/smtp/email",
                    new HttpEntity<>(body, headers),
                    String.class
            );
            return true;
        } catch (RestClientException exception) {
            log.warn("Failed to send {} OTP to {} with Brevo API", purposeLabel, email, exception);
            return false;
        }
    }

    public boolean isMailEnabled() {
        return mailEnabled;
    }
}

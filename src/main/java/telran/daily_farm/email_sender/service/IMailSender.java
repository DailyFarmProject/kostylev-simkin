package telran.daily_farm.email_sender.service;

import org.springframework.http.ResponseEntity;

public interface IMailSender {
	
	ResponseEntity<String> sendEmailVerification(String email, String verificationToken);
	

	ResponseEntity<String> sendEmail(String email, String subject, String message);
}

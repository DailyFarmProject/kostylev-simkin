package telran.daily_farm.email_sender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class MailSenderService implements IMailSender {


    @Autowired
	JavaMailSender sender;

    

	@Override
	public ResponseEntity<String> sendEmailVerification(String email, String verificationToken) {
		log.debug("sendEmailVerification starts" );
		String link = "http://localhost:8080/farmers/verify-email?token="+ verificationToken;
		System.out.println("generated link - " + link);
		 String htmlContent =  
				 "<!DOCTYPE html>" +
				            "<html>" +
				            "<head><meta charset='UTF-8'></head>" +
				            "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
				            "<h2>Registration Confirmation</h2>" +
				            "<p>To complete your registration, click the button below:</p>" +

				            "<a href='" + link + "'" +
				            "style='display: inline-block; padding: 12px 24px; font-size: 16px; " +
				            "color: white; background-color: #28a745; text-decoration: none; " +
				            "border-radius: 5px; font-weight: bold;'>Confirm Email</a>" +

				            "<p>If the button does not work, copy and open the following link manually:</p>" +
				            "<p style='font-size: 8px;><a href='" + link + "'>" + link + "</a></p>" +
				            "</body></html>";

		sendEmail(email, "Email verification", htmlContent);
		
		return ResponseEntity.ok("Check your email for verification");
	}



	@Override
	public ResponseEntity<String> sendEmail(String email, String subject, String message) {
		MimeMessage  msg =  sender.createMimeMessage();
		  try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
			  helper.setTo(email);
		        helper.setSubject(subject);
		        helper.setFrom("daily-farm@gmail.com");
		        helper.setText(message, true);
		        sender.send(msg);
		} catch (MessagingException e) {
			return ResponseEntity.ok("Error with sending error");
		}
	
//		smm.setTo(email);
//		smm.setSubject(subject);
//		smm.setFrom("daily-farm@gmail.com");
//		smm.setText(message);
//		sender.send(smm);
		  
		return ResponseEntity.ok("Check your email for verification");

	}
}






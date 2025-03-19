package telran.daily_farm.email_sender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import static telran.daily_farm.api.ApiConstants.*;



@Service("mailSenderService")
@Slf4j
public class MailSenderService implements IMailSender {


    @Autowired
	JavaMailSender sender;

    @Value("${daily.farm.domain}")
    private String domain;
//    http://localhost:8080
//    https://daily-farm-latest.onrender.com

    
    
	@Override
	public ResponseEntity<String> sendEmailVerification(String email, String verificationToken, boolean isFarmer) {
		String verificationPath = isFarmer ? FARMER_EMAIL_VERIFICATION : CUSTOMER_EMAIL_VERIFICATION;
		String link = domain+verificationPath +"?token="+ verificationToken;
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
		  
		return ResponseEntity.ok("Check your email for verification");

	}



	public ResponseEntity<String> sendResetPassword(String email, String genPassword) {
		String htmlContent =  
				 "<!DOCTYPE html>" +
				            "<html>" +
				            "<head><meta charset='UTF-8'></head>" +
				            "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
				            "<h2>Reset password</h2>" +
				            "<p>To login you can use password below:</p>" +

				            "<p" +
				            "style='display: inline-block; padding: 12px 24px; font-size: 16px; " +
				            "color: white; background-color: #28a745; text-decoration: none; " +
				            "border-radius: 5px; font-weight: bold;'>"+ genPassword +"</a>" +

				            "<p style='font-size: 16px;'>We strongly recommend changing your password as soon as possible</p>" +
				           
				            "</body></html>";

		sendEmail(email, "Reset password", htmlContent);
		
		return ResponseEntity.ok("Check your email for new password");
		
	}



	public ResponseEntity<String> sendChangeEmailVerification(String email, String verificationTokenForUpdateEmail) {
		String link = domain + FARMER_NEW_EMAIL_VERIFICATION+"?token="+ verificationTokenForUpdateEmail;
		System.out.println("generated link - " + link);
		 String htmlContent =  
				 "<!DOCTYPE html>" +
				            "<html>" +
				            "<head><meta charset='UTF-8'></head>" +
				            "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
				            "<h2>Confirmation of email change</h2>" +
				            "<p>To complete email update, click the button below:</p>" +

				            "<a href='" + link + "'" +
				            "style='display: inline-block; padding: 12px 24px; font-size: 16px; " +
				            "color: white; background-color: #28a745; text-decoration: none; " +
				            "border-radius: 5px; font-weight: bold;'>Confirm changes</a>" +

				            "<p>If the button does not work, copy and open the following link manually:</p>" +
				            "<p style='font-size: 8px;><a href='" + link + "'>" + link + "</a></p>" +
				            "</body></html>";

		sendEmail(email, "Email verification", htmlContent);
		
		return ResponseEntity.ok("Check your email for verification");
	}



	public ResponseEntity<String> sendVerificationTokenToNewEmail(String newEmailFromToken, String token) {
		String link = domain + FARMER_CHANGE_EMAIL + "?token="+ token;
		System.out.println("generated link - " + link);
		 String htmlContent =  
				 "<!DOCTYPE html>" +
				            "<html>" +
				            "<head><meta charset='UTF-8'></head>" +
				            "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
				            "<h2>Confirmation of email change</h2>" +
				            "<p>To complete email update, click the button below:</p>" +

				            "<a href='" + link + "'" +
				            "style='display: inline-block; padding: 12px 24px; font-size: 16px; " +
				            "color: white; background-color: #28a745; text-decoration: none; " +
				            "border-radius: 5px; font-weight: bold;'>Confirm changes</a>" +

				            "<p>If the button does not work, copy and open the following link manually:</p>" +
				            "<p style='font-size: 8px;><a href='" + link + "'>" + link + "</a></p>" +
				            "</body></html>";

		sendEmail(newEmailFromToken, "Email verification", htmlContent);
		
		return ResponseEntity.ok("Check your email for verification");
		
	}
}






package telran.daily_farm.email_sender.service;

import static telran.daily_farm.api.ApiConstants.FARMER_CHANGE_EMAIL;
import static telran.daily_farm.api.ApiConstants.FARMER_EMAIL_VERIFICATION;
import static telran.daily_farm.api.ApiConstants.FARMER_NEW_EMAIL_VERIFICATION;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;


@Service
public class SendGridEmailSender implements IMailSender {

	@Value("${sendgrid.api.key}")
	private String sendgridApiKey;
	
	@Value("${sender.grid.from.email}")
	private String fromEmail;
	
    @Value("${daily.farm.domain}")
    private String domain;
	
	@Override
	public ResponseEntity<String> sendEmailVerification(String email, String verificationToken) {
		String link = domain + FARMER_EMAIL_VERIFICATION + "?token=" + verificationToken;
		String header = "Registration Confirmation";
		String text = "To complete your registration, click the button below:";
		String footer = "If the button does not work, copy and open the following link manually";
		String htmlContent = getHtmlContent(link, header,text, footer);  

		return sendEmail(email, "Email verification", htmlContent);
	}
	
	@Override
	public ResponseEntity<String> sendChangeEmailVerification(String email, String verificationTokenForUpdateEmail) {
		String link = domain + FARMER_NEW_EMAIL_VERIFICATION+"?token="+ verificationTokenForUpdateEmail;
		String header = "Confirmation of email change";
		String text = "To complete email update, click the button below:";
		String footer = "If the button does not work, copy and open the following link manually";
		String htmlContent = getHtmlContent(link, header, text, footer);  
		sendEmail(email, "Email verification", htmlContent);
		
		return ResponseEntity.ok("Check your email for verification");
	}
	
	@Override
	public ResponseEntity<String> sendVerificationTokenToNewEmail(String newEmailFromToken, String token) {
		String link = domain + FARMER_CHANGE_EMAIL + "?token="+ token;
		String header = "Confirmation of email change";
		String text = "To complete email update, click the button below:";
		String footer = "If the button does not work, copy and open the following link manually";
		String htmlContent = getHtmlContent(link, header,text, footer);  
		sendEmail(newEmailFromToken, "Email verification", htmlContent);
		
		return ResponseEntity.ok("Check your email for verification");
		
	}
	
	@Override
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

	private String getHtmlContent(String link, String header, String text, String footer) {
		
		return  "<!DOCTYPE html>" +
	            "<html>" +
	            "<head><meta charset='UTF-8'></head>" +
	            "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
	            "<h2>" + header + "</h2>" +
	            "<p> " + text + "</p>" +

	            "<a href='" + link + "'" +
	            "style='display: inline-block; padding: 12px 24px; font-size: 16px; " +
	            "color: white; background-color: #28a745; text-decoration: none; " +
	            "border-radius: 5px; font-weight: bold;'>Confirm Email</a>" +

	            "<p>" + footer + "</p>" +
	            "<p style='font-size: 8px;><a href='" + link + "'>" + link + "</a></p>" +
	            "</body></html>";
	}

	@Override
	public ResponseEntity<String> sendEmail(String email, String subject, String message) {
		Email from = new Email(fromEmail);
		
	    Email to = new Email(email);
	    Content content = new Content("text/html", message);
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid(sendgridApiKey);
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());
	      
	      
	    } catch (IOException ex) {
	    	return ResponseEntity.ok("server error");
	    }
		return ResponseEntity.ok("Check your email for verification");
	}

}

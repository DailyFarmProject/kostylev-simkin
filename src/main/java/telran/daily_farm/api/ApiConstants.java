package telran.daily_farm.api;

public interface ApiConstants {
	
	String FARMER_REGISTER="/farmer/register";
	String FARMER_EMAIL_VERIFICATION="/farmer/verify-email";
	String FARMER_EMAIL_VERIFICATION_RESEND="/farmer/verify-email/resend";
	
	String FARMER_EMAIL_CHANGE_VERIFICATION="/farmer/email-update";
	String FARMER_NEW_EMAIL_VERIFICATION="/farmer/verify-newemail";
	
	String FARMER_LOGIN="/farmer/login";
	String FARMER_LOGOUT="/farmer/logout";
	String FARMER_REMOVE="/farmer/";
	String FARMER_REFRESH_TOKEN="/farmer/refresh";
	String FARMER_CHANGE_PASSWORD="/farmer/password";
	String RESET_PASSWORD="/farmer/password-reset";
	String FARMER_CHANGE_EMAIL="/farmer/email";
	
	String FARMER_EDIT="/farmer/";

	String FARMER_CHANGE_COMPANY_NAME="/farmer/company";
	String FARMER_CHANGE_ADDRESS="/farmes/address";
	String FARMER_CHANGE_COORDINATES="/farmer/coordinates";
	String FARMER_CHANGE_PHONE="/farmer/phone";
	
	
	
    String CUSTOMER_REGISTER = "/customer/register";
    String CUSTOMER_LOGIN = "/customer/login";
    String CUSTOMER_REMOVE = "/customer/";
    String CUSTOMER_REFRESH_TOKEN = "/customer/refresh";
    String CUSTOMER_CHANGE_PASSWORD = "/customer/password";
    String CUSTOMER_CHANGE_EMAIL = "/customer/email";
    String CUSTOMER_EDIT = "/customer/";
    String CUSTOMER_CHANGE_FIRST_LAST_NAME = "/customer/name";
    String CUSTOMER_CHANGE_PHONE = "/customer/phone";
    
    


}

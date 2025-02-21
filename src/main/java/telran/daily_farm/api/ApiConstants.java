package telran.daily_farm.api;

public interface ApiConstants {
	
	String FARMER_REGISTER="/farmers/register";
	String FARMER_EMAIL_VERIFICATION="/farmers/verify-email";
	String FARMER_EMAIL_VERIFICATION_RESEND="/farmers/verify-email/resend";
	String FARMER_LOGIN="/farmers/login";
	String FARMER_LOGOUT="/farmers/logout";
	String FARMER_REMOVE="/farmers/";
	String FARMER_REFRESH_TOKEN="/farmers/refresh";
	String FARMER_CHANGE_PASSWORD="/farmers/password";
	String FARMER_CHANGE_EMAIL="/farmers/email";
	
	String FARMER_EDIT="/farmers/";

	String FARMER_CHANGE_FIRST_LAST_NAME_PASSWORD="/farmers/name";
	String FARMER_CHANGE_ADDRESS="/farmers/address";
	String FARMER_CHANGE_COORDINATES="/farmers/coordinates";
	String FARMER_CHANGE_PHONE="/farmers/phone";
	
	
	
	String CLIENT_REGISTER="/clients/register";
	String CLIENT_EDIT="/clients/";
	String CLIENT_LOGIN="/clients/login";
	String CLIENT_REMOVE="/clients/";

}

package telran.daily_farm.security.api;

public interface AuthApiConstants {

	String FARMER_REGISTER = "/farmer/register";
	String FARMER_EMAIL_VERIFICATION = "/farmer/verify-email";
	String FARMER_EMAIL_VERIFICATION_RESEND = "/farmer/verify-email/resend";

	String FARMER_EMAIL_CHANGE_VERIFICATION = "/farmer/email-update";
	String FARMER_NEW_EMAIL_VERIFICATION = "/farmer/verify-newemail";

	String FARMER_LOGIN = "/farmer/login";
	String FARMER_LOGOUT = "/farmer/logout";

	String FARMER_REFRESH_TOKEN = "/farmer/refresh";
	String FARMER_CHANGE_PASSWORD = "/farmer/password";
	String RESET_PASSWORD = "/farmer/password-reset";
	String FARMER_CHANGE_EMAIL = "/farmer/email";
	String FARMER_REMOVE = "/farmer/";
	String GET_ALL_SETS = "/farm-sets";
	
	String GET_LANGUAGES = "/languages";
	
	String GET_CATEGORIES = "/categories";
	String GET_SIZES = "/sizes";
}

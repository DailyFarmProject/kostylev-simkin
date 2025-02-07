package telran.daily_farm.api.messages;

public interface ErrorMessages {

	String NOT_VALID_COUNTRY = "The provided country is not valid.";
	String NOT_VALID_CITY = "The provided city is not valid.";
	
	String NAME_IS_NOT_VALID = "The provided name is not valid.";
	String LAST_NAME_IS_NOT_VALID = "The provided last name is not valid.";
	
	String PHONE_NUMBER_IS_NOT_VALID = "The provided phone number is not valid.";
	String EMAIL_IS_NOT_VALID = "The provided email is not valid.";
	
	String PASSWORD_IS_NOT_VALID = "Password must be at least 8 characters long";
	
	String ADDRESS_FIELD_IS_REQUIRED = "Addrees field is requiared.";
	
	String PAYPAL_CLIENT_ID_INVALID = "Invalid PayPal Client ID format.";
    String PAYPAL_SECRET_INVALID = "Invalid PayPal Secret format.";
    String PAYPAL_DETAILS_IS_REQUIRED = "PayPal field is requiared.";
    
    String WRONG_USER_TYPE = "Wrong user type."; 
    
    String WRONG_USER_NAME_OR_PASSWORD = "Wrong user name or password";
    String USER_NOT_FOUND = "User is not found";
    String INVALID_TOKEN = "Invalid or expired JWT token";
    
    
    String FARMER_WITH_THIS_EMAIL_EXISTS = "Farmer with this email exists";
    String FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS = "Farmer with this email is not exists";
    
    String ERROR_EDIT_OWN_ACCOUNT_ONLY = "You can edit only your own account";
    String ERROR_DELETE_OWN_ACCOUNT_ONLY = "You can delete only your own account";
}

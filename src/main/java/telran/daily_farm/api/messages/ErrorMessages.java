package telran.daily_farm.api.messages;

public interface ErrorMessages {

	String NOT_VALID_COUNTRY = "The provided country is not valid.";
	String NOT_VALID_CITY = "The provided city is not valid.";
	String NOT_VALID_REGION = "The provided region is not valid.";
	String NOT_VALID_STREET = "The provided street is not valid.";

	String NAME_IS_NOT_VALID = "The provided name is not valid.";
	String LAST_NAME_IS_NOT_VALID = "The provided last last name is not valid.";

	String PHONE_NUMBER_IS_NOT_VALID = "The provided phone number is not valid.";
	String EMAIL_IS_NOT_VALID = "The provided email is not valid.";

	String PASSWORD_IS_NOT_VALID = "Password must be at least 8 characters long";

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

	String ADDRESS_FIELD_IS_REQUIRED = "Addrees field is requiared.";
	String LOCATION_REQUIRED = "Either address or coordinates must be fully specified";
	String COUNTRY_REQUIRED = "Country is required";
	String REGION_REQUIRED = "Region is required";
	String CITY_REQUIRED = "City or nearest populated place is required";
	String POSTAL_CODE_REQUIRED = "Postal code is required";
	String INVALID_POSTAL_CODE = "Invalid postal code format";
	String HOUSE_NUMBER_TOO_LONG = "House number must not exceed 10 characters";
	String ADDITIONAL_INFO_TOO_LONG = "Additional info must not exceed 255 characters";

	String LATITUDE_REQUIRED = "Latitude is required";
	String LONGITUDE_REQUIRED = "Longitude is required";
	String INVALID_LATITUDE = "Latitude must be between -90 and 90 degrees";
	String INVALID_LONGITUDE = "Longitude must be between -180 and 180 degrees";
	String ADDRESS_VALIDATION_FAILED = "Address validation failed: low relevance or incorrect place type";
	String COULD_NOT_RETRIVE_COORDINATES = "Could not retrieve coordinates";
	String WRONG_COORDINATES = "No location or places were found for the given input";
}

package telran.daily_farm.api;

public interface ApiConstants {
	
	String FARMER_REGISTER="/farmers/register";
	String FARMER_LOGIN="/farmers/login";
	String FARMER_REMOVE="/farmers/";
	String FARMER_REFRESH_TOKEN="/farmers/refresh";
	String FARMER_CHANGE_PASSWORD="/farmers/password";
	String FARMER_CHANGE_EMAIL="/farmers/email";
	
	String FARMER_EDIT="/farmers/";

	String FARMER_CHANGE_FIRST_LAST_NAME_PASSWORD="/farmers/name";
	String FARMER_CHANGE_ADDRESS="/farmers/address";
	String FARMER_CHANGE_COORDINATES="/farmers/coordinates";
	String FARMER_CHANGE_PHONE="/farmers/phone";
	
	
	
    String CUSTOMER_REGISTER = "/customer/register";
    String CUSTOMER_LOGIN = "/customer/login";
    String CUSTOMER_REMOVE = "/customer/";
    String CUSTOMER_REFRESH_TOKEN = "/customer/refresh";
    String CUSTOMER_CHANGE_PASSWORD = "/customer/password";
    String CUSTOMER_CHANGE_EMAIL = "/customer/email";
    String CUSTOMER_EDIT = "/customer/";
    String CUSTOMER_CHANGE_FIRST_LAST_NAME = "/customer/name";
    String CUSTOMER_CHANGE_PHONE = "/customer/phone";
    
    
    String CLIENT_VIEW_PRODUCTS = "/clients/products"; // View available products
    String CLIENT_ADD_TO_CART = "/clients/cart/add"; // Add product to cart
    String CLIENT_REMOVE_FROM_CART = "/clients/cart/remove"; // Remove product from cart
    String CLIENT_GET_CART = "/clients/cart"; // Get cart contents
    String CLIENT_CREATE_ORDER = "/clients/order"; // Create a new order
    String CLIENT_GET_ORDERS = "/clients/orders"; // Get order history
    String CLIENT_GET_ORDER_DETAILS = "/clients/orders/{id}"; // Get details of a specific order
    
    
    String CLIENT_SEND_FEEDBACK = "/clients/feedback"; // Submit product feedback
    String CLIENT_GET_SUPPORT = "/clients/support"; // Contact support service

}

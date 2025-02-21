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
	
	
	
    String CLIENT_REGISTER = "/clients/register";
    String CLIENT_LOGIN = "/clients/login";
    String CLIENT_REMOVE = "/clients/";
    String CLIENT_REFRESH_TOKEN = "/clients/refresh";
    String CLIENT_CHANGE_PASSWORD = "/clients/password";
    String CLIENT_CHANGE_EMAIL = "/clients/email";
    String CLIENT_EDIT = "/clients/";
    String CLIENT_CHANGE_FIRST_LAST_NAME = "/clients/name";
    String CLIENT_CHANGE_PHONE = "/clients/phone";
    
    
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

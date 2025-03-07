package telran.daily_farm.exeption_controller;

import java.util.stream.Collectors;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.ApiResponse;

@ControllerAdvice
@Slf4j
public class ExeptionController {
	
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), 404);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
	
	
	@ExceptionHandler(JwtException.class)
	  public ResponseEntity<String> handleJwtException(HttpServletRequest request) {
	        String message = (String) request.getAttribute("JWT_ERROR");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message != null ? message : "Invalid token");
	    }
	
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	
	 @ExceptionHandler(ResponseStatusException.class)
	    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
	        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
	    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> argumentNotVAlidHandler(MethodArgumentNotValidException e){
		String message = e.getAllErrors().stream().map(error -> error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HandlerMethodValidationException.class)
	ResponseEntity<String> argumentNotVAlidHandler(HandlerMethodValidationException e){
		String message = e.getAllErrors().stream().map(error -> error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}

}

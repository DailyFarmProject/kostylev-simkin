package telran.daily_farm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
	private String status;
    private T data;
    private String message;
    private int code;
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("success", data, message, 200);
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        return new ApiResponse<>("error", null, message, code);
    }

}

package utp.bambinochicken.bambinochicken.shared.api;

public record ApiResponse<T>(
        String message,
        T data
) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(message, data);
    }
}

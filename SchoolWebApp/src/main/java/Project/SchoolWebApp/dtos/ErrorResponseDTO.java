package Project.SchoolWebApp.dtos;

/**
 * Record used to show responses of internal errors, usually runtime exceptions.
 * @param message
 * @param errorType
 * @param errorNumber
 */
public record ErrorResponseDTO(String message, String errorType, int errorNumber) {
}

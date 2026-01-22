package Project.SchoolWebApp.dtos;

import java.time.LocalDateTime;

/**
 * Record used to show responses of internal errors, usually runtime exceptions.
 * @param message
 * @param errorType
 * @param status
 */
public record ErrorResponseDTO(String message, String errorType, LocalDateTime timestamp, int status) {
}

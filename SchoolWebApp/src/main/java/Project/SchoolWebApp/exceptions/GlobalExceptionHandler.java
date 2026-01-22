package Project.SchoolWebApp.exceptions;

import Project.SchoolWebApp.dtos.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataNotFound(DataNotFoundException ex){

        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), ex.getClass().getSimpleName(),
                                                     LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataConflict(DataConflictException ex){

        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), ex.getClass().getSimpleName(),
                LocalDateTime.now(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(BadRequestException ex){

        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), ex.getClass().getSimpleName(),
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
                       HandlerMethodValidationException.class,
                       ConstraintViolationException.class,
                       HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDTO> handleValidationError(Exception ex){

        String message = "Invalid data request";

        if( ex instanceof MethodArgumentNotValidException){

            message = ((MethodArgumentNotValidException) ex).getBindingResult()
                                                            .getFieldErrors()
                                                            .stream()
                                                            .map(FieldError::getDefaultMessage)
                                                            .collect(Collectors.joining("; "));
        }

        if(ex instanceof ConstraintViolationException){

            message = ((ConstraintViolationException) ex)
                    .getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; "));
        }

        if (ex instanceof HandlerMethodValidationException){

            message = ((HandlerMethodValidationException) ex).getMessage();
        }

        ErrorResponseDTO error = new ErrorResponseDTO(message,
                                            "ValidationError",
                                                      LocalDateTime.now(),
                                                      HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}

package Project.SchoolWebApp.exceptions;

import Project.SchoolWebApp.dtos.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> errorResponse(){

        ErrorResponseDTO error = new ErrorResponseDTO("", "DataNotFoundException",
                                                     HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponseDTO> DataConflictErrorResponse(){

        ErrorResponseDTO error = new ErrorResponseDTO("msg", "DataConflictException",
                HttpStatus.CONFLICT.value());
        return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> badRequestError(){

        ErrorResponseDTO errorResponse = new ErrorResponseDTO("", "BadRequestException",
                                                              HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<ErrorResponseDTO>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}

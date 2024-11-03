package org.example.bookmanager.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleBookNofFoundException(BookNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());

        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, messageSource.getMessage(
                        "errors.400",
                        null,
                        locale)
                );
        problemDetail.setProperty("errors",
                e.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList());

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }
}

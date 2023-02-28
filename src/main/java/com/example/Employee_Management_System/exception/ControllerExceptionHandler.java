package com.example.Employee_Management_System.exception;

import com.example.Employee_Management_System.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            NotFoundException.class,
            NotFoundException.class,
            RegisterException.class,
            ReportException.class,
            IllegalStateException.class,
            LoginFailedException.class
    })
    public ResponseEntity<Response> handleException(Exception ex) {
        return new ResponseEntity<>(
                Response
                        .builder()
                        .status(400)
                        .message(ex.getMessage())
                        .build()
                , HttpStatus.OK);
    }
}

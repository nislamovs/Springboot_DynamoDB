package com.example.dynamoDemo.controllers;


import static java.time.LocalDateTime.now;

import com.example.dynamoDemo.domain.exceptions.ErrorResponse;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  @Value("${application.support.email}")
  private String supportEmail;

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler({ProductNotFoundException.class})
  public ErrorResponse handleNotFoundException(RuntimeException ex) {

    log.info("Bad request : Requested data not found " + ex.getMessage());

    return new ErrorResponse(ex.getMessage());
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({
      ValidationException.class,
      IllegalArgumentException.class})
  public ErrorResponse handleBadRequests(Exception ex) {

    log.info("Bad request : Request error " + ex.getMessage());

    return new ErrorResponse(ex.getMessage());
  }

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({Exception.class})
  public ErrorResponse handleGenericException(Exception ex) {

    log.error("Internal exception: ", ex);

    final String message = "Unexpected problem encountered. Please contact support team (" + supportEmail + ") with timestamp ["
        + now() + "] and short description.";

    return new ErrorResponse(message);
  }

  private List<ErrorResponse> processError(MethodArgumentNotValidException ex) {
    List<ErrorResponse> errors = new ArrayList<>();

    ex.getBindingResult().getAllErrors()
        .forEach(err -> errors.add(new ErrorResponse(err.getDefaultMessage())));

    return errors;
  }

}

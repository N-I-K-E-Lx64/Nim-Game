package com.holisticon.nimgame;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * An Exception handler that forwards the exception message to the user, so he is informed about
   * what causes the error.
   * @param ex Exception
   * @param request Request
   * @return Response containing the error message.
   */
  @ExceptionHandler(value = { RuntimeException.class })
  protected ResponseEntity<?> handleExceptions(RuntimeException ex, WebRequest request) {
    logger.error(ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

}

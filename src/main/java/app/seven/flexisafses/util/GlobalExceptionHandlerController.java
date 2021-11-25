package app.seven.flexisafses.util;

import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.models.exception.CustomException;
import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.util.response.ErrorResponse;
import com.mongodb.MongoException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

  @Bean
  public ErrorAttributes errorAttributes() {
    // Hide exception field in the return object
    return new DefaultErrorAttributes() {
      @Override
      public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        errorAttributes.remove("exception");
        return errorAttributes;
      }
    };
  }

  @ExceptionHandler(value = CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
    return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage()), ex.getHttpStatus());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<?> handleBadRequestException(HttpServletResponse res, BadRequestException ex) throws IOException {
//    res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

      return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> handleNotFoundException(HttpServletResponse res, NotFoundException ex) throws IOException {
//    res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
  }

 @ExceptionHandler(MongoException.class)
  public ResponseEntity<?> handleMongoException(HttpServletResponse res, MongoException ex) throws IOException {
     return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
  }

 @ExceptionHandler(InternalAuthenticationServiceException.class)
  public ResponseEntity<?> handleInterAuthException(HttpServletResponse res, InternalAuthenticationServiceException ex) throws IOException {
     return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(HttpServletResponse res) throws IOException {
//    res.sendError(HttpStatus.FORBIDDEN.value(), );
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Access denied, you may not be authorized to access this route"));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleMongoValidationException(HttpServletResponse res, ConstraintViolationException e) throws IOException {
//    res.sendError(HttpStatus.BAD_REQUEST.value(), "Data is not valid: " + e.getMessage());
      return ResponseEntity.badRequest().body(new ErrorResponse("Data is not valid: " + e.getMessage()));

  }

  @ExceptionHandler(RequestRejectedException.class)
  public ResponseEntity<?> handleException(RequestRejectedException ex) throws IOException {
      System.out.println("Error occurred => " + ex);
//    res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage() != null ? ex.getMessage() : "Something went wrong 1");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex.getMessage() != null ? ex.getMessage() : "Something went wrong"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(HttpServletResponse res, Exception ex) throws IOException {
      System.out.println("Error occurred => " + ex);
//    res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage() != null ? ex.getMessage() : "Something went wrong 1");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex.getMessage() != null ? ex.getMessage() : "Something went wrong"));
  }

}

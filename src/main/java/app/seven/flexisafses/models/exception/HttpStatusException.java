package app.seven.flexisafses.models.exception;

import org.springframework.http.HttpStatus;

public class HttpStatusException extends Exception{
    String message;
    HttpStatus status;

    public HttpStatusException(String message, HttpStatus status){
        super(message);
        this.message = message;
        this.status = status;
    }
}

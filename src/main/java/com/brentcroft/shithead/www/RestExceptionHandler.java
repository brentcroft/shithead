package com.brentcroft.shithead.www;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {

    @Getter
    @Setter
    class ExceptionResponse
    {
        String type;
        String message;
        ExceptionResponse cause;

        ExceptionResponse(Throwable e)
        {
            type = e.getClass().getSimpleName();
            message = e.getMessage();
            if (Objects.nonNull(e.getCause()) )
            {
                cause = new ExceptionResponse(e.getCause());
            }
        }
    }


    @ExceptionHandler(value = { Exception.class})
    protected ResponseEntity<Object> handleConflict( Exception e, WebRequest request)
    {
        String responseText = "{ \"error\": " + JSONRenderer.render(new ExceptionResponse(e)) + " }";

        log.warn("Error response: " + responseText);

        return handleExceptionInternal( e, responseText, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


}
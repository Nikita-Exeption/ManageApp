package org.nikita.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class BadRequestControllerAdvice {

    private final MessageSource source;

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handlerBindException(BindException exception, Locale locale) {
        ProblemDetail detail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        source.getMessage("error.400.title", new Object[0], "errors.400.title", locale));
        detail.setProperty("errors", exception.getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .toList());
        return ResponseEntity.badRequest()
                .body(detail);
    }
}

package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@SuppressWarnings("unused")
@ControllerAdvice
public class DefaultAdvice {
    public static final String MESSAGE_KEY = "message";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleInternalException(RuntimeException e) {
        return new ResponseEntity<>(getBody(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(getBody(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(getBody(e), HttpStatus.NOT_FOUND);
    }

    private static Map<String, String> getBody(RuntimeException e) {
        return Map.of(MESSAGE_KEY, e.getMessage());
    }
}

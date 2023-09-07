package com.devcourse.be04daangnmarket.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
		String errorMessage = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

}

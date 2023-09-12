package com.devcourse.be04daangnmarket.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devcourse.be04daangnmarket.image.exception.FileDeleteException;
import com.devcourse.be04daangnmarket.image.exception.FileUploadException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class CommonExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> invalidHandle(IllegalArgumentException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> noSuchElementHandle(NoSuchElementException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(
			{
					FileUploadException.class,
					FileDeleteException.class
			}
	)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> fileHandle(FileException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> validationHandle(MethodArgumentNotValidException exception) {
		String errorMessage = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
}

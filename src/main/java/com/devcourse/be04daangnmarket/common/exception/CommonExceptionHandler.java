package com.devcourse.be04daangnmarket.common.exception;

import com.devcourse.be04daangnmarket.member.exception.DuplicatedUsernameException;
import com.devcourse.be04daangnmarket.member.exception.NotFoundMemberException;
import com.devcourse.be04daangnmarket.post.exception.NotFoundPostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devcourse.be04daangnmarket.comment.exception.NotFoundCommentException;
import com.devcourse.be04daangnmarket.image.exception.FileDeleteException;
import com.devcourse.be04daangnmarket.image.exception.FileUploadException;

@RestControllerAdvice
public class CommonExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> invalidHandle(IllegalArgumentException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}

	@ExceptionHandler(
			{
					NotFoundCommentException.class,
					NotFoundMemberException.class,
					NotFoundPostException.class
			}
	)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> notFoundHandle(NotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler(DuplicatedUsernameException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<String> duplicationUserHandle(DuplicatedUsernameException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
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
	public ResponseEntity<String> validationExceptionHandle(MethodArgumentNotValidException exception) {
		String errorMessage = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
}

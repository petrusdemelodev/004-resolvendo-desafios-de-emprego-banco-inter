package dev.petrusdemelo.desafioi.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dev.petrusdemelo.desafioi.domain.exceptions.InvalidPublicKeySizeException;
import dev.petrusdemelo.desafioi.domain.exceptions.UserIsAlreadyEncryptedException;
import dev.petrusdemelo.desafioi.services.exceptions.UserNotFoundException;
import dev.petrusdemelo.desafioi.shared.PublicKeyParser.InvalidPublicKeyException;
import lombok.Builder;
import lombok.Data;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidPublicKeySizeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorModel> handleInvalidPublicKeySizeException(InvalidPublicKeySizeException ex) {

		var body = ErrorModel.builder()
			.message(ex.getMessage())
			.statusCode(HttpStatus.BAD_REQUEST.toString())
			.build();

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidPublicKeyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorModel> handleInvalidPublicKeyException(InvalidPublicKeyException ex) {
		var body = ErrorModel.builder()
			.message(ex.getMessage())
			.statusCode(HttpStatus.BAD_REQUEST.toString())
			.build();

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorModel> handleIllegalArgumentException(IllegalArgumentException ex) {
		var body = ErrorModel.builder()
			.message(ex.getMessage())
			.statusCode(HttpStatus.BAD_REQUEST.toString())
			.build();

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserIsAlreadyEncryptedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorModel> handleUserIsAlreadyEncryptedException(UserIsAlreadyEncryptedException ex) {
		var body = ErrorModel.builder()
			.message(ex.getMessage())
			.statusCode(HttpStatus.BAD_REQUEST.toString())
			.build();

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorModel> handleUserNotFoundException(UserNotFoundException ex) {
		var body = ErrorModel.builder()
			.message(ex.getMessage())
			.statusCode(HttpStatus.NOT_FOUND.toString())
			.build();

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex,
		HttpHeaders headers, 
		HttpStatusCode status, 
		WebRequest request) {

		var errors = new ArrayList<String>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}

		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		var body = ErrorModel.builder()
      .messages(errors)
      .statusCode(HttpStatus.BAD_REQUEST.toString()).build();

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
    MissingServletRequestParameterException ex,
		HttpHeaders headers, 
		HttpStatusCode status, 
		WebRequest request) {
		String message = "Query parameter '" + ex.getParameterName() + "' is required";

		var body = ErrorModel.builder()
      .message(message)
      .statusCode(HttpStatus.BAD_REQUEST.toString())
      .build();

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(
		ServletRequestBindingException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {

		var body = ErrorModel.builder()
			.message(ex.getMessage())
			.statusCode(HttpStatus.BAD_REQUEST.toString())
      .build();

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorModel> handleRuntimeException(RuntimeException ex) {
		var body = ErrorModel.builder()
      .message(ex.getMessage())
      .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
      .build();

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, 
			HttpHeaders headers, 
			HttpStatusCode status, 
			WebRequest request) {
				var body = ErrorModel.builder()
					.message(ex.getMessage())
					.statusCode(HttpStatus.BAD_REQUEST.toString())
					.build();
	
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorModel> handleException(Exception ex) {
		var body = ErrorModel.builder()
			.message(ex.getMessage())
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			.build();

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}	
}

@Data
@Builder
@JsonPropertyOrder({ "statusCode", "message", "messages" })
@JsonInclude(Include.NON_NULL)
class ErrorModel implements Serializable {
	private static final long serialVersionUID = -9054193687126443791L;
	private String statusCode;
	private String message;
	private Collection<String> messages;
}
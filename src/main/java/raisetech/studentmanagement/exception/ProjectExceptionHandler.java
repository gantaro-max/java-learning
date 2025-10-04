package raisetech.studentmanagement.exception;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import raisetech.studentmanagement.domain.ErrorResponse;
import raisetech.studentmanagement.domain.ValidationError;

@RestControllerAdvice
public class ProjectExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ProjectExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> validationException(MethodArgumentNotValidException e,
      HttpServletRequest request) {
    logger.warn("入力にバリデーションエラーがあります", e);
    List<ValidationError> validationErrors = e.getBindingResult().getFieldErrors().stream().map(
        fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
        .toList();
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
        "入力にバリデーションエラーがあります", request.getRequestURI(), validationErrors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException e,
      HttpServletRequest request) {
    logger.warn("不正な入力です", e);
    List<ValidationError> validationErrors = e.getConstraintViolations().stream().map(violation -> {
      String fieldName = null;
      for (Path.Node node : violation.getPropertyPath()) {
        fieldName = node.getName();
      }
      if (fieldName == null)
        fieldName = "unknownField";
      return new ValidationError(fieldName, violation.getMessage());
    }).toList();

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
        "不正な入力です", request.getRequestURI(), validationErrors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(ResourceNotFoundException e,
      HttpServletRequest request) {
    logger.warn("該当ありません", e);
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
        "該当ありません", request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> otherException(Exception e, HttpServletRequest request) {
    logger.error("サーバー内部で予期せぬエラーが発生しました", e);
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error", "サーバー内部で予期せぬエラーが発生しました", request.getRequestURI());
    return ResponseEntity.internalServerError().body(errorResponse);
  }

}

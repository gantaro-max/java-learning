package raisetech.studentmanagement.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ProjectExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<Map<String, String>>> validationException(
      MethodArgumentNotValidException e) {
    List<Map<String, String>> errorMsg = e.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> {
          Map<String, String> error = new HashMap<>();
          error.put("field", fieldError.getField());
          error.put("message",
              fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage()
                  : "エラーメッセージが設定されていません");
          return error;
        }).toList();
    return ResponseEntity.badRequest().body(errorMsg);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<List<Map<String, String>>> constViolaException(
      ConstraintViolationException e) {
    List<Map<String, String>> errorMsg = e.getConstraintViolations().stream().map(cveError -> {
      Map<String, String> cveErrorMsg = new HashMap<>();
      cveErrorMsg.put("field", cveError.getPropertyPath().toString());
      cveErrorMsg.put("message", cveError.getMessage());
      return cveErrorMsg;
    }).toList();

    return ResponseEntity.badRequest().body(errorMsg);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> notFoundException(ResourceNotFoundException e) {
    Map<String, String> body = new HashMap<>();
    body.put("message", e.getMessage());
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> otherException(Exception e) {
    logger.error("サーバー内部で予期せぬエラーが発生しました", e);
    Map<String, String> body = new HashMap<>();
    body.put("message", "サーバー内部で予期せぬエラーが発生しました");
    return ResponseEntity.internalServerError().body(body);
  }

}

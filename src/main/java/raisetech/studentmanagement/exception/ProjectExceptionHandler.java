package raisetech.studentmanagement.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionHandler {

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

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> notFoundException(ResourceNotFoundException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> otherException(Exception e) {
    return new ResponseEntity<>("サーバー内部で予期せぬエラーが発生しました",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

}

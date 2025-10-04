package raisetech.studentmanagement.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private final ZonedDateTime timestamp;
  private final int status;
  private final String error;
  private final String message;
  private final String path;
  @JsonInclude(Include.NON_NULL)
  private final List<ValidationError> errors;

  //NotFoundエラー、InternalServerエラー用
  public ErrorResponse(int status, String error, String message, String path) {
    this.timestamp = ZonedDateTime.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.errors = null;
  }

  public ErrorResponse(int status, String error, String message, String path,
      List<ValidationError> errors) {
    this.timestamp = ZonedDateTime.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.errors = errors;
  }


}

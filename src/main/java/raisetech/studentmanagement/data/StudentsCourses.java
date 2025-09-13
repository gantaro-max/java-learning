package raisetech.studentmanagement.data;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  @NotEmpty(message = "コースIDは空にできません。")
  private String courseId;
  @NotEmpty(message = "IDは空にできません。")
  private String studentId;
  @NotEmpty(message = "コース名は空にできません。")
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime completeDate;

}

package raisetech.studentmanagement.data;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"takeCourseId", "courseId", "studentId", "startDate"})
public class StudentsCourses {

  @NotEmpty(message = "受講コースIDは空にできません")
  private String takeCourseId;
  @NotEmpty(message = "コースIDは空にできません")
  private String courseId;
  @NotEmpty(message = "IDは空にできません")
  private String studentId;
  @NotEmpty(message = "コース名は空にできません")
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime completeDate;

}

package raisetech.studentmanagement.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private String courseId;
  private String studentId;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime completeDate;

}

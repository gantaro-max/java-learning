package raisetech.studentmanagement.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;

@Getter
@Setter
@NoArgsConstructor
public class StudentDetail {

  @Valid
  @NotNull(message = "受講生情報は必須です。")
  private Student student;
  private List<StudentsCourses> studentsCourses;
  @NotNull(message = "コース選択は必須です。")
  private Integer courseNum;

  public StudentDetail(Student student, List<StudentsCourses> studentsCourses) {
    this.student = student;
    this.studentsCourses = studentsCourses;
    this.courseNum = 0;
  }

}

package raisetech.studentmanagement.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;

@Getter
@Setter
public class StudentDetail {

  @Valid
  @NotNull(message = "受講生情報は必須です。")
  private Student student;
  @Valid
  @NotEmpty(message = "コース内容が作成出来ていません。")
  private List<StudentsCourses> studentsCourses;
  @Valid
  @NotNull(message = "コース選択は必須です。")
  private Integer courseNum;

}

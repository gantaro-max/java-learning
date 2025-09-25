package raisetech.studentmanagement.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.studentmanagement.data.StudentsCourses;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private ResponseStudent responseStudent;
  private List<StudentsCourses> studentsCourses;
}

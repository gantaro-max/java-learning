package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.studentmanagement.data.Apply;
import raisetech.studentmanagement.data.StudentsCourses;

@Schema(description = "Response用受講生情報および受講生コース情報")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Schema(description = "Response用受講生情報")
  private ResponseStudent responseStudent;
  @Schema(description = "受講生コース情報")
  private List<StudentsCourses> studentsCourses;
  @Schema(description = "受講生コース申込状況")
  private List<Apply> applyList;
}

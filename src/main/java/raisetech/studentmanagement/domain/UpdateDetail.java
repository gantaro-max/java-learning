package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDetail {

  @Schema(description = "Update用受講生情報")
  private UpdateStudent updateStudent;
  @Schema(description = "Update用受講生コース情報と申込状況")
  private List<UpCourseApply> upCourseApplyList;

}

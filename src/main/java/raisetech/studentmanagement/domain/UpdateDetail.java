package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDetail {

  @Schema(description = "Update用受講生情報")
  @Valid
  private UpdateStudent updateStudent;
  @Schema(description = "Update用受講生コース情報と申込状況")
  @Valid
  private List<UpCourseApply> upCourseApplyList;

}

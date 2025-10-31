package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpCourseApply {

  @Schema(description = "受講ID", requiredMode = RequiredMode.REQUIRED)
  @NotNull(message = "受講IDは空にできません")
  private String takeCourseId;
  @Schema(description = "コースID", requiredMode = RequiredMode.REQUIRED)
  @NotNull(message = "コースIDは空にできません")
  private String courseId;
  @Schema(description = "申込ID", requiredMode = RequiredMode.REQUIRED)
  @NotNull(message = "申込IDは空にできません")
  private String applyId;
  @Schema(description = "申込状況", requiredMode = RequiredMode.REQUIRED)
  @NotNull(message = "申込状況は空にできません")
  private String applyStatus;

}

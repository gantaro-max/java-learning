package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "登録用受講生情報")
@Getter
@Setter
public class RegisterStudent {

  @Schema(description = "受講生名", requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "名前は必須です")
  private String fullName;
  @Schema(description = "受講生名（カナ）", requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "カナ名は必須です")
  private String kanaName;
  @Schema(description = "ニックネーム")
  private String nickName;
  @Schema(description = "メールアドレス", requiredMode = RequiredMode.REQUIRED)
  @NotBlank(message = "emailは必須です")
  @Email(message = "有効なメールアドレス形式で入力して下さい")
  private String email;
  @Schema(description = "地域")
  private String address;
  @Schema(description = "年齢", requiredMode = RequiredMode.REQUIRED)
  @NotNull(message = "年齢は空にできません")
  @Min(value = 18, message = "登録は18以上になります")
  private Integer age;
  @Schema(description = "性別")
  private String gender;
  @Schema(description = "備考")
  private String remark;
  @Schema(description = "コースID", requiredMode = RequiredMode.REQUIRED)
  @NotEmpty(message = "コースIDは空にできません")
  private String courseId;
}

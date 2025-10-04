package raisetech.studentmanagement.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterStudent {

  @NotBlank(message = "名前は必須です")
  private String fullName;
  @NotBlank(message = "カナ名は必須です")
  private String kanaName;
  private String nickName;
  @NotBlank(message = "emailは必須です")
  @Email(message = "有効なメールアドレス形式で入力して下さい")
  private String email;
  private String address;
  @NotNull(message = "年齢は空にできません")
  @Min(value = 18, message = "登録は18以上になります")
  private Integer age;
  private String gender;
  private String remark;
  @NotEmpty(message = "コースIDは空にできません")
  private String courseId;
}

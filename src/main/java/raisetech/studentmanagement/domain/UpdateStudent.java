package raisetech.studentmanagement.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudent {

  @NotBlank(message = "名前は空にできません")
  private String fullName;
  @NotBlank(message = "カナ名は空にできません")
  private String kanaName;
  private String nickName;
  @NotBlank(message = "emailは空にできません")
  @Email(message = "有効なメールアドレス形式で入力して下さい。")
  private String email;
  private String address;
  @NotNull(message = "年齢は空にできません。")
  @Min(value = 18, message = "登録は18以上になります。")
  private Integer age;
  private String gender;
  private String remark;
  private boolean deleted;
}

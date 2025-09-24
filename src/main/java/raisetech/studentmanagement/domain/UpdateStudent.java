package raisetech.studentmanagement.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudent {

  private String nickName;
  @NotEmpty(message = "emailは空にできません。")
  @Email(message = "有効なメールアドレス形式で入力して下さい。")
  private String email;
  private String address;
  @Min(value = 18, message = "登録は18以上になります。")
  private String age;
  private String gender;
  private String remark;
  private boolean deleted;
}

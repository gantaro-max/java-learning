package raisetech.studentmanagement.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudent {

  @NotEmpty(message = "IDは空にできません。")
  private String studentId;
  @NotEmpty(message = "名前は空にできません。")
  private String fullName;
  @NotEmpty(message = "カナ名は空にできません。")
  private String kanaName;
  private String nickName;
  @NotEmpty(message = "emailは空にできません。")
  @Email(message = "有効なメールアドレス形式で入力して下さい。")
  private String email;
  private String address;
  @Min(value = 18, message = "登録は18以上になります。")
  private Integer age;
  private String gender;
  private String remark;
  private boolean deleted;
}

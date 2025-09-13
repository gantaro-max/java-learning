package raisetech.studentmanagement.data;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Student {

  private String studentId;
  @NotEmpty(message = "名前は空にできません。")
  private String fullName;
  @NotEmpty(message = "カナ名は空にできません。")
  private String kanaName;
  private String nickName;
  @NotEmpty(message = "emailは空にできません。")
  private String email;
  private String address;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}

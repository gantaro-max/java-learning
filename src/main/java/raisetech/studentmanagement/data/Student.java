package raisetech.studentmanagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String studentId;
  private String fullName;
  private String kanaName;
  private String nickName;
  private String email;
  private String address;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;

  @Override
  public String toString() {
    return "id:" + studentId + ",name:" + fullName + ",(" + kanaName + "," + nickName + ")\n"
        + "mail:" + email + ",area:" + address + ",age:" + age + ",gender:" + gender + ",備考:"
        + remark;
  }
}

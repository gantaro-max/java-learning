package raisetech.studentmanagement.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  private String studentId;
  private String fullName;
  private String kanaName;
  private String nickName;
  private String email;
  private String address;
  private Integer age;
  private String gender;
  private String remark;
  private boolean deleted;
}

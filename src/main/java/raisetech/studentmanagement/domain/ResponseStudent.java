package raisetech.studentmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStudent {

  private String studentId;
  private String fullName;
  private String kanaName;
  private String nickName;
  private String email;
  private String address;
  private Integer age;
  private String gender;
  private String remark;

}

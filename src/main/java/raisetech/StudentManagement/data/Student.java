package raisetech.StudentManagement.data;

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
}

package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Response用受講生情報")
@Getter
@Setter
@EqualsAndHashCode(of = "studentId")
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStudent {

  @Schema(description = "受講生ID")
  private String studentId;
  @Schema(description = "受講生名")
  private String fullName;
  @Schema(description = "受講生名（カナ）")
  private String kanaName;
  @Schema(description = "ニックネーム")
  private String nickName;
  @Schema(description = "メールアドレス")
  private String email;
  @Schema(description = "地域")
  private String address;
  @Schema(description = "年齢")
  private Integer age;
  @Schema(description = "性別")
  private String gender;
  @Schema(description = "備考")
  private String remark;

}

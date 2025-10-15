package raisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.UpdateStudent;
import raisetech.studentmanagement.exception.ResourceNotFoundException;
import raisetech.studentmanagement.service.StudentService;

@WebMvcTest(StudentController.class)
@Import(StudentControllerTest.TestConfig.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private StudentService studentService;
  @Autowired
  private ObjectMapper objectMapper;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @TestConfiguration
  static class TestConfig {

    @Bean
    public StudentService studentService() {
      return Mockito.mock(StudentService.class);
    }
  }

  @Test
  void 受講生詳細一覧検索が動作し空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/students")).andExpect(status().isOk());
    verify(studentService, times(1)).getStudentDetailList();
  }

  @Test
  void 受講生新規登録が正常に行われることれること() throws Exception {
    RegisterStudent registerStudent = new RegisterStudent();
    registerStudent.setFullName("山田花子");
    registerStudent.setKanaName("ヤマダハナコ");
    registerStudent.setNickName("ハナコ");
    registerStudent.setEmail("yamahana@example.com");
    registerStudent.setAddress("東京都杉並区");
    registerStudent.setAge(22);
    registerStudent.setGender("女");
    registerStudent.setRemark("なし");
    registerStudent.setCourseId("4001");

    Set<ConstraintViolation<RegisterStudent>> violations = validator.validate(registerStudent);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 存在しないIDの生徒を取得しようとした際に404を返す() throws Exception {
    String testUuid = "00000000-0000-0000-0000-000000000000";
    when(studentService.getStudentDetail(testUuid))
        .thenThrow(new ResourceNotFoundException("該当ありません"));
    mockMvc.perform(get("/students/" + testUuid)).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.timestamp").isNotEmpty()).andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("該当ありません"))
        .andExpect(jsonPath("$.path").value("/students/" + testUuid))
        .andExpect(jsonPath("$.errors").doesNotExist());
  }

  @Test
  void IDに空白が入っていた際に400を返す() throws Exception {
    String invalidStudentId = " ";
    mockMvc.perform(get("/students/" + invalidStudentId)).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isNotEmpty()).andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("不正な入力です"))
        .andExpect(jsonPath("$.path").value("/students/%20"))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[*].message", hasItem("IDの形式が不正です")));
  }

  @Test
  void 新規登録バリデーションエラーの際に400を返す() throws Exception {
    RegisterStudent invalidRegister = new RegisterStudent();
    invalidRegister.setFullName("");
    invalidRegister.setKanaName("");
    invalidRegister.setEmail("");
    invalidRegister.setAge(null);
    invalidRegister.setCourseId("");

    mockMvc
        .perform(post("/students").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRegister)))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("入力にバリデーションエラーがあります"))
        .andExpect(jsonPath("$.path").value("/students"))
        .andExpect(jsonPath("$.errors", hasSize(5)))
        .andExpect(
            jsonPath("$.errors[*].message", containsInAnyOrder("名前は必須です", "カナ名は必須です",
                "emailは必須です", "年齢は空にできません", "コースIDは空にできません")));
  }

  @Test
  void 更新処理バリデーションエラーの際に400を返す() throws Exception {
    UpdateStudent invalidUpdate = new UpdateStudent();
    invalidUpdate.setFullName("");
    invalidUpdate.setKanaName("");
    invalidUpdate.setEmail("345678");
    invalidUpdate.setAge(5);

    mockMvc
        .perform(put("/students/1").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidUpdate)))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("入力にバリデーションエラーがあります"))
        .andExpect(jsonPath("$.path").value("/students/1"))
        .andExpect(jsonPath("$.errors", hasSize(4)))
        .andExpect(jsonPath("$.errors[*].message",
            containsInAnyOrder("名前は空にできません", "カナ名は空にできません",
                "有効なメールアドレス形式で入力して下さい", "登録は18以上になります")));
  }

  @Test
  void 予期せぬエラーが発生した際に500を返す() throws Exception {
    String testUuid = "43a70504-27d3-42f2-8590-a66c4886779a";
    when(studentService.getStudentDetail(testUuid)).thenThrow(
        new RuntimeException("データベース接続エラー"));
    mockMvc.perform(get("/students/" + testUuid)).andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").isNotEmpty()).andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.message").value("サーバー内部で予期せぬエラーが発生しました"))
        .andExpect(jsonPath("$.path").value("/students/43a70504-27d3-42f2-8590-a66c4886779a"))
        .andExpect(jsonPath("$.errors").doesNotExist());
  }


}

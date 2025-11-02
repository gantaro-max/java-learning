package raisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.studentmanagement.data.Apply;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.ResponseStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpCourseApply;
import raisetech.studentmanagement.domain.UpdateDetail;
import raisetech.studentmanagement.domain.UpdateStudent;
import raisetech.studentmanagement.exception.ResourceNotFoundException;
import raisetech.studentmanagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private StudentService studentService;

  @Captor
  private ArgumentCaptor<RegisterStudent> captorRegister;

  @Captor
  private ArgumentCaptor<UpdateDetail> captorUpdateDetail;

  @BeforeEach
  void setCaptor() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void 受講生詳細一覧検索が動作し空のリストが返ってくること() throws Exception {
    List<StudentDetail> studentDetailList = new ArrayList<>();
    when(studentService.getStudentDetailList()).thenReturn(studentDetailList);
    mockMvc.perform(get("/students")).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
    verify(studentService, times(1)).getStudentDetailList();
  }

  @Test
  void 受講生新規登録が正常に行われること() throws Exception {
    String responseId = "00000000-0000-0000-0000-000000000000";

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
    StudentDetail registerDetail = new StudentDetail();
    ResponseStudent responseStudent = new ResponseStudent();
    responseStudent.setStudentId(responseId);
    registerDetail.setResponseStudent(responseStudent);
    registerDetail.setStudentsCourses(new ArrayList<>(List.of(new StudentsCourses())));
    registerDetail.setApplyList(new ArrayList<>(List.of(new Apply())));

    when(studentService.setStudentNewCourse(any(RegisterStudent.class))).thenReturn(registerDetail);

    mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerStudent)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.responseStudent.studentId").value(responseId));
    verify(studentService, times(1)).setStudentNewCourse(captorRegister.capture());

    assertThat(captorRegister.getValue().getFullName()).isEqualTo(registerStudent.getFullName());
  }

  @Test
  void 受講生IDで受講生の検索が正常に行われること() throws Exception {
    String testStudentId = "00000000-0000-0000-0000-000000000000";
    StudentDetail studentDetail = new StudentDetail();
    ResponseStudent responseStudent = new ResponseStudent();
    responseStudent.setStudentId(testStudentId);
    studentDetail.setResponseStudent(responseStudent);
    studentDetail.setStudentsCourses(new ArrayList<>());
    Optional<StudentDetail> opDetail = Optional.of(studentDetail);

    when(studentService.getStudentDetail(testStudentId)).thenReturn(opDetail);

    mockMvc.perform(get("/students/" + testStudentId)).andExpect(status().isOk())
        .andExpect(jsonPath("$.responseStudent.studentId").value(testStudentId));

    verify(studentService, times(1)).getStudentDetail(testStudentId);

  }

  @Test
  void 受講生_受講生コース情報_申込状況の更新が正常に行われること() throws Exception {
    String testStudentId = "00000000-0000-0000-0000-000000000000";
    UpdateStudent updateStudent = new UpdateStudent();
    updateStudent.setFullName("山田花子");
    updateStudent.setKanaName("ヤマダハナコ");
    updateStudent.setNickName("ハナコ");
    updateStudent.setEmail("yamahana@example.com");
    updateStudent.setAddress("東京都杉並区");
    updateStudent.setAge(22);
    updateStudent.setGender("女");
    updateStudent.setRemark("なし");
    updateStudent.setDeleted(false);

    String testTakeCourseId = "77777777-8888-9999-1111-222222222222";
    String testApplyId = "99999999-9999-9999-9999-999999999999";
    UpCourseApply upCourseApply = new UpCourseApply();
    upCourseApply.setTakeCourseId(testTakeCourseId);
    upCourseApply.setCourseId("4001");
    upCourseApply.setApplyId(testApplyId);
    upCourseApply.setApplyStatus("受講中");
    List<UpCourseApply> upCourseApplyList = new ArrayList<>(List.of(upCourseApply));
    UpdateDetail updateDetail = new UpdateDetail(updateStudent, upCourseApplyList);

    StudentDetail responseDetail = new StudentDetail();
    ResponseStudent responseStudent = new ResponseStudent();
    responseStudent.setStudentId(testStudentId);
    responseStudent.setFullName(updateStudent.getFullName());
    responseStudent.setKanaName(updateStudent.getKanaName());
    responseStudent.setNickName(updateStudent.getNickName());
    responseStudent.setEmail(updateStudent.getEmail());
    responseStudent.setAddress(updateStudent.getAddress());
    responseStudent.setAge(updateStudent.getAge());
    responseStudent.setGender(updateStudent.getGender());
    responseStudent.setRemark(updateStudent.getRemark());
    responseDetail.setResponseStudent(responseStudent);

    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId(testTakeCourseId);
    studentsCourses.setStudentId(testStudentId);
    studentsCourses.setCourseName("JAVA");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 10, 10, 10));
    responseDetail.setStudentsCourses(new ArrayList<>(List.of(studentsCourses)));

    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId(testTakeCourseId);
    apply.setApplyStatus(upCourseApply.getApplyStatus());
    responseDetail.setApplyList(new ArrayList<>(List.of(apply)));

    when(studentService.updateStudent(any(UpdateDetail.class), eq(testStudentId))).thenReturn(
        responseDetail);

    mockMvc.perform(put("/students/" + testStudentId).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDetail))).andExpect(status().isOk())
        .andExpect(jsonPath("$.responseStudent.studentId").value(testStudentId));

    verify(studentService, times(1)).updateStudent(captorUpdateDetail.capture(), eq(testStudentId));

    assertThat(captorUpdateDetail.getValue()).isEqualTo(updateDetail);


  }

  @Test
  void 存在しないIDの受講生を取得しようとした際に404を返す() throws Exception {
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

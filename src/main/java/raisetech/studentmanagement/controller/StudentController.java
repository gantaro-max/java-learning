package raisetech.studentmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

/**
 * 受講生の検索や更新などを行うREST APIとして受け付けるControllerです。
 */
@RestController
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索です。 論理削除している受講生を除く受講生検索を行います。
   *
   * @return 受講生一覧（論理削除を除く全件）
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.getStudentDetailList();
  }

  /**
   * 受講生コース一覧検索です。 論理削除している受講生を除いて受講生に紐づく受講生コース情報の検索を行います。
   *
   * @return 受講生コース一覧（論理削除を除く全件）
   */
  @GetMapping("/studentsCoursesList")
  public List<StudentDetail> getStudentCoursesList() {
    return service.getStudentDetailList();
  }

  /**
   * 受講生の登録処理です。受講生情報と受講生コース情報を登録します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録処理の結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<String> getRegisterStudent(@RequestBody StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    student.setStudentId(UUID.randomUUID().toString());
    StudentsCourses newCourse = converter.getConvertNewCourse(studentDetail, student);
    Student registerStudent = service.setStudentNewCourse(student, newCourse);
    return ResponseEntity.ok("登録処理が成功しました studentId:" + registerStudent.getStudentId());
  }

  /**
   * 受講生検索です。 studentIdに紐づく任意の受講生の情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  @GetMapping("/updateView/{studentId}")
  public StudentDetail updateView(@PathVariable("studentId") String studentId) {
    Optional<StudentDetail> detail = service.getStudentDetail(studentId);
    StudentDetail studentDetail = new StudentDetail();
    if (detail.isPresent()) {
      studentDetail = detail.get();
    }
    return studentDetail;
  }

  /**
   * 受講生情報の更新処理です。 受講生の更新を行いその結果を返します。
   *
   * @param student 受講生情報
   * @return 更新処理の結果
   */
  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody Student student) {
    if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
      return new ResponseEntity<>("リクエストが不正です", HttpStatus.BAD_REQUEST);
    }
    Optional<StudentDetail> searchStudent = service.getStudentDetail(student.getStudentId());
    if (searchStudent.isEmpty()) {
      return new ResponseEntity<>("該当の受講生が見つかりません", HttpStatus.NOT_FOUND);
    }
    service.updateStudent(student);
    return ResponseEntity.ok("更新処理が成功しました");
  }

}

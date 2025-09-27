package raisetech.studentmanagement.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpdateStudent;
import raisetech.studentmanagement.exception.ResourceNotFoundException;
import raisetech.studentmanagement.service.StudentService;

/**
 * 受講生の検索や更新などを行うREST APIとして受け付けるControllerです。
 */
@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。 論理削除している受講生を除く受講生詳細一覧の検索を行います。
   *
   * @return 受講生詳細一覧（論理削除を除く全件）
   */
  @GetMapping("/students")
  public List<StudentDetail> getStudentCoursesList() {
    return service.getStudentDetailList();
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param registerStudent 受講生登録情報
   * @return 登録処理の結果
   */
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> getRegisterStudent(
      @Valid @RequestBody RegisterStudent registerStudent) {
    StudentDetail registerDetail = service.setStudentNewCourse(registerStudent);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{studentId}")
        .buildAndExpand(registerDetail.getResponseStudent().getStudentId()).toUri();
    return ResponseEntity.created(location).body(registerDetail);
  }

  /**
   * 受講生検索です。 studentIdに紐づく任意の受講生の情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 検索処理の結果
   */
  @GetMapping("/students/{studentId}")
  public ResponseEntity<StudentDetail> updateView(@PathVariable("studentId") String studentId) {
    Optional<StudentDetail> detail = service.getStudentDetail(studentId);
    if (detail.isEmpty()) {
      throw new ResourceNotFoundException("該当が見つかりませんでした ID:" + studentId);
    }
    StudentDetail studentDetail = detail.get();
    return new ResponseEntity<>(studentDetail, HttpStatus.OK);
  }

  /**
   * 受講生情報の更新処理です。 受講生の更新を行いその結果を返します。
   *
   * @param updateStudent 受講生更新情報
   * @return 更新処理の結果
   */
  @PutMapping("/students/{studentId}")
  public ResponseEntity<StudentDetail> updateStudent(@PathVariable("studentId") String studentId,
      @Valid @RequestBody UpdateStudent updateStudent) {
    StudentDetail updateDetail = service.updateStudent(updateStudent, studentId);
    return new ResponseEntity<>(updateDetail, HttpStatus.OK);
  }

}

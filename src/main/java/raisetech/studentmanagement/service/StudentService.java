package raisetech.studentmanagement.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Apply;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.ResponseStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpdateDetail;
import raisetech.studentmanagement.exception.ResourceNotFoundException;
import raisetech.studentmanagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。 論理削除している受講生を除く受講生詳細一覧の検索を行います。
   *
   * @return 受講生詳細一覧（論理削除を除く全件）
   */
  public List<StudentDetail> getStudentDetailList() {
    List<Student> students = repository.getStudentList();
    List<StudentsCourses> studentsCourses = repository.getStudentCourseList();
    List<Apply> applyList = repository.getApplyList();
    return converter.convertStudentDetailList(students, studentsCourses, applyList);
  }

  /**
   * 受講生検索です。 studentIdに紐づく受講生の情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  public Optional<StudentDetail> getStudentDetail(String studentId) {
    Student student = repository.getStudentById(studentId);
    Optional<Student> opStudent = Optional.ofNullable(student);
    if (opStudent.isEmpty()) {
      return Optional.empty();
    }
    Student foundStudent = opStudent.get();
    ResponseStudent responseStudent = converter.convertStudentToResponse(foundStudent);
    List<Apply> applyList = repository.getApplyList();
    List<StudentsCourses> studentCourses = repository.getStudentCourse(studentId);
    List<Apply> studentApply = converter.convertApplyListByStudentCourses(applyList,
        studentCourses);

    StudentDetail studentDetail = new StudentDetail(responseStudent, studentCourses,
        studentApply);
    return Optional.of(studentDetail);
  }

  /**
   * 受講生と受講生コース情報と申込状況の登録を行います。
   *
   * @param registerStudent 受講生登録情報
   * @return 受講生詳細
   */
  @Transactional
  public StudentDetail setStudentNewCourse(RegisterStudent registerStudent) {
    Student student = converter.convertRegisterToStudent(registerStudent);
    StudentsCourses newStudentCourse = converter.convertStudentCourse(registerStudent, student);
    Apply newApply = converter.convertApply(newStudentCourse);
    repository.setStudentData(student);
    repository.setNewCourse(newStudentCourse);
    repository.setNewApply(newApply);
    ResponseStudent responseStudent = converter.convertStudentToResponse(student);
    List<StudentsCourses> newStudentsCourses = repository.getStudentCourse(student.getStudentId());
    List<Apply> allApplyList = repository.getApplyList();
    List<Apply> applyList = converter.convertApplyListByStudentCourses(allApplyList,
        newStudentsCourses);
    return new StudentDetail(responseStudent, newStudentsCourses, applyList);
  }

  /**
   * 受講生の更新処理を行います。
   *
   * @param updateDetail 更新用受講生詳細情報
   * @return 受講生詳細
   */
  @Transactional
  public StudentDetail updateStudent(UpdateDetail updateDetail, String studentId) {
    Student searchStudent = repository.getStudentById(studentId);
    if (searchStudent == null) {
      throw new ResourceNotFoundException("該当の受講生が見つかりません");
    }
    List<StudentsCourses> searchCourses = repository.getStudentCourse(studentId);

    List<Apply> allApplyList = repository.getApplyList();
    List<Apply> searchApply = converter.convertApplyListByStudentCourses(allApplyList,
        searchCourses);

    Student newStudent = converter.convertUpdateToStudent(updateDetail.getUpdateStudent(),
        searchStudent);
    List<StudentsCourses> newStudentsCourses = converter.convertUpdateToCourses(
        updateDetail.getUpCourseApplyList(), searchCourses);
    List<Apply> newApplyList = converter.convertUpdateToApply(updateDetail.getUpCourseApplyList(),
        searchApply);
    repository.updateStudent(newStudent);
    for (StudentsCourses course : newStudentsCourses) {
      repository.updateStudentsCourses(course);
    }
    for (Apply apply : newApplyList) {
      repository.updateApply(apply);
    }
    ResponseStudent responseStudent = converter.convertStudentToResponse(newStudent);

    return new StudentDetail(responseStudent, newStudentsCourses, newApplyList);
  }

}

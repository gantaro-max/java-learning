package raisetech.studentmanagement.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpdateStudent;
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
    return converter.convertStudentDetails(students, studentsCourses);
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
    StudentDetail studentDetail = new StudentDetail(foundStudent,
        repository.getStudentCourses(studentId));
    return Optional.of(studentDetail);
  }

  /**
   * 受講生と受講生コース情報の登録を行います。
   *
   * @param student   受講生
   * @param newCourse 受講生コース情報
   * @return 受講生
   */
  @Transactional
  public Student setStudentNewCourse(Student student, StudentsCourses newCourse) {
    repository.setStudentData(student);
    repository.setNewCourse(newCourse);
    return student;
  }

  @Transactional
  public StudentDetail setStudentNewCourse(RegisterStudent registerStudent) {
    Student student = converter.convertRegisterToStudent(registerStudent);
    StudentsCourses newStudentCourse = converter.convertStudentCourse(registerStudent, student);
    repository.setStudentData(student);
    repository.setNewCourse(newStudentCourse);
    List<StudentsCourses> newStudentsCourses = repository.getStudentCourses(student.getStudentId());
    return new StudentDetail(student, newStudentsCourses);
  }

  /**
   * 受講生の更新処理を行います。
   *
   * @param student 受講生
   */
  @Transactional
  public void updateStudent(Student student) {
    repository.updateStudent(student);
  }

  @Transactional
  public StudentDetail updateStudent(UpdateStudent updateStudent) {
    Student newStudent = converter.convertUpdateToStudent(updateStudent);
    repository.updateStudent(newStudent);
    List<StudentsCourses> studentsCourses = repository.getStudentCourses(newStudent.getStudentId());
    return new StudentDetail(newStudent, studentsCourses);
  }

}

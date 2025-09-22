package raisetech.studentmanagement.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.StudentDetail;
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
   * 受講生の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（全件）
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
   * @return 受講生
   */
  public Optional<StudentDetail> getStudentDetail(String studentId) {
    Student student = repository.getStudentById(studentId);
    Optional<Student> opStudent = Optional.ofNullable(student);
    if (opStudent.isEmpty()) {
      return Optional.empty();
    }
    Student foundStudent = opStudent.get();
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(foundStudent);
    studentDetail.setStudentsCourses(repository.getStudentCourses(studentId));
    return Optional.of(studentDetail);
  }

  @Transactional
  public Student setStudentNewCourse(Student student, StudentsCourses newCourse) {
    repository.setStudentData(student);
    repository.setNewCourse(newCourse);
    return student;
  }

  @Transactional
  public void updateStudent(Student student) {
    repository.updateStudent(student);
  }

}

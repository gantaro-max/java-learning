package raisetech.studentmanagement.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> getStudentList() {
    return repository.getStudentList();
  }

  public List<StudentsCourses> getStudentCourseList() {
    return repository.getStudentCourseList();
  }

  public Optional<StudentDetail> getStudentDetail(String studentId) {
    Student student = repository.getStudentData(studentId);
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

  public Student getStudentData(String studentId) {
    return repository.getStudentData(studentId);
  }

  @Transactional
  public void setStudentNewCourse(Student student, StudentsCourses newCourse) {
    repository.setStudentData(student);
    repository.setNewCourse(newCourse);
  }

  @Transactional
  public void updateStudent(Student student) {
    repository.updateStudent(student);
  }

}

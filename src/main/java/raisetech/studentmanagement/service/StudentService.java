package raisetech.studentmanagement.service;

import java.util.List;
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

  public StudentDetail getStudentDetail(String studentId) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(repository.getStudentCourses(studentId));
    studentDetail.setStudent(repository.getStudentData(studentId));
    return studentDetail;
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

package raisetech.studentmanagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
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

//  public void setStudentData(Student student) {
//    repository.setStudentData(student);
//  }
//  public void setNewCourse(StudentsCourses newCourse) {
//    repository.setNewCourse(newCourse);
//  }

  @Transactional
  public void setStudentNewCourse(Student student, StudentsCourses newCourse) {
    repository.setStudentData(student);
    repository.setNewCourse(newCourse);
  }

}

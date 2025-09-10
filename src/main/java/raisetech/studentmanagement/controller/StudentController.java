package raisetech.studentmanagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

@RestController
public class StudentController {

  private final StudentService studentService;
  private final StudentConverter studentConverter;

  @Autowired
  public StudentController(StudentService studentService, StudentConverter studentConverter) {
    this.studentService = studentService;
    this.studentConverter = studentConverter;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = studentService.getStudentList();
    List<StudentsCourses> studentCourses = studentService.getStudentCourseList();

    return studentConverter.convertStudentDetails(students, studentCourses);
  }

  @GetMapping("/students-courses")
  public List<StudentsCourses> getStudentCoursList() {
    return studentService.getStudentCourseList();
  }

}

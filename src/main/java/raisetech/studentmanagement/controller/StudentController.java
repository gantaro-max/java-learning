package raisetech.studentmanagement.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

@Controller
public class StudentController {

  private final StudentService studentService;
  private final StudentConverter studentConverter;

  @Autowired
  public StudentController(StudentService studentService, StudentConverter studentConverter) {
    this.studentService = studentService;
    this.studentConverter = studentConverter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = studentService.getStudentList();
    List<StudentsCourses> studentCourses = studentService.getStudentCourseList();

    model.addAttribute("studentList",
        studentConverter.convertStudentDetails(students, studentCourses));

    return "studentList";
  }

  @GetMapping("/students-courses")
  public List<StudentsCourses> getStudentCoursList() {
    return studentService.getStudentCourseList();
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String getRegisterStudent(@ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    Student student = studentDetail.getStudent();
    student.setStudentId(UUID.randomUUID().toString());
    StudentsCourses newCourse = studentConverter.getConvertNewCourse(studentDetail,
        student);

    studentService.setStudentData(student);
    studentService.setNewCourse(newCourse);

    System.out.println(
        studentDetail.getStudent() + "\n登録されました。");
    return "redirect:/studentList";
  }

}

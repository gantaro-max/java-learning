package raisetech.studentmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/studentsCoursesList")
  public String getStudentCoursesList(Model model) {
    List<Student> students = studentService.getStudentList();
    List<StudentsCourses> studentCourses = studentService.getStudentCourseList();

    List<StudentDetail> studentDetailList = studentConverter.convertStudentDetails(students,
        studentCourses);

    model.addAttribute("studentDetailList", studentDetailList);

    return "studentsCoursesList";
  }

  @GetMapping("/students-courses")
  public List<StudentsCourses> getStudentCoursList() {
    return studentService.getStudentCourseList();
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(new Student());
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String getRegisterStudent(@Valid @ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    Student student = studentDetail.getStudent();
    student.setStudentId(UUID.randomUUID().toString());
    StudentsCourses newCourse = studentConverter.getConvertNewCourse(studentDetail, student);

    studentService.setStudentNewCourse(student, newCourse);

    return "redirect:/studentList";
  }

  @GetMapping("/updateView/{studentId}")
  public String updateView(@PathVariable("studentId") String studentId, Model model) {
    Optional<StudentDetail> studentDetail = studentService.getStudentDetail(studentId);
    if (studentDetail.isEmpty()) {
      String nullStudentMsg = "該当の受講生が見つかりません。";
      List<Student> students = studentService.getStudentList();
      List<StudentsCourses> studentCourses = studentService.getStudentCourseList();
      model.addAttribute("studentList",
          studentConverter.convertStudentDetails(students, studentCourses));
      model.addAttribute("errorMsg", nullStudentMsg);
      return "studentList";
    }
    model.addAttribute("student", studentDetail.get().getStudent());
    model.addAttribute("studentsCourses", studentDetail.get().getStudentsCourses());
    return "updateStudent";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@Valid @ModelAttribute Student student,
      BindingResult result, Model model) {
    if (result.hasErrors()) {
      Optional<StudentDetail> opStudentDetail = studentService.getStudentDetail(
          student.getStudentId());
      opStudentDetail.ifPresentOrElse(detail -> {
        model.addAttribute("studentsCourses", detail.getStudentsCourses());
      }, () -> {
        model.addAttribute("errorMsg", "該当コースが見つかりません");
      });
      return "updateStudent";
    }
    studentService.updateStudent(student);
    return "redirect:/studentList";
  }

}

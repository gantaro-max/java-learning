package raisetech.studentmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/studentsCoursesList")
  public List<StudentDetail> getStudentCoursesList() {
    List<Student> students = studentService.getStudentList();
    List<StudentsCourses> studentCourses = studentService.getStudentCourseList();
    return studentConverter.convertStudentDetails(students, studentCourses);
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
  public String getRegisterStudent(StudentDetail studentDetail) {
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
  public ResponseEntity<String> updateStudent(@RequestBody Student student) {
    if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
      return new ResponseEntity<>("リクエストが不正です", HttpStatus.BAD_REQUEST);
    }
    Student searchStudent = studentService.getStudentData(student.getStudentId());
    if (searchStudent == null) {
      return new ResponseEntity<>("該当の受講生が見つかりません", HttpStatus.NOT_FOUND);
    }
    studentService.updateStudent(student);
    return ResponseEntity.ok("更新処理が成功しました");
  }

}

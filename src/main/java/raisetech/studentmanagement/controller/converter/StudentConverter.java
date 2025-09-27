package raisetech.studentmanagement.controller.converter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.ResponseStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpdateStudent;

/**
 * 受講生詳細を受講性や受講生コース情報、もしくはその逆に変換するコンバーターです。
 */
@Component
public class StudentConverter {

  private final Map<String, String> courses = Map.of(
      "1001", "AWS", "2001", "WP", "3001", "FE",
      "4001", "JAVA", "5001", "DE", "6001", "WM");

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   *
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      ResponseStudent responseStudent = convertStudentToResponse(student);
      studentDetail.setResponseStudent(responseStudent);
      List<StudentsCourses> convertStudentCourses = studentCourses.stream()
          .filter(studentCourse -> student.getStudentId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentsCourses(convertStudentCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails.stream()
        .sorted(Comparator.comparing(std -> std.getResponseStudent().getStudentId())).toList();
  }

  public String getCourseNameById(String courseId) {
    return courses.entrySet().stream().filter(course -> course.getKey().equals(courseId))
        .map(Entry::getValue).findFirst().orElse("該当なし");
  }

  public StudentsCourses convertStudentCourse(RegisterStudent registerStudent, Student student) {
    StudentsCourses studentsCourses = new StudentsCourses();

    studentsCourses.setCourseId(String.valueOf(registerStudent.getCourseId()));
    studentsCourses.setStudentId(student.getStudentId());
    studentsCourses.setCourseName(getCourseNameById(registerStudent.getCourseId()));
    studentsCourses.setStartDate(LocalDateTime.now());

    return studentsCourses;
  }

  public Student convertRegisterToStudent(RegisterStudent registerStudent) {
    return new Student(UUID.randomUUID().toString(), registerStudent.getFullName(),
        registerStudent.getKanaName(), registerStudent.getNickName(), registerStudent.getEmail(),
        registerStudent.getAddress(), registerStudent.getAge(), registerStudent.getGender(),
        registerStudent.getRemark(), false);
  }

  public Student convertUpdateToStudent(UpdateStudent updateStudent, Student student) {
    student.setFullName(updateStudent.getFullName());
    student.setKanaName(updateStudent.getKanaName());
    student.setNickName(updateStudent.getNickName());
    student.setEmail(updateStudent.getEmail());
    student.setAddress(updateStudent.getAddress());
    student.setAge(updateStudent.getAge());
    student.setGender(updateStudent.getGender());
    student.setRemark(updateStudent.getRemark());
    student.setDeleted(updateStudent.isDeleted());

    return student;
  }

  public ResponseStudent convertStudentToResponse(Student student) {
    return new ResponseStudent(student.getStudentId(), student.getFullName(), student.getKanaName(),
        student.getNickName(), student.getEmail(), student.getAddress(), student.getAge(),
        student.getGender(), student.getRemark());
  }


}

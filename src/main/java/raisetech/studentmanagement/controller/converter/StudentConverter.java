package raisetech.studentmanagement.controller.converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.StudentDetail;

@Component
public class StudentConverter {

  private final Map<Integer, String> courses = Map.of(
      1001, "AWS", 2001, "WP", 3001, "FE",
      4001, "JAVA", 5001, "DE", 6001, "WM");

  public Map<String, StudentsCourses> getStringStudentsCoursesMap(List<Student> students,
      List<StudentsCourses> studentCourses) {
    Map<String, StudentsCourses> studentsCoursesList = new HashMap<>();
    students.forEach(stu -> {
      studentCourses.forEach(stc -> {
        if (stu.getStudentId().equals(stc.getStudentId())) {
          studentsCoursesList.put(stu.getFullName(), stc);
        }
      });
    });

    return studentsCoursesList.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.comparing(StudentsCourses::getCourseId)))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
            LinkedHashMap::new));
  }


  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      List<StudentsCourses> convertStudentCourses = studentCourses.stream()
          .filter(studentCourse -> student.getStudentId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentsCourses(convertStudentCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  public StudentsCourses getConvertNewCourse(StudentDetail studentDetail, Student student) {
    StudentsCourses newCourse = new StudentsCourses();
    newCourse.setCourseId(String.valueOf(studentDetail.getCourseNum()));
    newCourse.setStudentId(student.getStudentId());
    newCourse.setCourseName(getCourseNameById(studentDetail.getCourseNum()));
    return newCourse;
  }

  public String getCourseNameById(Integer courseNum) {
    return courses.entrySet().stream().filter(course -> course.getKey().equals(courseNum))
        .map(Entry::getValue).findFirst().orElse("該当なし");
  }
}

package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> getStudentList() {
    //　検索処理
    List<Student> allStudents = repository.getStudentList();
    //　絞り込みをする。年齢が30代のみを抽出する.
    //　抽出したリストをコントローラーに返す。
    return allStudents.stream()
        .filter(stu -> 30 <= stu.getAge() && stu.getAge() < 40).toList();
  }

  public List<StudentsCourses> getStudentCourseList() {
    // 絞り込み検索で「Javaコース」のコース情報のみを抽出する。
    // 抽出したリストをコントローラーに返す。
    //courseNameに格納されているコースは今のところWP,FE,AWS,JAVAの4種類だけ
    return repository.getStudentCourseList().stream()//もしものNPE避け
        .filter(course -> "JAVA".equals(course.getCourseName())).toList();
  }

}

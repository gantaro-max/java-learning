package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }


  public List<Student> searchStudentList(){
    //　検索処理
    List<Student> allStudents = repository.getSearch();
    //　絞り込みをする。年齢が30代のみを抽出する.
    //　抽出したリストをコントローラーに返す。
    return allStudents.stream()
        .filter(stu -> stu.getAge()>=30).toList();
  }


  public List<StudentsCourses> searchAllCourses(){
    // 絞り込み検索で「Javaコース」のコース情報のみを抽出する。
    // 抽出したリストをコントローラーに返す。
    return repository.getAllCourses().stream()
        .filter(course -> "JAVA".equals(course.getCourseName())).toList();
  }

}

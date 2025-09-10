package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = FALSE")
  List<Student> getStudentList();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> getStudentCourseList();


}

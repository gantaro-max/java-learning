package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
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

  @Insert("INSERT INTO students(student_id,full_name,kana_name,nick_name,email,address"
      + ",age,gender,remark) VALUES(#{studentId},#{fullName},#{kanaName}"
      + ",#{nickName},#{email},#{address},#{age},#{gender},#{remark})")
  void setStudentData(Student student);

  @Insert("INSERT INTO students_courses(course_id,student_id,course_name) VALUES(#{courseId}"
      + ",#{studentId},#{courseName})")
  void setNewCourse(StudentsCourses newCourse);

}

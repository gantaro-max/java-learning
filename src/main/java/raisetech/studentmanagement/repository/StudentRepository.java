package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = FALSE")
  List<Student> getStudentList();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> getStudentCourseList();

  @Select("SELECT * FROM students WHERE is_deleted = FALSE AND student_id = #{studentId}")
  Student getStudentData(String studentId);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> getStudentCourses(String studentId);

  @Insert("INSERT INTO students(student_id,full_name,kana_name,nick_name,email,address"
      + ",age,gender,remark) VALUES(#{studentId},#{fullName},#{kanaName}"
      + ",#{nickName},#{email},#{address},#{age},#{gender},#{remark})")
  void setStudentData(Student student);

  @Insert("INSERT INTO students_courses(course_id,student_id,course_name) VALUES(#{courseId}"
      + ",#{studentId},#{courseName})")
  void setNewCourse(StudentsCourses newCourse);

  @Update(
      "UPDATE students SET nick_name = #{nickName},email = #{email},address = #{address}"
          + ",age = #{age},gender = #{gender},remark = #{remark},is_deleted = #{deleted}"
          + " WHERE student_id = #{studentId}")
  void updateStudent(Student student);

}

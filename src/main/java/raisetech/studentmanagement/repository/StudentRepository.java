package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> getStudentList();

  /**
   * 受講生コース情報の全件検索を行います。
   *
   * @return 受講生コース情報（全件）
   */
  List<StudentsCourses> getStudentCourseList();

  /**
   * 受講生の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  Student getStudentById(String studentId);

  /**
   * 受講生IDに紐づく受講生コース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> getStudentCourse(String studentId);

  @Insert("INSERT INTO students(student_id,full_name,kana_name,nick_name,email,address"
      + ",age,gender,remark) VALUES(#{studentId},#{fullName},#{kanaName}"
      + ",#{nickName},#{email},#{address},#{age},#{gender},#{remark})")
  void setStudentData(Student student);

  @Insert(
      "INSERT INTO students_courses(course_id,student_id,course_name,start_date) VALUES(#{courseId}"
          + ",#{studentId},#{courseName},#{startDate})")
  void setNewCourse(StudentsCourses newCourse);

  @Update(
      "UPDATE students SET nick_name = #{nickName},email = #{email},address = #{address}"
          + ",age = #{age},gender = #{gender},remark = #{remark},is_deleted = #{deleted}"
          + " WHERE student_id = #{studentId}")
  void updateStudent(Student student);

}

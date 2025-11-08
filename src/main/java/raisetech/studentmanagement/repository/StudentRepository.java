package raisetech.studentmanagement.repository;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.studentmanagement.data.Apply;
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
   * 受講生コース申込み状況の全件検索を行います。
   *
   * @return 受講生コース申込状況（全件）
   */
  List<Apply> getApplyList();

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
  List<StudentsCourses> getStudentCourse(String studentId);

  /**
   * 受講生の登録を行います。
   *
   * @param student 受講生
   */
  void setStudentData(Student student);

  /**
   * 受講生コース情報の登録を行います。
   *
   * @param newCourse 受講生コース情報
   */
  void setNewCourse(StudentsCourses newCourse);

  /**
   * 受講生コース申込状況の登録を行います。
   *
   * @param newApply 受講生コース申込状況
   */
  void setNewApply(Apply newApply);

  /**
   * 受講生情報の更新処理です。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報の更新処理です。
   *
   * @param studentsCourses 受講生コース情報
   */
  void updateStudentsCourses(StudentsCourses studentsCourses);

  /**
   * 申込状況の更新処理です。
   *
   * @param apply 申込状況
   */
  void updateApply(Apply apply);

  List<Apply> searchApplyByTakeCourseId(String takeCourseId);

  /**
   * 受講生名による受講生の検索です。
   *
   * @param fullName 受講生名
   * @return 受講生一覧
   */
  List<Student> searchStudentsByFullName(String fullName);

  List<Student> searchStudentsByKanaName(String kanaName);

  List<Student> searchStudentsByNickName(String nickName);

  List<Student> searchStudentsByEmail(String email);

  List<Student> searchStudentsByAddress(String address);

  List<Student> searchStudentsByAge(Integer age);

  List<Student> searchStudentsByGender(String gender);

  List<Student> searchStudentsByRemark(String remark);

  List<Student> searchStudentsByDeleted(boolean deleted);

  List<StudentsCourses> searchCoursesByCourseName(String courseName);

  List<Apply> searchApplyByApplyStatus(String applyStatus);

  List<Student> searchStudentsByStudentIdList(@Param("studentIdList") Set<String> studentIdList);

  List<StudentsCourses> searchCoursesByTakeCourseIdList(
      @Param("takeCourseIdList") List<String> takeCourseIdList);

  List<Apply> searchApplyByTakeCourseIdList(
      @Param("takeCourseIdList") List<String> takeCourseIdList);

}

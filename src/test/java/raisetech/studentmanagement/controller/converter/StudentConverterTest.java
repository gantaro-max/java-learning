package raisetech.studentmanagement.controller.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.ResponseStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpdateStudent;

class StudentConverterTest {

  private static final String UUID_REGEXP =
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

  @Test
  void 受講生一覧と受講生コース情報一覧から受講生詳細一覧に変換できること() {
    Student student = new Student();
    student.setStudentId("00000000-0000-0000-0000-000000000000");
    student.setFullName("山田花子");
    student.setKanaName("ヤマダハナコ");
    student.setNickName("ハナコ");
    student.setEmail("yamahana@example.com");
    student.setAddress("東京都杉並区");
    student.setAge(22);
    student.setGender("女");
    student.setRemark("なし");
    student.setDeleted(false);
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setCourseId("4001");
    studentsCourses.setStudentId(student.getStudentId());
    studentsCourses.setCourseName("JAVA");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 10, 10, 10));
    List<Student> studentList = new ArrayList<>();
    studentList.add(student);
    List<StudentsCourses> studentsCoursesList = new ArrayList<>();
    studentsCoursesList.add(studentsCourses);
    ResponseStudent responseStudent = new ResponseStudent();
    responseStudent.setStudentId(student.getStudentId());
    responseStudent.setFullName(student.getFullName());
    responseStudent.setKanaName(student.getKanaName());
    responseStudent.setNickName(student.getNickName());
    responseStudent.setEmail(student.getEmail());
    responseStudent.setAddress(student.getAddress());
    responseStudent.setAge(student.getAge());
    responseStudent.setGender(student.getGender());
    responseStudent.setRemark(student.getRemark());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setResponseStudent(responseStudent);
    studentDetail.setStudentsCourses(studentsCoursesList);

    StudentConverter converter = new StudentConverter();

    List<StudentDetail> resultDetailList = converter.convertStudentDetailList(studentList,
        studentsCoursesList);

    assertThat(resultDetailList.getFirst()).isEqualTo(studentDetail);
  }

  @Test
  void コースIDからコース名を取得できること() {
    String testCourseId = "1001";
    String testCourseName = "AWS";
    StudentConverter converter = new StudentConverter();
    String resultCourseName = converter.getCourseNameById(testCourseId);

    assertThat(resultCourseName).isEqualTo(testCourseName);

  }

  @Test
  void コースIDが存在しない場合該当なしと返ってくること() {
    String testCourseId = "9999";
    String testMsg = "該当なし";
    StudentConverter converter = new StudentConverter();
    String resultMsg = converter.getCourseNameById(testCourseId);

    assertThat(resultMsg).isEqualTo(testMsg);

  }

  @Test
  void 受講生登録情報と受講生情報から受講生コース情報をつくれること() {
    RegisterStudent registerStudent = new RegisterStudent();
    registerStudent.setFullName("山田花子");
    registerStudent.setKanaName("ヤマダハナコ");
    registerStudent.setNickName("ハナコ");
    registerStudent.setEmail("yamahana@example.com");
    registerStudent.setAddress("東京都杉並区");
    registerStudent.setAge(22);
    registerStudent.setGender("女");
    registerStudent.setRemark("なし");
    registerStudent.setCourseId("4001");
    Student student = new Student();
    student.setStudentId("00000000-0000-0000-0000-000000000000");

    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setCourseId("4001");
    studentsCourses.setStudentId(student.getStudentId());
    studentsCourses.setCourseName("JAVA");
    studentsCourses.setStartDate(LocalDateTime.now());

    StudentConverter converter = new StudentConverter();
    StudentsCourses resultStudentCourse = converter.convertStudentCourse(registerStudent, student);

    assertThat(resultStudentCourse.getCourseId()).isEqualTo(studentsCourses.getCourseId());
    assertThat(resultStudentCourse.getStudentId()).isEqualTo(studentsCourses.getStudentId());
    assertThat(resultStudentCourse.getStartDate().getYear()).isEqualTo(
        studentsCourses.getStartDate().getYear());
    assertThat(resultStudentCourse.getStartDate().getMonth()).isEqualTo(
        studentsCourses.getStartDate().getMonth());
    assertThat(resultStudentCourse.getStartDate().getDayOfMonth()).isEqualTo(
        studentsCourses.getStartDate().getDayOfMonth());
    assertThat(resultStudentCourse.getStartDate().getHour()).isEqualTo(
        studentsCourses.getStartDate().getHour());
    assertThat(resultStudentCourse.getStartDate().getMinute()).isEqualTo(
        studentsCourses.getStartDate().getMinute());

  }

  @Test
  void 受講生登録情報から受講生情報に変換できること() {
    RegisterStudent registerStudent = new RegisterStudent();
    registerStudent.setFullName("山田花子");
    registerStudent.setKanaName("ヤマダハナコ");
    registerStudent.setNickName("ハナコ");
    registerStudent.setEmail("yamahana@example.com");
    registerStudent.setAddress("東京都杉並区");
    registerStudent.setAge(22);
    registerStudent.setGender("女");
    registerStudent.setRemark("なし");
    registerStudent.setCourseId("4001");

    String studentId = "00000000-0000-0000-0000-000000000000";

    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName(registerStudent.getFullName());
    student.setKanaName(registerStudent.getKanaName());
    student.setNickName(registerStudent.getNickName());
    student.setEmail(registerStudent.getEmail());
    student.setAddress(registerStudent.getAddress());
    student.setAge(registerStudent.getAge());
    student.setGender(registerStudent.getGender());
    student.setRemark(registerStudent.getRemark());
    student.setDeleted(false);

    StudentConverter converter = new StudentConverter();

    Student resultStudent = converter.convertRegisterToStudent(registerStudent);

    assertThat(resultStudent.getStudentId()).isNotNull();
    assertThat(resultStudent.getStudentId()).isNotEmpty();
    assertThat(resultStudent.getStudentId()).matches(UUID_REGEXP);
    assertThat(resultStudent.getFullName()).isEqualTo(student.getFullName());
    assertThat(resultStudent.getKanaName()).isEqualTo(student.getKanaName());
    assertThat(resultStudent.getNickName()).isEqualTo(student.getNickName());
    assertThat(resultStudent.getEmail()).isEqualTo(student.getEmail());
    assertThat(resultStudent.getAddress()).isEqualTo(student.getAddress());
    assertThat(resultStudent.getAge()).isEqualTo(student.getAge());
    assertThat(resultStudent.getGender()).isEqualTo(student.getGender());
    assertThat(resultStudent.getRemark()).isEqualTo(student.getRemark());
    assertThat(resultStudent.isDeleted()).isEqualTo(student.isDeleted());
  }

  @Test
  void 受講生更新情報から受講生情報に変換できること() {
    UpdateStudent updateStudent = new UpdateStudent();
    updateStudent.setFullName("山田花子");
    updateStudent.setKanaName("ヤマダハナコ");
    updateStudent.setNickName("ハナコ");
    updateStudent.setEmail("yamahana@example.com");
    updateStudent.setAddress("東京都杉並区");
    updateStudent.setAge(22);
    updateStudent.setGender("女");
    updateStudent.setRemark("なし");
    updateStudent.setDeleted(false);

    String studentId = "00000000-0000-0000-0000-000000000000";

    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName(updateStudent.getFullName());
    student.setKanaName(updateStudent.getKanaName());
    student.setNickName(updateStudent.getNickName());
    student.setEmail(updateStudent.getEmail());
    student.setAddress(updateStudent.getAddress());
    student.setAge(updateStudent.getAge());
    student.setGender(updateStudent.getGender());
    student.setRemark(updateStudent.getRemark());
    student.setDeleted(updateStudent.isDeleted());

    StudentConverter converter = new StudentConverter();

    Student resultStudent = converter.convertUpdateToStudent(updateStudent, student);

    assertThat(resultStudent).isEqualTo(student);

  }

  @Test
  void 受講生情報からレスポンス用受講生情報に変換できること() {
    Student student = new Student();
    student.setStudentId("00000000-0000-0000-0000-000000000000");
    student.setFullName("山田花子");
    student.setKanaName("ヤマダハナコ");
    student.setNickName("ハナコ");
    student.setEmail("yamahana@example.com");
    student.setAddress("東京都杉並区");
    student.setAge(22);
    student.setGender("女");
    student.setRemark("なし");
    student.setDeleted(false);

    ResponseStudent responseStudent = new ResponseStudent();
    responseStudent.setStudentId(student.getStudentId());
    responseStudent.setFullName(student.getFullName());
    responseStudent.setKanaName(student.getKanaName());
    responseStudent.setNickName(student.getNickName());
    responseStudent.setEmail(student.getEmail());
    responseStudent.setAddress(student.getAddress());
    responseStudent.setAge(student.getAge());
    responseStudent.setGender(student.getGender());
    responseStudent.setRemark(student.getRemark());

    StudentConverter converter = new StudentConverter();

    ResponseStudent resultResponse = converter.convertStudentToResponse(student);

    assertThat(resultResponse).isEqualTo(responseStudent);

  }


}

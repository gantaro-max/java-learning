package raisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行われること() {
    String studentId = "11111111-1111-1111-1111-111111111111";
    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickName("ドカベン");
    student.setEmail("yamada@example.com");
    student.setAddress("神奈川県横浜市");
    student.setAge(49);
    student.setGender("男");
    student.setRemark("受け放題");
    student.setDeleted(false);
    List<Student> actual = sut.getStudentList();

    Optional<Student> result = actual.stream().filter(ob -> studentId.equals(ob.getStudentId()))
        .findFirst();

    assertThat(actual.size()).isEqualTo(4);
    assertThat(result).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生の登録が行われること() {
    String studentId = "55555555-5555-5555-5555-555555555555";
    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName("上田俊雄");
    student.setKanaName("ウエダトシオ");
    student.setNickName("とっしー");
    student.setEmail("ueda@example.com");
    student.setAddress("高知県南国市");
    student.setAge(23);
    student.setGender("男");
    student.setRemark("受け放題");
    student.setDeleted(false);

    sut.setStudentData(student);

    Student actual = sut.getStudentById(studentId);

    assertThat(actual).usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生コース情報全件検索が行えること() {
    String studentId = "22222222-2222-2222-2222-222222222222";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId("22222222-3333-4444-5555-666666666666");
    studentsCourses.setCourseId("2001");
    studentsCourses.setStudentId(studentId);
    studentsCourses.setCourseName("WP");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 15, 12, 0, 0));
    studentsCourses.setCompleteDate(null);
    List<StudentsCourses> actual = sut.getStudentCourseList();
    Optional<StudentsCourses> result = actual.stream().filter(
            ob -> studentId.equals(ob.getStudentId()) && studentsCourses.getCourseId()
                .equals(ob.getCourseId()) && studentsCourses.getStartDate().equals(ob.getStartDate()))
        .findFirst();
    assertThat(actual.size()).isEqualTo(6);
    assertThat(result).isPresent().get().usingRecursiveComparison().isEqualTo(studentsCourses);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    String studentId = "22222222-2222-2222-2222-222222222222";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId("77777777-8888-9999-1111-222222222222");
    studentsCourses.setCourseId("5001");
    studentsCourses.setStudentId(studentId);
    studentsCourses.setCourseName("DE");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 20, 15, 0, 0));
    studentsCourses.setCompleteDate(null);

    sut.setNewCourse(studentsCourses);

    List<StudentsCourses> actual = sut.getStudentCourse(studentId);
    Optional<StudentsCourses> result = actual.stream()
        .filter(ob -> studentsCourses.getCourseId().equals(ob.getCourseId())
            && studentsCourses.getStartDate().equals(ob.getStartDate())).findFirst();

    assertThat(result).isPresent().get().usingRecursiveComparison().isEqualTo(studentsCourses);

  }

  @Test
  void 受講生IDによる受講生検索ができること() {
    String studentId = "11111111-1111-1111-1111-111111111111";
    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickName("ドカベン");
    student.setEmail("yamada@example.com");
    student.setAddress("神奈川県横浜市");
    student.setAge(49);
    student.setGender("男");
    student.setRemark("受け放題");
    student.setDeleted(false);

    Student actual = sut.getStudentById(studentId);

    assertThat(actual).usingRecursiveComparison().isEqualTo(student);

  }

  @Test
  void 無効な受講生IDで検索すると受講生がNullで返ること() {
    String studentId = "00000000-0000-0000-0000-000000000000";
    Student actual = sut.getStudentById(studentId);

    assertThat(actual).isNull();
  }

  @Test
  void 受講生IDによる受講生コース情報が検索できること() {
    String studentId = "22222222-2222-2222-2222-222222222222";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId("22222222-3333-4444-5555-666666666666");
    studentsCourses.setCourseId("2001");
    studentsCourses.setStudentId(studentId);
    studentsCourses.setCourseName("WP");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 15, 12, 0, 0));
    studentsCourses.setCompleteDate(null);

    List<StudentsCourses> actual = sut.getStudentCourse(studentId);
    Optional<StudentsCourses> result = actual.stream().filter(
            ob -> studentId.equals(ob.getStudentId()) && studentsCourses.getCourseId()
                .equals(ob.getCourseId()) && studentsCourses.getStartDate().equals(ob.getStartDate()))
        .findFirst();

    assertThat(result).isPresent().get().usingRecursiveComparison().isEqualTo(studentsCourses);
  }

  @Test
  void 無効な受講生IDで検索すると受講生コースリストが空で返ること() {
    String studentId = "00000000-0000-0000-0000-000000000000";
    List<StudentsCourses> actual = sut.getStudentCourse(studentId);

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生情報の更新ができること() {
    String studentId = "11111111-1111-1111-1111-111111111111";
    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickName("ガリベン");
    student.setEmail("yamada@example.com");
    student.setAddress("神奈川県横浜市");
    student.setAge(33);
    student.setGender("男");
    student.setRemark("特になし");
    student.setDeleted(false);

    sut.updateStudent(student);
    Student actual = sut.getStudentById(studentId);

    assertThat(actual).usingRecursiveComparison().isEqualTo(student);

  }

}

package raisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.studentmanagement.data.Apply;
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

  @Test
  void 受講生コース情報の更新ができること() {
    String studentId = "22222222-2222-2222-2222-222222222222";
    String testTakeCourseId = "22222222-3333-4444-5555-666666666666";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId(testTakeCourseId);
    studentsCourses.setCourseId("5001");
    studentsCourses.setStudentId(studentId);
    studentsCourses.setCourseName("DE");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 15, 12, 0, 0));
    studentsCourses.setCompleteDate(LocalDateTime.of(2025, 11, 4, 12, 0, 0));

    sut.updateStudentsCourses(studentsCourses);
    List<StudentsCourses> actualStudentsCourses = sut.getStudentCourse(studentId);

    Optional<StudentsCourses> actual = actualStudentsCourses.stream()
        .filter(sc -> sc.getTakeCourseId().equals(testTakeCourseId)).findFirst();

    assertThat(actual).isPresent().get().usingRecursiveComparison().isEqualTo(studentsCourses);
  }

  @Test
  void 受講生コース申込状況の全件検索できること() {
    String testApplyId = "11111111-9999-2222-8888-333333333333";
    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId("11111111-2222-3333-4444-555555555555");
    apply.setApplyStatus("受講終了");

    List<Apply> allApplyList = sut.getApplyList();
    Optional<Apply> actualApply = allApplyList.stream()
        .filter(ap -> ap.getApplyId().equals(apply.getApplyId())).findFirst();

    assertThat(allApplyList.size()).isEqualTo(6);
    assertThat(actualApply).isPresent().get().usingRecursiveComparison().isEqualTo(apply);

  }

  @Test
  void 受講生コース申込み状況を登録できること() {
    String studentId = "22222222-2222-2222-2222-222222222222";
    String testTakeCourseId = "77777777-8888-9999-1111-222222222222";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId(testTakeCourseId);
    studentsCourses.setCourseId("5001");
    studentsCourses.setStudentId(studentId);
    studentsCourses.setCourseName("DE");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 20, 15, 0, 0));
    studentsCourses.setCompleteDate(null);

    String testApplyId = "77777777-3333-8888-2222-999999999999";
    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId(testTakeCourseId);
    apply.setApplyStatus("仮申込");

    sut.setNewCourse(studentsCourses);
    sut.setNewApply(apply);

    List<Apply> applyList = sut.getApplyList();

    Optional<Apply> actualApply = applyList.stream()
        .filter(ap -> ap.getApplyId().equals(apply.getApplyId())).findFirst();

    assertThat(applyList.size()).isEqualTo(7);
    assertThat(actualApply).isPresent().get().usingRecursiveComparison().isEqualTo(apply);

  }

  @Test
  void 受講生コース申込状況を更新できること() {
    String testApplyId = "22222222-8888-3333-7777-444444444444";
    String testTakeCourseId = "22222222-3333-4444-5555-666666666666";
    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId(testTakeCourseId);
    apply.setApplyStatus("受講中");

    sut.updateApply(apply);

    List<Apply> applyList = sut.getApplyList();

    Optional<Apply> actualApply = applyList.stream()
        .filter(ap -> ap.getApplyId().equals(apply.getApplyId())).findFirst();

    assertThat(actualApply).isPresent().get().usingRecursiveComparison().isEqualTo(apply);

  }

  @Test
  void 受講生名による受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByFullName("山田太郎");

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生カナ名による受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByKanaName("ヤマダタロ");

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生ニックネームによる受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByNickName("ドカベ");

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生メールアドレスによる受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByEmail("yamada@ex");

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生の地域による受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByAddress("横浜");

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生の年齢による受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByAge(49);

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生の性別による受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByGender("男");

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生の備考による受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByRemark("受け放題");

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生の削除フラグによる受講生検索ができること() {
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

    List<Student> actualStudentList = sut.searchStudentsByDeleted(false);

    Optional<Student> actualStudent = actualStudentList.stream()
        .filter(stu -> studentId.equals(stu.getStudentId())).findFirst();

    assertThat(actualStudent).isPresent().get().usingRecursiveComparison().isEqualTo(student);
  }

  @Test
  void 受講生コース名による受講生コース一覧検索ができること() {
    String studentId = "22222222-2222-2222-2222-222222222222";
    String testTakeCourseId = "22222222-3333-4444-5555-666666666666";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId(testTakeCourseId);
    studentsCourses.setCourseId("2001");
    studentsCourses.setStudentId(studentId);
    studentsCourses.setCourseName("WP");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 15, 12, 0, 0));
    studentsCourses.setCompleteDate(null);

    List<StudentsCourses> actualStudentsCoursesList = sut.searchCoursesByCourseName("WP");

    Optional<StudentsCourses> actualStudentsCourses = actualStudentsCoursesList.stream()
        .filter(sc -> testTakeCourseId.equals(sc.getTakeCourseId())).findFirst();

    assertThat(actualStudentsCourses).isPresent().get().usingRecursiveComparison()
        .isEqualTo(studentsCourses);
  }

  @Test
  void 受講生申込みの現状による受講生申込状況一覧検索ができること() {
    String testApplyId = "22222222-8888-3333-7777-444444444444";
    String testTakeCourseId = "22222222-3333-4444-5555-666666666666";
    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId(testTakeCourseId);
    apply.setApplyStatus("本申込");

    List<Apply> actualApplyList = sut.searchApplyByApplyStatus("本申込");

    Optional<Apply> actualApply = actualApplyList.stream()
        .filter(app -> testApplyId.equals(app.getApplyId())).findFirst();

    assertThat(actualApply).isPresent().get().usingRecursiveComparison()
        .isEqualTo(apply);
  }

  @Test
  void 受講生IDリストによる受講生一覧検索ができること() {
    String studentId1 = "11111111-1111-1111-1111-111111111111";
    Student student1 = new Student();
    student1.setStudentId(studentId1);
    student1.setFullName("山田太郎");
    student1.setKanaName("ヤマダタロウ");
    student1.setNickName("ドカベン");
    student1.setEmail("yamada@example.com");
    student1.setAddress("神奈川県横浜市");
    student1.setAge(49);
    student1.setGender("男");
    student1.setRemark("受け放題");
    student1.setDeleted(false);

    String studentId2 = "22222222-2222-2222-2222-222222222222";
    Student student2 = new Student();
    student2.setStudentId(studentId2);
    student2.setFullName("鈴木花子");
    student2.setKanaName("スズキハナコ");
    student2.setNickName("ハナコ");
    student2.setEmail("suzuki@example.com");
    student2.setAddress("東京都世田谷区");
    student2.setAge(20);
    student2.setGender("女");
    student2.setRemark("特になし");
    student2.setDeleted(false);

    List<Student> studentList = new ArrayList<>(List.of(student1, student2));

    List<Student> actualStudentList = sut.searchStudentsByStudentIdList(
        Set.of(studentId1, studentId2));

    assertThat(actualStudentList).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(studentList);

  }

  @Test
  void 受講生コースIDリストによる受講生コース一覧検索ができること() {
    String testTakeCourseId1 = "11111111-2222-3333-4444-555555555555";
    StudentsCourses studentsCourses1 = new StudentsCourses();
    studentsCourses1.setTakeCourseId(testTakeCourseId1);
    studentsCourses1.setCourseId("1001");
    studentsCourses1.setStudentId("11111111-1111-1111-1111-111111111111");
    studentsCourses1.setCourseName("AWS");
    studentsCourses1.setStartDate(LocalDateTime.of(2024, 10, 14, 11, 0, 0));
    studentsCourses1.setCompleteDate(null);

    String testTakeCourseId2 = "22222222-3333-4444-5555-666666666666";
    StudentsCourses studentsCourses2 = new StudentsCourses();
    studentsCourses2.setTakeCourseId(testTakeCourseId2);
    studentsCourses2.setCourseId("2001");
    studentsCourses2.setStudentId("22222222-2222-2222-2222-222222222222");
    studentsCourses2.setCourseName("WP");
    studentsCourses2.setStartDate(LocalDateTime.of(2025, 10, 15, 12, 0, 0));
    studentsCourses2.setCompleteDate(null);

    List<StudentsCourses> studentsCoursesList = new ArrayList<>(
        List.of(studentsCourses1, studentsCourses2));

    List<StudentsCourses> actualStudentsCoursesList = sut.searchCoursesByTakeCourseIdList(
        List.of(testTakeCourseId1, testTakeCourseId2));

    assertThat(actualStudentsCoursesList).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(studentsCoursesList);
  }

  @Test
  void 受講生IDリストによる受講生コース一覧検索ができること() {
    String studentId1 = "44444444-4444-4444-4444-444444444444";
    StudentsCourses studentsCourses1 = new StudentsCourses();
    studentsCourses1.setTakeCourseId("44444444-5555-6666-7777-888888888888");
    studentsCourses1.setCourseId("4001");
    studentsCourses1.setStudentId(studentId1);
    studentsCourses1.setCourseName("JAVA");
    studentsCourses1.setStartDate(LocalDateTime.of(2025, 10, 17, 14, 0, 0));
    studentsCourses1.setCompleteDate(null);

    String studentId2 = "22222222-2222-2222-2222-222222222222";
    StudentsCourses studentsCourses2 = new StudentsCourses();
    studentsCourses2.setTakeCourseId("22222222-3333-4444-5555-666666666666");
    studentsCourses2.setCourseId("2001");
    studentsCourses2.setStudentId(studentId2);
    studentsCourses2.setCourseName("WP");
    studentsCourses2.setStartDate(LocalDateTime.of(2025, 10, 15, 12, 0, 0));
    studentsCourses2.setCompleteDate(null);

    List<StudentsCourses> studentsCoursesList = new ArrayList<>(
        List.of(studentsCourses1, studentsCourses2));
    Set<String> setStudentList = new HashSet<>(List.of(studentId1, studentId2));

    List<StudentsCourses> actualStudentsCoursesList = sut.searchCoursesByStudentIdList(
        setStudentList);

    assertThat(actualStudentsCoursesList).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(studentsCoursesList);
  }

  @Test
  void 受講生コースIDリストよる受講生申込状況一覧検索ができること() {
    String testApplyId1 = "22222222-8888-3333-7777-444444444444";
    String testTakeCourseId1 = "22222222-3333-4444-5555-666666666666";
    Apply apply1 = new Apply();
    apply1.setApplyId(testApplyId1);
    apply1.setTakeCourseId(testTakeCourseId1);
    apply1.setApplyStatus("本申込");

    String testApplyId2 = "11111111-9999-2222-8888-333333333333";
    String testTakeCourseId2 = "11111111-2222-3333-4444-555555555555";
    Apply apply2 = new Apply();
    apply2.setApplyId(testApplyId2);
    apply2.setTakeCourseId(testTakeCourseId2);
    apply2.setApplyStatus("受講終了");

    List<Apply> applyList = new ArrayList<>(List.of(apply1, apply2));

    List<Apply> actualApplyList = sut.searchApplyByTakeCourseIdList(
        List.of(testTakeCourseId1, testTakeCourseId2));

    assertThat(actualApplyList).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(applyList);
  }


}

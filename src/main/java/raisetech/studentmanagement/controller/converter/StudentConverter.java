package raisetech.studentmanagement.controller.converter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.Apply;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.ResponseStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpCourseApply;
import raisetech.studentmanagement.domain.UpdateStudent;
import raisetech.studentmanagement.exception.ResourceNotFoundException;

/**
 * 受講生詳細を受講性や受講生コース情報、もしくはその逆に変換するコンバーターです。
 */
@Component
public class StudentConverter {

  private final Map<String, String> courses = Map.of(
      "1001", "AWS", "2001", "WP", "3001", "FE",
      "4001", "JAVA", "5001", "DE", "6001", "WM");

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   *
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetailList(List<Student> students,
      List<StudentsCourses> studentCourses, List<Apply> applyList) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    Map<String, List<StudentsCourses>> mapStudentsCourses = studentCourses.stream()
        .collect(Collectors.groupingBy(StudentsCourses::getStudentId));

    Map<String, Apply> mapApplyList = applyList.stream()
        .collect(Collectors.toMap(Apply::getTakeCourseId, ap -> ap));

    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      ResponseStudent responseStudent = convertStudentToResponse(student);
      studentDetail.setResponseStudent(responseStudent);

      List<StudentsCourses> convertStudentCourses = mapStudentsCourses.getOrDefault(
          student.getStudentId(), new ArrayList<>());
      List<Apply> studentApply = convertStudentCourses.stream()
          .map(sc -> mapApplyList.get(sc.getTakeCourseId())).filter(Objects::nonNull).toList();

      studentDetail.setStudentsCourses(convertStudentCourses);
      studentDetail.setApplyList(studentApply);
      studentDetails.add(studentDetail);
    });
    return studentDetails.stream()
        .sorted(Comparator.comparing(std -> std.getResponseStudent().getStudentId())).toList();
  }

  /**
   * フィールドcoursesからコースIDに紐づくコース名を取得
   *
   * @param courseId コースID
   * @return コース名
   */
  public String getCourseNameById(String courseId) {
    return courses.entrySet().stream().filter(course -> course.getKey().equals(courseId))
        .map(Entry::getValue).findFirst().orElse("該当なし");
  }

  /**
   * 登録情報のcourseIdと受講生情報のstudentIdから受講生コース情報を作る
   *
   * @param registerStudent 登録情報
   * @param student         受講生情報
   * @return 受講生コース情報
   */
  public StudentsCourses convertStudentCourse(RegisterStudent registerStudent, Student student) {
    StudentsCourses studentsCourses = new StudentsCourses();

    studentsCourses.setTakeCourseId(UUID.randomUUID().toString());
    studentsCourses.setCourseId(registerStudent.getCourseId());
    studentsCourses.setStudentId(student.getStudentId());
    studentsCourses.setCourseName(getCourseNameById(registerStudent.getCourseId()));
    studentsCourses.setStartDate(LocalDateTime.now());

    return studentsCourses;
  }

  /**
   * 受講生コース情報から新しい申し込み状況を作成
   *
   * @param studentsCourses 受講生コース情報
   * @return 申込み状況
   */
  public Apply convertApply(StudentsCourses studentsCourses) {
    Apply apply = new Apply();

    apply.setApplyId(UUID.randomUUID().toString());
    apply.setTakeCourseId(studentsCourses.getTakeCourseId());
    apply.setApplyStatus("仮申込");

    return apply;
  }

  /**
   * 登録用受講生情報から受講生情報に変換処理
   *
   * @param registerStudent 登録用受講生情報
   * @return 受講生情報
   */
  public Student convertRegisterToStudent(RegisterStudent registerStudent) {
    return new Student(UUID.randomUUID().toString(), registerStudent.getFullName(),
        registerStudent.getKanaName(), registerStudent.getNickName(), registerStudent.getEmail(),
        registerStudent.getAddress(), registerStudent.getAge(), registerStudent.getGender(),
        registerStudent.getRemark(), false);
  }

  /**
   * 更新用受講生情報と既存の受講生情報から更新後の受講生情報を作成
   *
   * @param updateStudent 更新用受講生情報
   * @param student       既存の受講生情報
   * @return 更新後の受講生情報
   */
  public Student convertUpdateToStudent(UpdateStudent updateStudent, Student student) {
    student.setFullName(updateStudent.getFullName());
    student.setKanaName(updateStudent.getKanaName());
    student.setNickName(updateStudent.getNickName());
    student.setEmail(updateStudent.getEmail());
    student.setAddress(updateStudent.getAddress());
    student.setAge(updateStudent.getAge());
    student.setGender(updateStudent.getGender());
    student.setRemark(updateStudent.getRemark());
    student.setDeleted(updateStudent.isDeleted());

    return student;
  }

  /**
   * 更新用受講生コース申込状況と既存の受講生コース情報から更新用の受講生コース情報を作成
   *
   * @param upCourseApplyList 更新用受講生コース情報申込状況
   * @param studentsCourses   既存の受講生コース情報
   * @return 更新用の受講生コース情報
   */
  public List<StudentsCourses> convertUpdateToCourses(List<UpCourseApply> upCourseApplyList,
      List<StudentsCourses> studentsCourses) {

    Map<String, StudentsCourses> mapStudentsCourses = studentsCourses.stream()
        .collect(Collectors.toMap(StudentsCourses::getTakeCourseId, course -> course));

    upCourseApplyList.forEach(uca -> {
      StudentsCourses targetStudentsCourses = mapStudentsCourses.get(uca.getTakeCourseId());

      if (targetStudentsCourses != null) {
        targetStudentsCourses.setCourseId(uca.getCourseId());
        targetStudentsCourses.setCourseName(getCourseNameById(uca.getCourseId()));
        if (targetStudentsCourses.getCompleteDate() == null && uca.getApplyStatus()
            .equals("受講終了")) {
          targetStudentsCourses.setCompleteDate(LocalDateTime.now());
        }
      } else {
        throw new ResourceNotFoundException("不正な受講コースIDが含まれています");
      }

    });

    return studentsCourses;
  }

  /**
   * 更新用受講生コース情報申込状況と既存の申込状況から更新用の申込状況を作成
   *
   * @param upCourseApplyList 更新用受講生コース情報申込状況
   * @param applyList         既存の申込み状況
   * @return 更新用の申込状況
   */
  public List<Apply> convertUpdateToApply(List<UpCourseApply> upCourseApplyList,
      List<Apply> applyList) {

    Map<String, String> mapAppLyIdStatus = upCourseApplyList.stream()
        .collect(Collectors.toMap(UpCourseApply::getApplyId, UpCourseApply::getApplyStatus));

    applyList.forEach(apply -> {
      String upStatus = mapAppLyIdStatus.get(apply.getApplyId());
      if (upStatus != null) {
        apply.setApplyStatus(upStatus);
      }
    });

    return applyList;
  }

  /**
   * 受講生情報からResponse用受講生情報に変換し取得
   *
   * @param student 受講生情報
   * @return Response用受講生情報
   */
  public ResponseStudent convertStudentToResponse(Student student) {
    return new ResponseStudent(student.getStudentId(), student.getFullName(), student.getKanaName(),
        student.getNickName(), student.getEmail(), student.getAddress(), student.getAge(),
        student.getGender(), student.getRemark());
  }


}

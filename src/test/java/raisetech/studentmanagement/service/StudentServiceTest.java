package raisetech.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Apply;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.ResponseStudent;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.UpCourseApply;
import raisetech.studentmanagement.domain.UpdateDetail;
import raisetech.studentmanagement.domain.UpdateStudent;
import raisetech.studentmanagement.exception.ResourceNotFoundException;
import raisetech.studentmanagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;
  @Mock
  private StudentConverter converter;
  private StudentService sut;

  @Captor
  private ArgumentCaptor<Student> captorStudent;
  @Captor
  private ArgumentCaptor<StudentsCourses> captorStudentCourse;
  @Captor
  private ArgumentCaptor<Apply> captorApply;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void getStudentAllDataShouldSucceed() {
    List<Student> studentList = new ArrayList<>();
    List<StudentsCourses> studentsCourses = new ArrayList<>();
    List<Apply> applyList = new ArrayList<>();
    List<StudentDetail> checkDetailList = new ArrayList<>();
    when(repository.getStudentList()).thenReturn(studentList);
    when(repository.getStudentCourseList()).thenReturn(studentsCourses);
    when(repository.getApplyList()).thenReturn((applyList));
    when(converter.convertStudentDetailList(studentList, studentsCourses, applyList)).thenReturn(
        checkDetailList);

    List<StudentDetail> studentDetailList = sut.getStudentDetailList();

    verify(repository, times(1)).getStudentList();
    verify(repository, times(1)).getStudentCourseList();
    verify(repository, times(1)).getApplyList();
    verify(converter, times(1)).convertStudentDetailList(studentList, studentsCourses, applyList);

    assertThat(studentDetailList).isEqualTo(checkDetailList);
  }

  @Test
  void getStudentByIdShouldSucceed() {
    String studentId = "00000000-0000-0000-0000-000000000000";
    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickName("ドカベン");
    student.setEmail("yamada@example.com");
    student.setAddress("神奈川県横浜市");
    student.setAge(20);
    student.setGender("男");
    student.setRemark("受け放題");
    student.setDeleted(false);

    String testTakeCourseId = "77777777-8888-9999-1111-222222222222";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId(testTakeCourseId);
    studentsCourses.setCourseId("4001");
    studentsCourses.setCourseName("JAVA");
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 10, 10, 10));
    List<StudentsCourses> studentsCoursesList = new ArrayList<>(List.of(studentsCourses));

    String testApplyId = "99999999-9999-9999-9999-999999999999";
    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId(testTakeCourseId);
    apply.setApplyStatus("受講中");

    List<Apply> applyList = new ArrayList<>(List.of(apply));

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

    StudentDetail responseDetail = new StudentDetail();
    responseDetail.setResponseStudent(responseStudent);
    responseDetail.setStudentsCourses(studentsCoursesList);
    responseDetail.setApplyList(applyList);

    when(repository.getStudentById(studentId)).thenReturn(student);
    when(repository.getStudentCourse(studentId)).thenReturn(studentsCoursesList);
    when(repository.searchApplyByTakeCourseId(testTakeCourseId)).thenReturn(applyList);
    when(converter.convertStudentToResponse(student)).thenReturn(responseStudent);

    Optional<StudentDetail> opStudentDetail = sut.getStudentDetail(studentId);

    verify(repository, times(1)).getStudentById(studentId);
    verify(repository, times(1)).getStudentCourse(studentId);
    verify(repository, times(1)).searchApplyByTakeCourseId(testTakeCourseId);
    verify(converter, times(1)).convertStudentToResponse(student);

    assertThat(opStudentDetail).isPresent().get().usingRecursiveComparison()
        .isEqualTo(responseDetail);

  }

  @Test
  void createStudentShouldSucceed() {
    RegisterStudent registerStudent = new RegisterStudent();
    registerStudent.setFullName("山田太郎");
    registerStudent.setKanaName("ヤマダタロウ");
    registerStudent.setNickName("ドカベン");
    registerStudent.setEmail("yamada@example.com");
    registerStudent.setAddress("神奈川県横浜市");
    registerStudent.setAge(20);
    registerStudent.setGender("男");
    registerStudent.setRemark("受け放題");
    registerStudent.setCourseId("4001");

    Student student = new Student();
    String studentId = "00000000-0000-0000-0000-000000000000";
    student.setStudentId(studentId);
    student.setFullName(registerStudent.getFullName());
    student.setKanaName(registerStudent.getKanaName());
    student.setNickName(registerStudent.getNickName());
    student.setEmail(registerStudent.getEmail());
    student.setAddress(registerStudent.getAddress());
    student.setAge(registerStudent.getAge());
    student.setGender(registerStudent.getGender());
    student.setRemark(registerStudent.getRemark());

    String testTakeCourseId = "77777777-8888-9999-1111-222222222222";
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId(testTakeCourseId);
    studentsCourses.setCourseId(registerStudent.getCourseId());
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 10, 10, 10));

    String testApplyId = "99999999-9999-9999-9999-999999999999";
    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId(testTakeCourseId);
    apply.setApplyStatus("仮申込");

    ResponseStudent responseStudent = new ResponseStudent();
    List<StudentsCourses> studentsCoursesList = new ArrayList<>(List.of(studentsCourses));
    List<Apply> applyList = new ArrayList<>(List.of(apply));

    when(converter.convertRegisterToStudent(registerStudent)).thenReturn(student);
    when(converter.convertStudentCourse(registerStudent, student)).thenReturn(studentsCourses);
    when(converter.convertApply(studentsCourses)).thenReturn(apply);
    when(converter.convertStudentToResponse(student)).thenReturn(responseStudent);
    when(repository.getStudentCourse(student.getStudentId())).thenReturn(studentsCoursesList);
    when(repository.searchApplyByTakeCourseId(studentsCourses.getTakeCourseId())).thenReturn(
        applyList);

    StudentDetail studentDetail = sut.setStudentNewCourse(registerStudent);

    verify(converter, times(1)).convertRegisterToStudent(registerStudent);
    verify(converter, times(1)).convertStudentCourse(registerStudent, student);
    verify(converter, times(1)).convertApply(studentsCourses);
    verify(converter, times(1)).convertStudentToResponse(student);
    verify(repository, times(1)).setStudentData(captorStudent.capture());
    verify(repository, times(1)).setNewCourse(captorStudentCourse.capture());
    verify(repository, times(1)).setNewApply(captorApply.capture());
    verify(repository, times(1)).getStudentCourse(student.getStudentId());
    verify(repository, times(1)).searchApplyByTakeCourseId(studentsCourses.getTakeCourseId());

    Student capturedStudent = captorStudent.getValue();
    StudentsCourses capturedStudentCourse = captorStudentCourse.getValue();
    Apply capturedApply = captorApply.getValue();

    assertThat(capturedStudent).isEqualTo(student);
    assertThat(capturedStudentCourse).isEqualTo(studentsCourses);
    assertThat(capturedApply).isEqualTo(apply);

    assertThat(studentDetail.getResponseStudent()).isEqualTo(responseStudent);
    assertThat(studentDetail.getStudentsCourses()).isEqualTo(studentsCoursesList);
    assertThat(studentDetail.getApplyList()).isEqualTo(applyList);

  }

  @Test
  void updateStudentShouldSucceed() {
    String studentId = "00000000-0000-0000-0000-000000000000";
    UpdateStudent updateStudent = new UpdateStudent();
    updateStudent.setFullName("山田太郎");
    updateStudent.setKanaName("ヤマダタロウ");
    updateStudent.setNickName("ドカベン");
    updateStudent.setEmail("yamada@example.com");
    updateStudent.setAddress("神奈川県横浜市");
    updateStudent.setAge(20);
    updateStudent.setGender("男");
    updateStudent.setRemark("受け放題");
    updateStudent.setDeleted(false);

    String testTakeCourseId = "77777777-8888-9999-1111-222222222222";
    String testApplyId = "99999999-9999-9999-9999-999999999999";
    UpCourseApply upCourseApply = new UpCourseApply();
    upCourseApply.setTakeCourseId(testTakeCourseId);
    upCourseApply.setCourseId("4001");
    upCourseApply.setApplyId(testApplyId);
    upCourseApply.setApplyStatus("受講中");
    List<UpCourseApply> upCourseApplyList = new ArrayList<>(List.of(upCourseApply));

    UpdateDetail updateDetail = new UpdateDetail(updateStudent, upCourseApplyList);

    Student student = new Student();
    student.setStudentId(studentId);
    Student newStudent = new Student();
    newStudent.setStudentId(studentId);
    newStudent.setFullName(updateStudent.getFullName());
    newStudent.setKanaName(updateStudent.getKanaName());
    newStudent.setNickName(updateStudent.getNickName());
    newStudent.setEmail(updateStudent.getEmail());
    newStudent.setAddress(updateStudent.getAddress());
    newStudent.setAge(updateStudent.getAge());
    newStudent.setGender(updateStudent.getGender());
    newStudent.setRemark(updateStudent.getRemark());
    newStudent.setDeleted(updateStudent.isDeleted());

    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setTakeCourseId(testTakeCourseId);
    studentsCourses.setCourseId(upCourseApply.getCourseId());
    studentsCourses.setStudentId(studentId);
    studentsCourses.setStartDate(LocalDateTime.of(2025, 10, 10, 10, 10));

    Apply apply = new Apply();
    apply.setApplyId(testApplyId);
    apply.setTakeCourseId(testTakeCourseId);
    apply.setApplyStatus(upCourseApply.getApplyStatus());

    ResponseStudent responseStudent = new ResponseStudent();
    List<StudentsCourses> studentsCoursesList = new ArrayList<>(List.of(studentsCourses));
    List<Apply> applyList = new ArrayList<>(List.of(apply));

    when(repository.getStudentById(studentId)).thenReturn(student);
    when(repository.getStudentCourse(newStudent.getStudentId())).thenReturn(studentsCoursesList);
    when(repository.searchApplyByTakeCourseId(studentsCourses.getTakeCourseId())).thenReturn(
        applyList);
    when(converter.convertUpdateToStudent(updateStudent, student)).thenReturn(newStudent);
    when(converter.convertUpdateToCourses(upCourseApplyList, studentsCoursesList)).thenReturn(
        studentsCoursesList);
    when(converter.convertUpdateToApply(upCourseApplyList, applyList)).thenReturn(applyList);
    when(converter.convertStudentToResponse(newStudent)).thenReturn(responseStudent);

    StudentDetail studentDetail = sut.updateStudent(updateDetail, studentId);

    verify(repository, times(1)).getStudentById(studentId);
    verify(repository, times(1)).getStudentCourse(newStudent.getStudentId());
    verify(repository, times(1)).searchApplyByTakeCourseId(studentsCourses.getTakeCourseId());
    verify(converter, times(1)).convertUpdateToStudent(updateStudent, student);
    verify(converter, times(1)).convertUpdateToCourses(upCourseApplyList, studentsCoursesList);
    verify(converter, times(1)).convertUpdateToApply(upCourseApplyList, applyList);
    verify(converter, times(1)).convertStudentToResponse(newStudent);
    verify(repository, times(1)).updateStudent(captorStudent.capture());
    verify(repository, times(1)).updateStudentsCourses(captorStudentCourse.capture());
    verify(repository, times(1)).updateApply(captorApply.capture());

    Student capturedStudent = captorStudent.getValue();
    StudentsCourses capturedStudentsCourses = captorStudentCourse.getValue();
    Apply capturedApply = captorApply.getValue();

    assertThat(capturedStudent).isEqualTo(newStudent);
    assertThat(capturedStudentsCourses).isEqualTo(studentsCourses);
    assertThat(capturedApply).isEqualTo(apply);

    assertThat(studentDetail.getResponseStudent()).isEqualTo(responseStudent);
    assertThat(studentDetail.getStudentsCourses()).isEqualTo(studentsCoursesList);
    assertThat(studentDetail.getApplyList()).isEqualTo(applyList);

  }

  @Test
  void updateNotFoundStudentShouldThrowException() {
    String studentId = "00000000-0000-0000-0000-000000000000";
    UpdateStudent updateStudent = new UpdateStudent();
    UpCourseApply upCourseApply = new UpCourseApply();
    List<UpCourseApply> upCourseApplyList = new ArrayList<>(List.of(upCourseApply));
    UpdateDetail updateDetail = new UpdateDetail(updateStudent, upCourseApplyList);
    when(repository.getStudentById(studentId)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class,
        () -> sut.updateStudent(updateDetail, studentId));
  }

  @Test
  void 受講生名から検索した受講生詳細情報を作成できること() {

  }

}


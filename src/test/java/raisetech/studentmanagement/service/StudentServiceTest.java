package raisetech.studentmanagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentsCourses;
import raisetech.studentmanagement.domain.RegisterStudent;
import raisetech.studentmanagement.domain.ResponseStudent;
import raisetech.studentmanagement.domain.StudentDetail;
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

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_全件検索が動作すること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentsCourses> studentsCourses = new ArrayList<>();
    when(repository.getStudentList()).thenReturn(studentList);
    when(repository.getStudentCourseList()).thenReturn(studentsCourses);

    sut.getStudentDetailList();

    verify(repository, times(1)).getStudentList();
    verify(repository, times(1)).getStudentCourseList();
    verify(converter, times(1)).convertStudentDetailList(studentList, studentsCourses);
  }

  @Test
  void 受講生IDに紐づく受講生を検索処理が動作すること() {
    String studentId = "00000000-0000-0000-0000-000000000000";
    Student student = new Student();
    List<StudentsCourses> studentsCourses = new ArrayList<>();
    ResponseStudent responseStudent = new ResponseStudent();

    when(repository.getStudentById(studentId)).thenReturn(student);
    when(repository.getStudentCourse(studentId)).thenReturn(studentsCourses);
    when(converter.convertStudentToResponse(student)).thenReturn(responseStudent);

    sut.getStudentDetail(studentId);

    verify(repository, times(1)).getStudentById(studentId);
    verify(repository, times(1)).getStudentCourse(studentId);
    verify(converter, times(1)).convertStudentToResponse(student);

  }

  @Test
  void 受講生登録処理が動作すること() {
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
    student.setFullName(registerStudent.getFullName());
    student.setKanaName(registerStudent.getKanaName());
    student.setNickName(registerStudent.getNickName());
    student.setEmail(registerStudent.getEmail());
    student.setAddress(registerStudent.getAddress());
    student.setAge(registerStudent.getAge());
    student.setGender(registerStudent.getGender());
    student.setRemark(registerStudent.getRemark());

    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setCourseId(registerStudent.getCourseId());

    ResponseStudent responseStudent = new ResponseStudent();
    List<StudentsCourses> studentsCoursesList = new ArrayList<>();

    when(converter.convertRegisterToStudent(registerStudent)).thenReturn(student);
    when(converter.convertStudentCourse(registerStudent, student)).thenReturn(studentsCourses);
    when(converter.convertStudentToResponse(student)).thenReturn(responseStudent);
    when(repository.getStudentCourse(student.getStudentId())).thenReturn(studentsCoursesList);

    StudentDetail studentDetail = sut.setStudentNewCourse(registerStudent);

    verify(converter, times(1)).convertRegisterToStudent(registerStudent);
    verify(converter, times(1)).convertStudentCourse(registerStudent, student);
    verify(converter, times(1)).convertStudentToResponse(student);
    verify(repository, times(1)).getStudentCourse(student.getStudentId());
    verify(repository, times(1)).setStudentData(captorStudent.capture());
    verify(repository, times(1)).setNewCourse(captorStudentCourse.capture());

    Student capturedStudent = captorStudent.getValue();
    StudentsCourses capturedStudentCourse = captorStudentCourse.getValue();

    Assertions.assertEquals(student, capturedStudent);
    Assertions.assertEquals(studentsCourses, capturedStudentCourse);

    Assertions.assertEquals(responseStudent, studentDetail.getResponseStudent());
    Assertions.assertEquals(studentsCoursesList, studentDetail.getStudentsCourses());

  }

  @Test
  void 受講生更新処理が動作すること() {
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

    ResponseStudent responseStudent = new ResponseStudent();
    List<StudentsCourses> studentsCoursesList = new ArrayList<>();

    when(repository.getStudentById(studentId)).thenReturn(student);
    when(converter.convertUpdateToStudent(updateStudent, student)).thenReturn(newStudent);
    when(converter.convertStudentToResponse(newStudent)).thenReturn(responseStudent);
    when(repository.getStudentCourse(newStudent.getStudentId())).thenReturn(studentsCoursesList);

    StudentDetail studentDetail = sut.updateStudent(updateStudent, studentId);

    verify(repository, times(1)).getStudentById(studentId);
    verify(converter, times(1)).convertUpdateToStudent(updateStudent, student);
    verify(converter, times(1)).convertStudentToResponse(newStudent);
    verify(repository, times(1)).getStudentCourse(newStudent.getStudentId());
    verify(repository, times(1)).updateStudent(captorStudent.capture());

    Student capturedStudent = captorStudent.getValue();

    Assertions.assertEquals(newStudent, capturedStudent);
    Assertions.assertEquals(responseStudent, studentDetail.getResponseStudent());
    Assertions.assertEquals(studentsCoursesList, studentDetail.getStudentsCourses());
  }

  @Test
  void 存在しない受講生IDで更新処理がされた時に例外が投げられること() {
    String studentId = "00000000-0000-0000-0000-000000000000";
    UpdateStudent updateStudent = new UpdateStudent();
    when(repository.getStudentById(studentId)).thenReturn(null);
    Assertions.assertThrows(ResourceNotFoundException.class,
        () -> sut.updateStudent(updateStudent, studentId));
  }

}
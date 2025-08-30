package raisetech.StudentManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {
  private final ListUser lu = new ListUser();

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}
  @PostMapping("/regist")
  public String registUser(@RequestParam("name")String name,
      @RequestParam("age")int age,@RequestParam("email")String email){
    lu.add(name,age,email);
    List<User> userList = lu.getUserList();
    return StudentManagementApplication.firstHalf()
        + userList.stream().map(User::toString).collect(Collectors.joining("<br>"))
        + StudentManagementApplication.secondHalf();
  }
  @GetMapping("/first")
  public String first() {
    return """        
        <!DOCTYPE html>
        <html lang="ja">
        <head>
           <meta charset="UTF-8">
           <meta name="viewport" content="width=device-width, initial-scale=1.0">
           <title>登録</title>
        </head>
        <body>
           <h1>登録情報入力</h1>
           <form action="/regist" method="POST">
               名前：<input type="text" name="name"><br>
               年齢：<input type="number" name="age"><br>
               email:<input type="email" name="email"><br>
               <input type="submit" value="送信">
           </form>
  
        </body>
        </html>
        """;
  }
  public static String firstHalf(){
    return """
        <!DOCTYPE html>
        <html lang="ja">
        <head>
           <meta charset="UTF-8">
           <meta name="viewport" content="width=device-width, initial-scale=1.0">
           <title>登録一覧</title>
        </head>
        <body>
          <h1>登録一覧<h1><h2></h2>
        """;
  }
  public static String secondHalf(){
    return """
          <br><br>
          <a href="http://localhost:8080/first">登録画面</a>
        </body>
        </html>
        """;
  }


}
class User{
  private String name;
  private int age;
  private  String email;

  public User(String name,int age,String email){
    this.name = name;
    this.age = age;
    this.email = email;
  }
  public String getName() {
    return name;
  }
  public int getAge() {
    return age;
  }
  public String getEmail() {
    return email;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setAge(int age) {
    this.age = age;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "名前: "+name+"　年齢: "+age+"　email: "+email;
  }
}

class ListUser{
  private List<User> userList = new ArrayList<>();

  public void add(String name,int age,String email){
    userList.add(new User(name,age,email));
  }
  public List<User> getUserList() {
    return userList;
  }
}

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
      @RequestParam(value="age",defaultValue = "0")int age,
      @RequestParam(value="email",defaultValue = "")String email,
      @RequestParam("submit")String submit){
    String msg = (submit.equals("update")) ? lu.addUser(name,age,email) : lu.deleteUser(name);
    List<User> userList = lu.getUserList();
    return StudentManagementApplication.firstHalf()
        + userList.stream().map(User::toString).collect(Collectors.joining("<br>"))
        + "<br><br>"+msg
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
               名前：<input type="text" name="name" required><br>
               年齢：<input type="number" name="age" ><br>
               email:<input type="email" name="email" ><br><br>
               <button type="submit" value="update" name="submit">登録更新</button><br>
               <button type="submit" value="delete" name="submit">削除</button>
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
  private final List<User> userList = new ArrayList<>();

  public String addUser(String name,int age,String email){
    boolean isUpdated = userList.stream().filter(user -> name.equals(user.getName()))
        .findFirst().map(user -> {user.setAge(age);user.setEmail(email);return true;})
        .orElse(false);
    String msg;
    if (isUpdated){
      msg = name+"さんを更新しました。";
    }else{
      userList.add(new User(name,age,email));
      msg = name+"さんを登録しました。";
    }
    return msg;
  }
  public String deleteUser(String name){
    boolean isDeleted=userList.removeIf(user -> name.equals(user.getName()));
    return isDeleted ? name+"さんを削除しました" : "削除対象が見つかりません";
  }
  public List<User> getUserList() {
    return userList;
  }
}

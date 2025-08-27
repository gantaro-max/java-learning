package raisetech.StudentManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}
  @GetMapping("/hello")
  public String hello(){
    return """        
        <!DOCTYPE html>
        <html lang="ja">
          <head>
          <meta charset="UTF-8">
          <title>Hello RaiseTech!</title>
          </head>
          <body>
            <h1>Hello RaiseTech!!</h1>
            <h2>Hello RaiseTech!!</h2>
          </body>
        </html>
        """;
  }

}

CREATE TABLE students (student_id VARCHAR(36) PRIMARY KEY
    ,full_name VARCHAR(100) NOT NULL,kana_name VARCHAR(100) NOT NULL,nick_name VARCHAR(100)
    ,email VARCHAR(255) NOT NULL UNIQUE,address VARCHAR(50),age int,gender VARCHAR(30));


CREATE TABLE students_courses(course_id VARCHAR(36) NOT NULL
    ,student_id VARCHAR(36) NOT NULL,course_name VARCHAR(100) NOT NULL
    ,start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,complete_date TIMESTAMP
    ,PRIMARY KEY (course_id,student_id,start_date)
    ,FOREIGN KEY (student_id) REFERENCES students(student_id));

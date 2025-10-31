CREATE TABLE IF NOT EXISTS students (student_id VARCHAR(36) PRIMARY KEY
    ,full_name VARCHAR(100) NOT NULL,kana_name VARCHAR(100) NOT NULL,nick_name VARCHAR(100)
    ,email VARCHAR(255) NOT NULL UNIQUE,address VARCHAR(50),age int,gender VARCHAR(30)
    ,remark VARCHAR(255),is_deleted BOOLEAN NOT NULL DEFAULT false);


CREATE TABLE IF NOT EXISTS students_courses(take_course_id VARCHAR(36) PRIMARY KEY,course_id VARCHAR(36) NOT NULL
    ,student_id VARCHAR(36) NOT NULL,course_name VARCHAR(100) NOT NULL
    ,start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,complete_date TIMESTAMP
    ,FOREIGN KEY (student_id) REFERENCES students(student_id));


CREATE TABLE apply (apply_id VARCHAR(36) PRIMARY KEY,take_course_id VARCHAR(36) NOT NULL
    ,apply_status VARCHAR(30) NOT NULL,FOREIGN KEY (take_course_id) REFERENCES students_courses(take_course_id));
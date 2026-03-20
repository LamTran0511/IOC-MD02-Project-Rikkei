create database IOC_MD02_Project01;
create schema quanlykhoahoc;
set search_path to quanlykhoahoc;
CREATE TABLE admin(
                      id SERIAL PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL
);
CREATE TABLE student(
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        dob DATE NOT NULL,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        phone VARCHAR(20) UNIQUE,
                        sex BOOLEAN NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP
);
CREATE TABLE course(
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       duration INT NOT NULL,
                       instructor VARCHAR(100) NOT NULL,
                       description TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE enrollment(
                           id SERIAL PRIMARY KEY,

                           student_id INT NOT NULL,
                           course_id INT NOT NULL,

                           registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           status VARCHAR(20) DEFAULT 'WAITING',

                           approved_at TIMESTAMP,
                           approved_by INT,

                           CONSTRAINT fk_student
                               FOREIGN KEY(student_id)
                                   REFERENCES student(id)
                                   ON DELETE CASCADE,

                           CONSTRAINT fk_course
                               FOREIGN KEY(course_id)
                                   REFERENCES course(id)
                                   ON DELETE CASCADE,

                           CONSTRAINT fk_admin
                               FOREIGN KEY(approved_by)
                                   REFERENCES admin(id)
);

INSERT INTO student (name, dob, email, phone, sex, password) VALUES
                                                                 ('Nguyen Van A','2000-05-10','vana@gmail.com','0901234567',true,'123456'),
                                                                 ('Tran Thi B','2001-08-15','thib@gmail.com','0901234568',false,'123456'),
                                                                 ('Le Van C','1999-12-20','vanc@gmail.com','0901234569',true,'123456'),
                                                                 ('Pham Thi D','2002-03-11','thid@gmail.com','0901234570',false,'123456'),
                                                                 ('Hoang Van E','2000-09-25','vane@gmail.com','0901234571',true,'123456');

-- COURSE
INSERT INTO course (name, duration, instructor, description) VALUES
                                                                 ('Java Core',30,'Nguyen Van K','Khóa học Java từ cơ bản đến OOP'),
                                                                 ('Spring Boot',45,'Tran Van L','Xây dựng REST API với Spring Boot'),
                                                                 ('Database PostgreSQL',25,'Le Thi M','Học thiết kế và quản lý CSDL PostgreSQL'),
                                                                 ('Frontend ReactJS',40,'Pham Van N','Phát triển giao diện web với ReactJS'),
                                                                 ('Data Structures',35,'Hoang Thi P','Cấu trúc dữ liệu và giải thuật cơ bản');

-- ENROLLMENT
INSERT INTO enrollment (student_id, course_id, status, approved_by, approved_at) VALUES
                                                                                     (1,1,'APPROVED',1,CURRENT_TIMESTAMP),
                                                                                     (1,3,'WAITING',NULL,NULL),
                                                                                     (2,2,'APPROVED',2,CURRENT_TIMESTAMP),
                                                                                     (2,4,'WAITING',NULL,NULL),
                                                                                     (3,1,'REJECTED',1,CURRENT_TIMESTAMP),
                                                                                     (3,5,'APPROVED',2,CURRENT_TIMESTAMP),
                                                                                     (4,3,'WAITING',NULL,NULL),
                                                                                     (5,2,'APPROVED',1,CURRENT_TIMESTAMP);
ALTER TABLE enrollment DROP CONSTRAINT fk_student;
ALTER TABLE enrollment DROP CONSTRAINT fk_course;
ALTER TABLE enrollment DROP CONSTRAINT fk_admin;

-- Tạo lại khóa ngoại chuẩn

-- 1. student_id → student
ALTER TABLE enrollment
    ADD CONSTRAINT fk_student
        FOREIGN KEY (student_id)
            REFERENCES student(id)
            ON DELETE RESTRICT;

-- 2. course_id → course
ALTER TABLE enrollment
    ADD CONSTRAINT fk_course
        FOREIGN KEY (course_id)
            REFERENCES course(id)
            ON DELETE RESTRICT;

-- 3. approved_by → admin
ALTER TABLE enrollment
    ADD CONSTRAINT fk_admin
        FOREIGN KEY (approved_by)
            REFERENCES admin(id)
            ON DELETE SET NULL;

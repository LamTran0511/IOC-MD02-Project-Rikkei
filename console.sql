create database IOC_MD02_Project;
create schema if not exists projectioc;
set search_path to projectioc;
CREATE TABLE if not exists Admin(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS student (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
                                       dob DATE NOT NULL,
                                       email VARCHAR(100) NOT NULL UNIQUE,
                                       sex BOOLEAN NOT NULL,          -- TRUE = Nam, FALSE = Nữ
                                       phone VARCHAR(20),
                                       password VARCHAR(255) NOT NULL,
                                       create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS course (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
                                      duration INT NOT NULL CHECK (duration > 0),  -- số giờ
                                      instructor VARCHAR(100) NOT NULL,
                                      create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'enrollment_status') THEN
            CREATE TYPE enrollment_status AS ENUM ('WAITING','DENIED','CANCEL','CONFIRMED');
        END IF;
    END
$$;
CREATE TYPE enrollment_status AS ENUM ('WAITING','DENIED','CANCEL','CONFIRMED');
CREATE TABLE IF NOT EXISTS enrollment (
                                          id SERIAL PRIMARY KEY,
                                          student_id INT NOT NULL,
                                          course_id INT NOT NULL,
                                          registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          status enrollment_status DEFAULT 'WAITING',

                                          CONSTRAINT fk_enroll_student FOREIGN KEY (student_id)
                                              REFERENCES student(id) ON DELETE CASCADE,

                                          CONSTRAINT fk_enroll_course FOREIGN KEY (course_id)
                                              REFERENCES course(id) ON DELETE CASCADE
);
-- (Gợi ý) tránh 1 học viên đăng ký trùng 1 khóa nhiều lần
CREATE UNIQUE INDEX IF NOT EXISTS uq_enrollment_student_course
    ON enrollment(student_id, course_id);
INSERT INTO projectioc.admin(username, password) VALUES ('admin','123456');
SELECT * FROM projectioc.student;
INSERT INTO projectioc.student(name, dob, email, sex, phone, password)
VALUES ('Trần Lâm', '2007-11-05', 'lamkagm@gmail.com', true, '0359216000', '123456');
INSERT INTO projectioc.course(name, duration, instructor)
VALUES ('Java Core', 40, 'Mr. Java'),
       ('PostgreSQL', 24, 'Ms. SQL');
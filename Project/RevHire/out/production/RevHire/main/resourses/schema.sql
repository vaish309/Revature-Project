
USE revhire;

CREATE TABLE job_seeker (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(100),
                            email VARCHAR(100) UNIQUE,
                            password VARCHAR(100)
);

CREATE TABLE employer (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          company_name VARCHAR(100),
                          email VARCHAR(100) UNIQUE,
                          password VARCHAR(100)
);

CREATE TABLE job (
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     title VARCHAR(100),
                     location VARCHAR(100),
                     salary DOUBLE,
                     employer_id INT,
                     FOREIGN KEY (employer_id) REFERENCES employer(id)
);

CREATE TABLE application (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             job_id INT,
                             seeker_id INT,
                             status VARCHAR(20),
                             FOREIGN KEY (job_id) REFERENCES job(id),
                             FOREIGN KEY (seeker_id) REFERENCES job_seeker(id)
);

CREATE TABLE resume (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        seeker_id INT UNIQUE,
                        objective TEXT,
                        education TEXT,
                        experience TEXT,
                        skills TEXT,
                        projects TEXT,
                        FOREIGN KEY (seeker_id) REFERENCES job_seeker(id)
);

CREATE TABLE notification (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              user_type VARCHAR(20),
                              user_id INT,
                              message VARCHAR(255)
);

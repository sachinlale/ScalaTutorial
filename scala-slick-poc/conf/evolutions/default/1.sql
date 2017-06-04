# --- !Ups
CREATE TABLE "employee"("id" Int AUTO_INCREMENT PRIMARY KEY ,"name" varchar(200) , "email" varchar(200)  ,"department_id" Int, "position" varchar);
CREATE TABLE "department"("id" Int AUTO_INCREMENT PRIMARY KEY ,"name" varchar(200));
INSERT INTO "department" values (1,'Sales');
INSERT INTO "department" values (2,'Admin');
INSERT INTO "department" values (3,'Engineering');
INSERT INTO "department" values (4,'HR');
INSERT INTO "employee" values (1,'Sachin', 'sachin@gmail.com',1,'Lead Consultant');
INSERT INTO "employee" values (2,'Smith', 'Smith@gmail.com',2,'Senior Director');
INSERT INTO "employee" values (3,'Lokesh', 'lokesh@gmail.com',3,'CTO');
INSERT INTO "employee" values (4,'SK', 'sakha@gmail.com',3,'Senior Consultant');

# --- !Downs

DROP TABLE "employee";
DROP TABLE "department";

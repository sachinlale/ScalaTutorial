# --- !Ups
CREATE TABLE "employee"("id" SERIAL PRIMARY KEY ,"name" varchar(200) , "email" varchar(200)  ,"department" varchar,"position" varchar);
INSERT INTO "employee" values (1,'Sachin', 'sachin@gmail.com','Sales','CTO');
INSERT INTO "employee" values (2,'Smith', 'Smith@gmail.com','Admin','Senior Director');
INSERT INTO "employee" values (3,'Lokesh', 'lokesh@gmail.com','Engineering','Lead Consultant');
INSERT INTO "employee" values (4,'SK', 'sakha@gmail.com','Engineering','Senior Consultant');

# --- !Downs

DROP TABLE "employee";

# --- !Ups
CREATE TABLE "employee"("id" SERIAL PRIMARY KEY ,"name" varchar(200) , "email" varchar(200)  ,"department" varchar,"position" varchar);
INSERT INTO "employee" values (1,'Vikas', 'vikas@knoldus.com','Sales','CTO');
INSERT INTO "employee" values (2,'Bhavya', 'bhavya@knoldus.com','Admin','Senior Director');
INSERT INTO "employee" values (3,'Ayush', 'ayush@knoldus.com','Engineering','Lead Consultant');
INSERT INTO "employee" values (4,'Satendra', 'satendra@knoldus.com','Engineering','Senior Consultant');

# --- !Downs

DROP TABLE "employee";

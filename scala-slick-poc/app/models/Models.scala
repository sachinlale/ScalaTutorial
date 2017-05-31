package models

import models.Department.Department

/*
 * One Approach but cannot use in case pattern matching directly as used in below Employee.
 * WIth this it has to be use Department.Value
 * 
 * 
 */
 object Department extends Enumeration  {
  type Department = Value
  val SALES = withName("Sales") 
  val HR = withName("Human Resource") 
  val ADMIN = withName("Admin") 
  val ENGINEERING = withName("Engineering")
}

case class Employee(name: String, email: String, department: String, position:String, id: Option[Int]=None)


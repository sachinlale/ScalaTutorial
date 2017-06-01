package models

case class Employee(name: String, email: String, department: String, position:String, id: Option[Int]=None)


package models

case class Employee(name: String, email: String, departmentId: Int, position:String, id: Option[Int]=None)
case class Department(name: String, id: Option[Int]=None)
case class EmployeeWithDepartment(employee: Employee, department: Department)


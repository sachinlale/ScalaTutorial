package utils



import models._
import play.api.libs.json.Json


object JsonFormat {

  implicit val departmentFormat = Json.format[Department] 
  implicit val employeeFormat = Json.format[Employee]  
  implicit val empWithDeptFormat = Json.format[EmployeeWithDepartment]

}



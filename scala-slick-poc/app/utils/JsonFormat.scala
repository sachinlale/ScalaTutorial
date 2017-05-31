package utils



import models._
import play.api.libs.json.Json
import models.Department._
import play.api.libs.json.Reads
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import play.api.libs.json.Writes


object JsonFormat {

  implicit val departmentReads = new Reads[Department.Value](){
    def reads(json : JsValue) = {
      json match {
        case JsString(s) => try {
          JsSuccess(Department.withName(s))
        } catch {
          case _ : NoSuchElementException => JsError(s"Department not found $s ")
        }
        case _ => JsError(s"Department not found $json")
      }
    }
  }
  
  implicit val departmentWrites = new Writes[Department] {
  def writes(dep: Department) = Json.obj(
    "department" -> dep.toString()
  )
}
  implicit val employeeFormat = Json.format[Employee]
  

}



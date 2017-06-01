package controllers

import models.Employee
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import repo.EmployeeRepository
import utils.Constants
import utils.JsonFormat._

import scala.concurrent.Future
import repo.EmployeeRepository

/**
 * Controller for Employee REST API
 */
class EmployeeController extends Controller {
  
  import Constants._
//  lazy val empRepository: EmployeeRepository = EmployeeRepository /// TODO: Use Object 
  
  val logger = Logger(this.getClass())

  /**
    * Handles request for getting all employee from the database
    */
  def list() = Action.async {
    EmployeeRepository.getAll().map { res =>
      logger.info("Emp list: " + res)
      Ok(successResponse(Json.toJson(res), "Employee retrieved successfully"))
    }
  }

  /**
    * Handles request for creation of new employee
    */
  def create() = Action.async(parse.json) { request =>
    logger.info("Employee Json ===> " + request.body)
    request.body.validate[Employee].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { emp =>
      
      EmployeeRepository.insert(emp).map { createdEmpId =>
        Ok(successResponse(Json.toJson(Map("id" -> createdEmpId)), "Employee created successfully"))
      }
    })
  }

  /**
    * Handles request for deletion of existing employee by employee_id
    */
  def delete(empId: Int) = Action.async { request =>
    EmployeeRepository.delete(empId).map { _ =>
      Ok(successResponse(Json.toJson("{}"), "Employee deleted successfully"))
    }
  }

  /**
    * Handles request for get employee details for editing
    */
  def edit(empId: Int): Action[AnyContent] = Action.async { request =>
    EmployeeRepository.getById(empId).map { empOpt =>
      empOpt.fold(Ok(errorResponse(Json.toJson("{}"), "Employee does not exists.")))(emp => Ok(
        successResponse(Json.toJson(emp), "Employee retrieved successfully")))
    }
  }

  private def errorResponse(data: JsValue, message: String) = {
    obj("status" -> ERROR, "data" -> data, "msg" -> message)
  }

  /**
    * Handles request for update existing employee
    */
  def update = Action.async(parse.json) { request =>
    logger.info("Employee Json ===> " + request.body)
    request.body.validate[Employee].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { emp =>
      EmployeeRepository.update(emp).map { res => Ok(successResponse(Json.toJson("{}"), "Employee updated successfully")) }
    })
  }

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> SUCCESS, "data" -> data, "msg" -> message)
  }

}




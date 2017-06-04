package controllers

import models.Department
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import repo.DepartmentRepository
import utils.Constants
import utils.JsonFormat._

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

/**
 * Controller for Department REST API
 */
class DepartmentController extends Controller {
  
  import Constants._
  
  val logger = Logger(this.getClass())

  /**
    * Handles request for getting all department from the database
    */
  def list() = Action.async {
    DepartmentRepository.getAll().map { res =>
      logger.info("Dept list: " + res)
      Ok(successResponse(Json.toJson(res), "Department retrieved successfully"))
    }
  }

  /**
    * Handles request for creation of new department
    */
  def create() = Action.async(parse.json) { request =>
    logger.info("Department Json ===> " + request.body)
    request.body.validate[Department].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { dept =>
      
      DepartmentRepository.insert(dept).map { createdDeptId =>
        Ok(successResponse(Json.toJson(Map("id" -> createdDeptId)), "Department created successfully"))
      }
    })
  }

  /**
    * Handles request for deletion of existing department by department_id
    * 
    * handle Referential integrity constraint violation exception
    */
  def delete(deptId: Int) = Action.async { request =>   
    val result : Future[Int] = DepartmentRepository.delete(deptId)
//    result.onComplete {
//      case Success(data) => Future.successful(Ok(successResponse(Json.toJson("{}"), "Department deleted successfully")))
//      case Failure(ex) => Future.successful(BadRequest(errorResponse(Json.toJson("{}"), "Department deletion failed due to refered by employee")))
//      case _ => Future.successful(BadRequest(errorResponse(Json.toJson("{}"), "Department deletion failed due to refered by employee")))
//    }
    result.map { _ =>
      Ok(successResponse(Json.toJson("{}"), "Department deleted successfully"))
    } recover {
      case ex => BadRequest(errorResponse(Json.toJson("{}"), "Department deletion failed due to refered by employee")) 
    }
    
  }

  /**
    * Handles request for get department details for editing
    */
  def get(deptId: Int): Action[AnyContent] = Action.async { request =>
    DepartmentRepository.getById(deptId).map { deptOpt =>
      deptOpt.fold(Ok(errorResponse(Json.toJson("{}"), "Department does not exists.")))(dept => Ok(
        successResponse(Json.toJson(dept), "Department retrieved successfully")))
    }
  }

  private def errorResponse(data: JsValue, message: String) = {
    obj("status" -> ERROR, "data" -> data, "msg" -> message)
  }

  /**
    * Handles request for update existing department
    */
  def update = Action.async(parse.json) { request =>
    logger.info("Department Json ===> " + request.body)
    request.body.validate[Department].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { dept =>
      DepartmentRepository.update(dept).map { res => Ok(successResponse(Json.toJson("{}"), "Department updated successfully")) }
    })
  }

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> SUCCESS, "data" -> data, "msg" -> message)
  }

}




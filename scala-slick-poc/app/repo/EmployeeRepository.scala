package repo

import models.Employee
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import play.api.db.slick.HasDatabaseConfig
import slick.profile.RelationalProfile
import play.api.Play
import models.Department
import repo.DepartmentRepository.DepartmentTable
import models.EmployeeWithDepartment


object EmployeeRepository extends HasDatabaseConfig[JdbcProfile] {
  
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  
  import driver.api._
  import scala.concurrent.ExecutionContext.Implicits.global

  private val empTableQuery = TableQuery[EmployeeTable]
  private val empTableQueryInc = empTableQuery returning empTableQuery.map(_.id)
  private val deptTableQuery = TableQuery[DepartmentTable]
  
  private val empWithDeptTableQuery : Query[(EmployeeTable, DepartmentTable),(Employee, Department), Seq] 
  = empTableQuery join deptTableQuery on(_.departmentId === _.id)
  //empTableQuery.join(deptTableQuery).on(_.departmentId === _.id) equivalent
  
  def insert(employee: Employee): Future[Int] = db.run {
    empTableQuery += employee
  }
  

  def insertAll(employees: List[Employee]): Future[Seq[Int]] = db.run {
    empTableQueryInc ++= employees
  }

  def update(employee: Employee): Future[Int] = db.run {
    empTableQuery.filter(_.id === employee.id).update(employee)
  }

  def delete(id: Int): Future[Int] = db.run {
    empTableQuery.filter(_.id === id).delete
    
  }

  def getAll(): Future[List[EmployeeWithDepartment]] = db.run {
    empWithDeptTableQuery.to[List].result.map(_.map(EmployeeWithDepartment.tupled))
  }

  def getById(empId: Int): Future[Option[EmployeeWithDepartment]] = db.run {
    //empTableQuery.filter(_.id === empId).result.headOption
    empWithDeptTableQuery.filter(_._1.id === empId).result.headOption.map(_.map(EmployeeWithDepartment.tupled))
  }


  class EmployeeTable(tag: Tag) extends Table[Employee](tag, "employee") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val name: Rep[String] = column[String]("name")
    val email: Rep[String] = column[String]("email")
    val departmentId: Rep[Int] = column[Int]("department_id")
    val position: Rep[String] = column[String]("position")
    def emailUnique = index("email_unique_key", email, unique = true)
    def * = (name, email, departmentId, position, id.?) <> (Employee.tupled, Employee.unapply)
    def department = foreignKey("department", departmentId, deptTableQuery)(_.id)
  }

}



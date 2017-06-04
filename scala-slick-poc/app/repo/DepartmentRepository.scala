package repo

import models.Employee
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import play.api.db.slick.HasDatabaseConfig
import slick.profile.RelationalProfile
import play.api.Play
import models.Department

object DepartmentRepository extends HasDatabaseConfig[JdbcProfile] {
  
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  
  import driver.api._
  import scala.concurrent.ExecutionContext.Implicits.global

  private val deptTableQuery = TableQuery[DepartmentTable]
  private val deptTableQueryInc = deptTableQuery returning deptTableQuery.map(_.id)
  
  def insert(dept: Department): Future[Int] = db.run {
    deptTableQueryInc += dept
  }
  

  def update(dept: Department): Future[Int] = db.run {
    deptTableQuery.filter(_.id === dept.id).update(dept)
  }

  def delete(id: Int): Future[Int] = db.run {
    deptTableQuery.filter(_.id === id).delete
    
  }

  def getAll(): Future[List[Department]] = db.run {
    deptTableQuery.to[List].result
  }

  def getById(deptId: Int): Future[Option[Department]] = db.run {
    deptTableQuery.filter(_.id === deptId).result.headOption
  }

  class DepartmentTable(tag: Tag) extends Table[Department](tag, "department") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val name: Rep[String] = column[String]("name")
    def nameUnique = index("departement_name_unique_key", name, unique = true)

    def * = (name, id.?) <>(Department.tupled, Department.unapply)
  }


}



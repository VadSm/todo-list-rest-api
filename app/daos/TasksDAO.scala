package dao

import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject

import models.Task
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait TasksComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  class Tasks(tag: Tag) extends Table[Task](tag, "tasks") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def completed = column[Boolean]("completed")
    def priority = column[String]("priority")

    def * = (id, title, completed, priority) <> ((Task.apply _).tupled, Task.unapply)
  }
}

class TasksDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends TasksComponent
  with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val tasks = TableQuery[Tasks]

  def all(): Future[Seq[Task]] = db.run(tasks.result)

  // /** Construct the Map[String,String] needed to fill a select options set */
  // def options(): Future[Seq[(String, String)]] = {
  //   val query = (for {
  //     company <- companies
  //   } yield (company.id, company.name)).sortBy( /*name*/ _._2)

  //   db.run(query.result).map(rows => rows.map { case (id, name) => (id.toString, name) })
  // }

  // /** Insert a new company */
  // def insert(company: Company): Future[Unit] =
  //   db.run(companies += company).map(_ => ())

  // /** Insert new companies */
  // def insert(companies: Seq[Company]): Future[Unit] =
  //   db.run(this.companies ++= companies).map(_ => ())
}



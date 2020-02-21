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

  def insert(task: Task): Future[Task] = db.run {
    (tasks.map(task => (task.title, task.completed, task.priority))
      returning tasks.map(_.id)
      into ((nameDone, id) => Task(id, nameDone._1, nameDone._2, nameDone._3))
    ) += (task.title, task.completed, task.priority)
  }

  def delete(id: Long): Future[Long] =
    db.run(tasks.filter(_.id === id).delete).map(_ => (id))

  // def update(id: Long, title: String): Future[Task] = db.run {
  //   val taskToUpdate: Computer = tasks.filter(_.id === id).map(_ => task).copy(Some(title))
  //   db.run(tasks.filter(_.id === id).update(taskToUpdate)).map(_ => ())

  //   // tasks.filter(_.id === id).update 
  // }

  // def update(id: Long, newTitle: String): Future[Int] =
  //   db.run(tasks.filter(_.id === id).map(_.title).update(newTitle))

  // def findById(id: Long): Future[Task] =
  //   db.run(tasks.filter(_.id === id))

  // def update(id: Long, updatedTask: Task): Future[Int] = db.run {
  //   tasks.filter(_.id === id).map(task => (task.title, task.completed)).update(updatedTask.title, updatedTask.completed)
  //   // tasks.filter(_.id === id).update(updatedTask.title, updatedTask.completed)
  // }

  def updateTitle(id: Long, newTitle: String): Future[Int] = db.run {
    tasks.filter(_.id === id).map(_.title).update(newTitle)
  }

  def updateCompletedStatus(id: Long, newCompletedStatus: Boolean): Future[Int] = db.run {
    tasks.filter(_.id === id).map(_.completed).update(newCompletedStatus)
  }

    // val q = for { task <- tasks if task.id === id } yield task.title
    // val updateAction = q.update(newTitle)

    // val sql = q.updateStatement

    // tasks.filter(_.id === id).update(updatedTask.title, updatedTask.completed)
  }

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




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

  def create(task: Task): Future[Task] = 
    db.run(tasks returning tasks.map(_.id) += task).map(id => task.copy(id))

  def delete(id: Long) = 
    db.run(tasks.filter(_.id === id).delete)

  def update(updatedTask: Task): Future[Option[Task]] = db.run {
    tasks.filter(_.id === updatedTask.id).update(updatedTask).map {
      case 0 => None
      case _ => Some(updatedTask)
    }
  }

  def updateAllStatuses(newCompletedStatus: Boolean): Future[Boolean] = db.run {
    tasks.map(_.completed).update(newCompletedStatus).map(_ => newCompletedStatus)
  }

  def deleteAllCompleted() = 
    db.run(tasks.filter(_.completed === true).delete)

  // def updateTitle(id: Long, newTitle: String): Future[Int] = db.run {
  //   tasks.filter(_.id === id).map(_.title).update(newTitle)
  // }

  // def updateCompletedStatus(id: Long, newCompletedStatus: Boolean): Future[Int] = db.run {
  //   tasks.filter(_.id === id).map(_.completed).update(newCompletedStatus)
  // }




  // def insert(task: Task): Future[Task] = db.run {
  //   (tasks.map(task => (task.title, task.completed, task.priority))
  //     returning tasks.map(_.id)
  //     into ((nameDone, id) => Task(id, nameDone._1, nameDone._2, nameDone._3))
  //   ) += (task.title, task.completed, task.priority)
  // }

  }




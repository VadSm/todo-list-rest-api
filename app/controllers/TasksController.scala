package controllers

import dao.TasksDAO
import javax.inject.Inject

import models.Task
import play.api.mvc.{ AbstractController, ControllerComponents }

import scala.concurrent.ExecutionContext

import play.api.libs.json.Json

class TasksController @Inject() (
  tasksDAO: TasksDAO,
  controllerComponents: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) {

  def all = Action.async { implicit request =>
    tasksDAO.all().map(task => Ok(Json.toJson(task)))
  }

  def createTask = Action(parse.json[Task]).async { implicit request => 
    tasksDAO.create(request.body).map(task => Ok(Json.toJson(task)))
  }

  def deleteTask(id: Long) = Action.async { implicit request =>
    tasksDAO.delete(id).map(id => Ok(Json.obj("status" -> "Success")))
  }

  def updateTask = Action(parse.json[Task]).async { implicit request =>
    tasksDAO.update(request.body).map(task => Ok(Json.toJson(task)))
  }

  def updateAllStatuses(newCompletedStatus: Boolean) = Action.async { implicit request =>
    tasksDAO.updateAllStatuses(newCompletedStatus).map(status => Ok(Json.obj("status" -> "Success")))
  }

  def deleteAllCompletedTasks = Action.async { implicit request =>
    tasksDAO.deleteAllCompleted().map(_ => Ok(Json.obj("status" -> "Success")))
  }
}

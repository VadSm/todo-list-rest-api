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
    tasksDAO.all() map { task =>
      // If the task was found, return a 200 with the task data as JSON
      Ok(Json.toJson(task))
    }  // otherwise, return Not Found
  }

  def createTask = Action(parse.json[Task]).async { implicit request => 
    tasksDAO.insert(request.body).map(task => Ok(Json.toJson(task)))
  }

  def deleteTask(id: Long) = Action.async { implicit request =>
    tasksDAO.delete(id).map(id => Ok(Json.obj("id" -> id)))
  }

  def updateTaskTitle(id: Long, newTitle: String) = Action.async { implicit request =>
    tasksDAO.updateTitle(id, newTitle).map(id => Ok(Json.obj("id" -> id)))
  }

  def updateCompletedStatus(id: Long, newCompletedStatus: Boolean) = Action.async { implicit request =>
    tasksDAO.updateCompletedStatus(id, newCompletedStatus).map(id => Ok(Json.obj("id" -> id)))
  }

  def updateAllTasksCompletedStatus(newCompletedStatus: Boolean) = Action.async { implicit request =>
    tasksDAO.updateAllCompletedStatus(newCompletedStatus).map(status => Ok(Json.obj("status" -> status)))
  }

  // def updateTask(id: Long, newTitle: String) = Action.async { implicit request =>
  //   tasksDAO.update(id, newTitle).map(id => Ok(Json.obj("id" -> id)))
  // }

  // def updateTask(id: Long) = Action(parse.json[Task]).async { implicit request =>
  //   tasksDAO.update(id, request.body).map(id => Ok(Json.obj("id" -> id)))
  // }
}

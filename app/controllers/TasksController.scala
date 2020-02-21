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

  // def all: Action[AnyContent] = { implicit request =>
  //   tasksDAO.tasksByUserId(request.user.id).map{ tasks => Ok(Json.toJson(tasks)) }
  // }


  // val catForm = Form(
  //   mapping(
  //     "name" -> text(),
  //     "color" -> text())(Cat.apply)(Cat.unapply))

  // val dogForm = Form(
  //   mapping(
  //     "name" -> text(),
  //     "color" -> text())(Dog.apply)(Dog.unapply))

  // def insertCat = Action.async { implicit request =>
  //   val cat: Cat = catForm.bindFromRequest.get
  //   catDao.insert(cat).map(_ => Redirect(routes.Application.index))
  // }

  // def insertDog = Action.async { implicit request =>
  //   val dog: Dog = dogForm.bindFromRequest.get
  //   dogDao.insert(dog).map(_ => Redirect(routes.Application.index))
  // }
}

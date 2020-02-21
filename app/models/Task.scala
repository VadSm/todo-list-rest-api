package models

import play.api.libs.json.Json

case class Task(id: Long, title: String, completed: Boolean, priority: String)

object Task {
  // We're going to be serving this type as JSON, so specify a
  // default Json formatter for our Task type here
  implicit val format = Json.format[Task]
}

object Task {
  implicit val reads: Reads[Task] = for {
      id <- (__ \ "id").readWithDefault[Long](0L)
      title <- (__ \ "title").read[String]
      completed <- (__ \ "completed").readWithDefault[Boolean](false)
      priority <- (__ \ "priority").readWithDefault[String]("Medium")
    } yield Task(id, title, completed, priority)

  implicit val writes: Writes[Task] = task => Json.obj(
    "id" -> task.id,
    "title" -> task.title,
    "completed" -> task.completed,
    "priority" -> task.priority,
  )
}




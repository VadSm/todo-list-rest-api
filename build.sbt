lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """[AЕеЕкщофт1""",
    version := "2.8.x",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      "com.typesafe.play" %% "play-slick" % "5.0.0",
      "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
      "mysql" % "mysql-connector-java" % "8.0.13",
      "com.typesafe.play" %% "play-json" % "2.8.1",
      specs2 % Test,
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )


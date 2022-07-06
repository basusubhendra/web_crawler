val scala3Version = "3.1.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "web_crawler",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.4",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "org.jsoup" % "jsoup" % "1.9.1"
  )
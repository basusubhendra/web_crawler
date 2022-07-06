val scala3Version = "3.1.3"
val AkkaVersion = "2.6.19"

lazy val root = project
  .in(file("."))
  .settings(
    name := "web_crawler",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test
    )
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "org.jsoup" % "jsoup" % "1.9.1"
  )

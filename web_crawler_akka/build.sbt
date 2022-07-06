val scala3Version = "3.1.3"
resolvers += "akka" at "https://repo.akka.io/snapshots"
lazy val root = project
  .in(file("."))
  .settings(
    name := "web_crawler",
    version := "0.1.0-SNAPSHOT",
    mainClass := Some("web_crawler"),
    scalaVersion := scala3Version,
    libraryDependencies += "se.scalablesolutions.akka" % "akka-stm" % "1.3",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor"    % "2.3.4",
    libraryDependencies += "com.typesafe.akka" %% "akka-remote"   % "2.3.4",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "org.jsoup" % "jsoup" % "1.9.1"
  )

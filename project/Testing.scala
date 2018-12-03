import sbt.Keys._
import sbt.{Setting, _}

object Testing {
  lazy val dependencies: Setting[_] = {
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.0.5",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    )
  }
}

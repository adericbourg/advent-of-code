package net.dericbourg.advent

import scala.io.Source

object Run extends App {
  val input = Source.fromResource("input.txt").getLines().toSeq
  println(s"Largest finite area has size: ${Solver.solve(input)}")
}

case class Location(x: Int, y: Int)

case class Area(topLeft: Location, bottomRight: Location)

object Solver {


  def solve(input: Seq[String]): Int = {
    val locations = readLocations(input)
    largestFiniteArea(locations)
  }

  private def readLocations(input: Seq[String]): Seq[Location] = {
    input.map(line => line.split(",").map(_.trim))
      .map {
        case Array(x, y) => Location(x.toInt, y.toInt)
        case other => sys.error(other.mkString(","))
      }
  }

  def largestFiniteArea(locations: Seq[Location]): Int = {
    val closestLocation = findClosestLocation(locations) _
    val area = getArea(locations)

    val grid = (for {
      location <- browseArea(area)
      closestLocation <- closestLocation(location)
    } yield location -> closestLocation).toMap

    val finiteGrid = grid.filterKeys { location =>
      location.x != area.topLeft.x &&
        location.x != area.bottomRight.x &&
        location.y != area.topLeft.y &&
        location.y != area.bottomRight.y
    }
    val sizes = finiteGrid.groupBy(_._2).mapValues(_.size)
    sizes.values.max
  }

  private def findClosestLocation(locations: Seq[Location])(referenceLocation: Location): Option[Location] = {
    val distanceFromReference = manhattanDistance(referenceLocation) _
    val Seq((l1, d1), (_, d2)) = locations
      .map(location => location -> distanceFromReference(location))
      .sortBy(_._2)
      .take(2)
    if (d1 < d2)
      Some(l1)
    else
      None
  }

  private def getArea(locations: Seq[Location]): Area = {
    val minX = locations.minBy(_.x).x
    val minY = locations.minBy(_.y).y
    val maxX = locations.maxBy(_.x).x
    val maxY = locations.maxBy(_.y).y
    Area(Location(minX, minY), Location(maxX, maxY))
  }

  private def browseArea(area: Area): Iterable[Location] = {
    for {
      x <- area.topLeft.x to area.bottomRight.x
      y <- area.topLeft.y to area.bottomRight.y
    } yield Location(x, y)
  }

  private def manhattanDistance(c1: Location)(c2: Location): Int = {
    val Location(x1, y1) = c1
    val Location(x2, y2) = c2

    Math.abs(x1 - x2) + Math.abs(y1 - y2)
  }
}

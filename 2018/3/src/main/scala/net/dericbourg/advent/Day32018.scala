package net.dericbourg.advent

import scala.io.Source
import scala.util.Try

object Day32018 extends App {
  val input = Source.fromResource("input.txt").getLines()

  val fabricUsage = Solver.computeFabricUsage(input)
  val overClaims = Solver.countOverClaims(fabricUsage)
  val nonOverlapping = Solver.findNonOverlapping(fabricUsage)

  println(s"Square inches of fabric are within two or more claims: $overClaims")
  println(s"Claims that don't overlap: ${nonOverlapping.mkString(", ")}")
}

object Solver {
  def countOverClaims(fabricUsage: Map[Coordinates, Usage]): Int = {
    fabricUsage
      .values
      .count(_.count >= 2)
  }

  def findNonOverlapping(fabricUsage: Map[Coordinates, Usage]): Set[Int] = {
    val allClaims = fabricUsage.values.flatMap(_.ids).toSet
    fabricUsage.values.foldLeft(allClaims) { case (claims, usage) =>
      if (usage.count > 1) {
        claims -- usage.ids
      }
      else {
        claims
      }
    }
  }

  def computeFabricUsage(input: Iterator[String]): Map[Coordinates, Usage] = {
    input
      .flatMap(Request.parse)
      .map(asRequestRange)
      .foldLeft(Map.empty[Coordinates, Usage]) { case (acc, range) =>
        range.coordinates.foldLeft(acc) { case (localAcc, coordinates) =>
          val currentUsage = acc.getOrElse(coordinates, Usage(0, Set()))
          val updatedUsage = currentUsage.copy(count = currentUsage.count + 1, ids = currentUsage.ids + range.id)
          localAcc + (coordinates -> updatedUsage)
        }
      }
  }

  def asRequestRange(request: Request): RequestRange = {
    val coordinates = (request.topLeft.x until (request.topLeft.x + request.size.width)).flatMap { x =>
      (request.topLeft.y until (request.topLeft.y + request.size.height)).map { y =>
        Coordinates(x, y)
      }
    }.toSet
    RequestRange(request.id, coordinates)
  }
}

case class Coordinates(x: Int, y: Int)

case class Size(width: Int, height: Int)

case class Request(id: Int, topLeft: Coordinates, size: Size)

object Request {

  private val requestPattern = "#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)".r

  def parse(string: String): Option[Request] = {
    Try {
      val requestPattern(id, x, y, width, height) = string
      Request(id.toInt, Coordinates(x.toInt, y.toInt), Size(width.toInt, height.toInt))
    }.toOption
  }
}

case class RequestRange(id: Int, coordinates: Set[Coordinates])

case class Usage(count: Int, ids: Set[Int])

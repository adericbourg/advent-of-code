package net.dericbourg.advent

import scala.io.Source
import scala.util.Try

object Day32018 extends App {
  val input = Source.fromResource("input.txt").getLines()
  val result = Solver.solve(input)

  println(s"Square inches of fabric are within two or more claims: $result")
}

object Solver {
  def solve(input: Iterator[String]): Int = {
    val fabricUsage = input
      .flatMap(Request.parse)
      .map(asRequestRange)
      .foldLeft(Map.empty[Coordinates, Int]) { case (acc, range) =>
        range.coordinates.foldLeft(acc) { case (localAcc, coordinates) =>
          val currentCount = acc.getOrElse(coordinates, 0)
          localAcc + (coordinates -> (currentCount + 1))
        }
      }
    fabricUsage
      .values
      .count(_ >= 2)
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
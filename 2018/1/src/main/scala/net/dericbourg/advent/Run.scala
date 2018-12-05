package net.dericbourg.advent

import scala.io.Source

object Run extends App {
  val input = Source.fromResource("input.txt").getLines()
  val result = Solver.solve(input)

  println(s"Result: $result")
}

object Solver {
  private val positivePattern = "\\+([0-9]+)".r
  private val negativePattern = "-([0-9]+)".r

  sealed trait Operation {
    def value: Int
  }

  case class Plus(value: Int) extends Operation

  case class Minus(value: Int) extends Operation

  def solve(input: Iterator[String]): Int = {
    input
      .map(asOperation)
      .foldLeft(0) { case (total, operation) =>
        operation match {
          case Plus(value) => total + value
          case Minus(value) => total - value
        }
      }
  }

  private def asOperation(string: String): Operation = {
    string match {
      case positivePattern(value) => Plus(value.toInt)
      case negativePattern(value) => Minus(value.toInt)
      case other => sys.error(s"Cannot match '$other'")
    }
  }
}
package net.dericbourg.advent

import scala.io.Source

object Run extends App {
  val input = Source.fromResource("input.txt").getLines().toSeq

  val checksum = Solver.solve(input)
  println(s"Checksum: $checksum")
}

object Solver {

  def solve(input: Seq[String]): Int = {
    val (twice, thrice) = input
      .map(charCount)
      .fold((0, 0)) { case ((x1, y1), (x2, y2)) => (x1 + x2, y1 + y2) }
    twice * thrice
  }

  def charCount(str: String): (Int, Int) = {
    val counts = str
      .toCharArray
      .foldLeft(Map.empty[Char, Int]) { case (count, char) =>
        count + (char -> (count.getOrElse(char, 0) + 1))
      }
      .values
      .toSet
    (
      if (counts.contains(2)) 1 else 0,
      if (counts.contains(3)) 1 else 0
    )
  }


}
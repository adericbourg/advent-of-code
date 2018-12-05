package net.dericbourg.advent

import scala.annotation.tailrec
import scala.io.Source

object Run extends App {
  val input = Source.fromResource("input.txt").getLines()
  val shifts = input.map(_.toInt).toList

  val result = Solver.solve(shifts)
  println(s"Result: $result")

  val stableFreq = Solver.findStableFrequency(shifts)
  println(s"Stable frequency: $stableFreq")
}

object Solver {

  def solve(input: Seq[Int]): Int = input.sum

  def findStableFrequency(shifts: List[Int]): Option[Int] = {
    @tailrec
    def find(s: Stream[Int], previous: Int, seen: Set[Int]): Option[Int] = {
      val shift = s.head
      val frequency = previous + shift
      if (seen.contains(frequency)) {
        Some(frequency)
      }
      else {
        find(s.tail, frequency, seen + frequency)
      }
    }

    find(Stream.continually(shifts.toStream).flatten, 0, Set.empty)
  }
}
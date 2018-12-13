package net.dericbourg.advent

import scala.annotation.tailrec
import scala.io.Source

object Run extends App {
  val input = Source.fromResource("input.txt").getLines().next()

  val polymer = Solver.resultingPolymer(input)
  println(s"Resulting polymer: $polymer")
  println(s"  size: ${polymer.length}")

  println()

  val perUnitStrippedPolymer = Solver.shortestPerUnitPolymer(input)
  val (u, shortest, l) = perUnitStrippedPolymer
    .toSeq
    .map { case (unit, p) => (unit, p, p.length) }
    .minBy { case (_, _, length) => length }

  println(s"Shortest stripped polymer (for unit $u): $shortest")
  println(s"  size: $l")
}

object Solver {

  private val units = 'a' to 'z'
  private val reactions = units
    .map(c => s"$c${c.toUpper}")
    .flatMap(r => Seq(r, r.reverse))

  @tailrec
  def resultingPolymer(polymer: String): String = {
    val result = reactions.foldLeft(polymer) { case (resulting, reaction) =>
      resulting.replace(reaction, "")
    }
    if (result == polymer) {
      result
    }
    else {
      resultingPolymer(result)
    }
  }

  def shortestPerUnitPolymer(polymer: String): Map[Char, String] = {
    val stripPolymer = removeUnit(polymer) _
    units.map(unit => (unit, stripPolymer(unit)))
      .map { case (unit, strippedPolymer) => (unit, resultingPolymer(strippedPolymer)) }
      .toMap
  }

  private def removeUnit(polymer: String)(unit: Char) = {
    polymer
      .replaceAll(unit.toString, "")
      .replaceAll(unit.toUpper.toString, "")
  }
}
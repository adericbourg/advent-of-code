package net.dericbourg.advent

import scala.io.Source

object Run extends App {
  val input = Source.fromResource("input.txt").getLines().toSeq

  val checksum = Solver.solve(input)
  println(s"Checksum: $checksum")

  println("Boxes matching")
  Solver.matchBoxes(input).foreach { comp =>
    println(s"  ${comp.word1} with ${comp.word2}: '${comp.commonLetters}'")
  }
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

  def matchBoxes(input: Seq[String]): Seq[WordComparison] = {

    def run(word: String, remainingWords: List[String], matchingComparisons: Seq[WordComparison]): Seq[WordComparison] = {

      val compareToWord1 = compareWords(word) _
      val matchings = remainingWords
        .map(compareToWord1)
        .filter(comparison => comparison.commonLetters.length == (word.length - 1))
      remainingWords match {
        case next :: others => run(next, others, matchingComparisons ++ matchings)
        case Nil => matchingComparisons
      }
    }

    input.toList match {
      case head :: tail => run(head, tail, Seq.empty)
      case _ => sys.error("Unexpected input")
    }

  }

  private def compareWords(word1: String)(word2: String): WordComparison = {
    val common = word1.zip(word2).filter { case (c1, c2) => c1 == c2 }.map(_._1).mkString
    WordComparison(word1, word2, common)
  }

  case class WordComparison(word1: String, word2: String, commonLetters: String)

}
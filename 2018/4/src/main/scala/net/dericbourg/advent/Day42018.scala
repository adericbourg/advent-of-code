package net.dericbourg.advent

import java.time.format.DateTimeFormatter
import java.time.{Duration, LocalDateTime}

import net.dericbourg.advent.Event.{GuardFallsAsleep, GuardWakesUp, ShiftStart}

import scala.io.Source

object Day42018 extends App {
  val input = Source.fromResource("input.txt").getLines()
  Solver.solve(input)
}

object Solver {

  def solve(input: Iterator[String]): Unit = {
    val (sleepingSlots, _, _) = input
      .map(Event.parse)
      .toSeq
      .sorted
      .foldLeft((Seq.empty[SleepingSlot], Option.empty[Int], Option.empty[LocalDateTime])) { case ((slots, guard, sleepBegin), event) =>
        event match {
          case ShiftStart(_, guardId) =>
            (slots, Some(guardId), None)
          case GuardFallsAsleep(time) =>
            (slots, guard, Some(time))
          case GuardWakesUp(time) =>
            (guard, sleepBegin) match {
              case (Some(guardId), Some(startTime)) =>
                (slots :+ SleepingSlot(guardId, startTime, time), guard, sleepBegin)
              case _ =>
                sys.error(s"$guard | $sleepBegin")
            }
        }
      }

    val mostSleepyGuard: (Int, Duration) = findMostSleepyGuard(sleepingSlots)
    val mostSleepyMinute: Int = findMostSleepyMinuteForGuard(sleepingSlots, mostSleepyGuard._1)

    println(s"Most sleepy guard:  ${mostSleepyGuard._1} (${mostSleepyGuard._2.toMinutes} min)")
    println(s"Most sleepy minute: $mostSleepyMinute")
    println()
    println(s"Expected result: ${mostSleepyGuard._1} x $mostSleepyMinute = ${mostSleepyGuard._1 * mostSleepyMinute}")
  }

  private def findMostSleepyGuard(sleepingSlots: Seq[SleepingSlot]) = {
    val sleepTimePerGuard = sleepingSlots
      .groupBy(_.guardId)
      .mapValues(_.map(_.sleepDuration).fold(Duration.ZERO)((a, b) => a.plus(b)))
    val mostSleepyGuard = sleepTimePerGuard.toSeq.sortBy(kv => kv._2.toMinutes).reverse.head
    mostSleepyGuard
  }

  private def findMostSleepyMinuteForGuard(sleepingSlots: Seq[SleepingSlot], guardId: Int) = {
    val sleepOccurrencePerMinute = sleepingSlots
      .filter(_.guardId == guardId)
      .flatMap(_.sleepyMinutes)
      .foldLeft(Map.empty[Int, Int]) { case (count, minute) =>
        count + (minute -> (count.getOrElse(minute, 0) + 1))
      }
    val mostSleepyMinute = sleepOccurrencePerMinute.toSeq.sortBy(_._2).reverse.map(_._1).head
    mostSleepyMinute
  }
}

object Event {

  private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  private val datePattern = "\\[(.*)\\]"
  private val shiftPattern = s"$datePattern Guard #([0-9]+) begins shift".r
  private val sleepPattern = s"$datePattern falls asleep".r
  private val wakePattern = s"$datePattern wakes up".r

  def parse(str: String): Event = {
    str match {
      case shiftPattern(date, guardId) => ShiftStart(parseDate(date), guardId.toInt)
      case sleepPattern(date) => GuardFallsAsleep(parseDate(date))
      case wakePattern(date) => GuardWakesUp(parseDate(date))
    }
  }

  private def parseDate(date: String) = {
    LocalDateTime.parse(date, dateTimeFormatter)
  }

  sealed trait Event extends Ordered[Event] {
    def time: LocalDateTime

    def compare(other: Event): Int = this.time.compareTo(other.time)
  }

  case class ShiftStart(time: LocalDateTime, guardId: Int) extends Event

  case class GuardFallsAsleep(time: LocalDateTime) extends Event

  case class GuardWakesUp(time: LocalDateTime) extends Event

}

case class SleepingSlot(guardId: Int, startTime: LocalDateTime, endTime: LocalDateTime) {
  val sleepDuration: Duration = Duration.between(startTime, endTime)
  val sleepyMinutes: Seq[Int] = SleepingSlot.minutesInRange(startTime, endTime)
}

object SleepingSlot {
  def minutesInRange(startTime: LocalDateTime, endTime: LocalDateTime): Seq[Int] = {
    startTime.getMinute until endTime.getMinute
  }
}
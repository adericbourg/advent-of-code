package net.dericbourg.advent

import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.Source

object Run extends App {
  val input = Source.fromResource("input.txt")
    .getLines()
    .map(Dependency.parse)

  val dependencyGraph = new DependencyGraph()
  input.foreach(dep => dependencyGraph.append(dep.dependency, dep.dependant))

  val inOrderNodes = Solver.solve(dependencyGraph)

  println(s"In order use: ${inOrderNodes.mkString}")
}

object Solver {
  def solve(dependencyGraph: DependencyGraph): Seq[String] = {

    @tailrec
    def loop(dependencyGraph: DependencyGraph, visited: Seq[String]): Seq[String] = {
      dependencyGraph.nextFreeNode(visited) match {
        case Some(nextNode) => loop(dependencyGraph, visited :+ nextNode.value)
        case None           => visited
      }
    }

    loop(dependencyGraph, Seq())
  }

}

class DependencyGraph {

  import Node._

  val nodes: mutable.Map[String, Node] = mutable.Map.empty

  def append(parent: String, value: String): Unit = {
    val node = getNode(value)
    node.parents += parent
  }

  def nextFreeNode(freed: Seq[String]): Option[Node] = {
    nodes.values
      .filterNot(node => freed.contains(node.value))
      .filter(node => node.parents.forall(freed.contains)).toSeq.sorted.headOption
  }

  private def getNode(value: String): Node = {
    nodes.getOrElseUpdate(value, Node(value))
  }
}

case class Node(value: String,
                parents: mutable.Set[String] = mutable.Set.empty)

object Node {
  implicit val Order: Ordering[Node] = Ordering.by(node => node.value)
}

case class Dependency(dependency: String, dependant: String)

object Dependency {
  private val pattern = "Step (\\w+) must be finished before step (\\w+) can begin.".r

  def parse(line: String): Dependency = {
    val pattern(dependency, dependant) = line
    Dependency(dependency, dependant)
  }
}
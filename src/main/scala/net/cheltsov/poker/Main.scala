package net.cheltsov.poker

import net.cheltsov.poker.Solver.process
import scala.io.StdIn


object Main {
  def main(args: Array[String]): Unit = Iterator.continually(Option(StdIn.readLine()))
    .takeWhile(_.nonEmpty)
    .foreach { x =>
      x map process foreach println
    }
}

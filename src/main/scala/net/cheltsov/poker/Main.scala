package net.cheltsov.poker

import net.cheltsov.poker.Solver.process
import net.cheltsov.poker.binary.BiCards
import net.cheltsov.poker.denary.DeCards

import scala.io.StdIn


object Main {
  def main(args: Array[String]): Unit = Iterator.continually(Option(StdIn.readLine()))
    .takeWhile(_.nonEmpty)
    .foreach { x =>
      x map process(parser(args.headOption)) foreach println
    }

  def parser(arg: Option[String]): String => Cards = arg match {
    case Some("-b") | None => BiCards.apply
    case Some("-d")        => DeCards.apply
    case any               => throw new UnsupportedOperationException(s"Unrecognized option: $any")
  }
}

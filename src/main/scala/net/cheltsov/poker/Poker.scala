package net.cheltsov.poker

import scala.io.StdIn

object Poker extends App {

  Iterator.continually(StdIn.readLine()).takeWhile(i => i != null && i != "q")
    .map(Parser.pars(_, args.contains("--omaha"))).map {
    case Left(m) => Some(m)
    case Right(game) => {
      game.tail
        .map { h =>
          h.combinations(2)
            .map(_.reduceLeft(_ + _))
            .flatMap(p => game.head.combinations(3).map(p + _.reduceLeft(_ + _)))
            .max
        }
        .zip(game.tail)
        .map(p => (p._1, p._2.map(_.toString).reduceLeft(_ + _)))
        .sortBy(p => (p._1, p._2))
        .sliding(2)
        .map { case List(l, r) => if (l._1 < r._1) (l._2, " " + r._2) else (l._2, "=" + r._2) }
        .foldLeft[Option[String]](None) {
          case (None, (l, r)) => Some(l + r)
          case (Some(l), (_, r)) => Some(l + r)
        }
    }
  }.foreach(result => println(result.getOrElse("Ooooops. It was going to be unreachable")))
}

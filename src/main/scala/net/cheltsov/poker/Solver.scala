package net.cheltsov.poker

import net.cheltsov.poker.binary.{BiCards, BiHand}
import net.cheltsov.poker.denary.DeCards

object Solver {
  val TexasHoldEm: String  = "texas-holdem"
  val OmahaHoldEm: String  = "omaha-holdem"
  val FiveCardDraw: String = "five-card-draw"
  val SupportedGames: List[String] = List(TexasHoldEm, OmahaHoldEm, FiveCardDraw)
  val ErrorPrefix = "Error:"

  def process(line: String): String = {
    Parser.pars(line, DeCards.apply) match {
      case Left(error)                              => s"$ErrorPrefix $error. Line: $line"
      case Right((FiveCardDraw, fivers))            => processHands(fivers.map(p => (p._1.toHand.get, p._2)))
      case Right((_, (board, _) :: distributions))  => processHands(distributions.map(findBestHand(board)))
      case _                                        => s"$ErrorPrefix Kind of 500)). Line: $line"
    }
  }

  private def findBestHand(board: Cards)(distribution: (Cards, String)): (Hand, String) =
    ((for {
      casino  <- board.combineCards(3)
      gambler <- distribution._1.combineCards(2)
    } yield
      (casino + gambler).toHand.get).max, distribution._2)

  private def processHands(hands: List[(Hand, String)]): String =
    makeString(hands.sorted)

  private def makeString(sortedHands: List[(Hand, String)]): String =
    (sortedHands zip sortedHands.tail).map {
      case ((leftHand,  leftStr), (rightHand, _)) if leftHand.compare(rightHand) == 0  => s"$leftStr="
      case ((_,         leftStr), (_, _))                                              => s"$leftStr "
    }.mkString("") + sortedHands.last._2
}
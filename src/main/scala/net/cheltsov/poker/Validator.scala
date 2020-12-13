package net.cheltsov.poker

import net.cheltsov.poker.Solver.{FiveCardDraw, OmahaHoldEm, TexasHoldEm}
import net.cheltsov.poker.binary.BiCards

import scala.util.matching.Regex

object Validator {
  val BlankRegex: String = "\\s+"
  val CardRegex: String = "([AKQJT2-9][hdcs])"
  val TexasHoldEmRegex: Regex = s"$TexasHoldEm\\s+$CardRegex{5}(\\s+$CardRegex{2})+".r
  val OmahaHoldEmRegex: Regex = s"$OmahaHoldEm\\s+$CardRegex{5}(\\s+$CardRegex{4})+".r
  val FiveCardDrawRegex: Regex = s"$FiveCardDraw(\\s+$CardRegex{5})+".r

  def isValidInput(input: String): Boolean = {
    val regex = input.split(BlankRegex).head match {
      case TexasHoldEm  => TexasHoldEmRegex
      case OmahaHoldEm  => OmahaHoldEmRegex
      case FiveCardDraw => FiveCardDrawRegex
      case _            => throw new IllegalStateException()
    }
    regex matches input
  }

  def areCardsUnique(cards: List[BiCards]): Boolean = {
    cards.map(_.size).sum == cards.reduce(_ + _).size
  }
}

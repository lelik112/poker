package net.cheltsov.poker.denary

import net.cheltsov.poker.denary.DeHand._
import net.cheltsov.poker.{Cards, Hand}

class DeHand(override val cards: Set[Card]) extends DeCards(cards) with Hand {

  override def sameRankFinder(amount: Int): CombinationFinder = findSameRank(amount)
}

object DeHand {
  def apply(cards: Set[Card]): DeHand = new DeHand(cards)
  def apply(deCards: DeCards): DeHand = new DeHand(deCards.cards)

  private def findSameRank(amount: Int)(cards: Cards): Option[DeCards] =
    DeCards(cards).cards
      .groupBy(_.rank)
      .filter(_._2.size == amount)
      .values
      .headOption
      .map(DeCards(_))
}
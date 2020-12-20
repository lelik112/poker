package net.cheltsov.poker.string

import net.cheltsov.poker.{Cards, Hand}

class StHand(override val cards: String) extends StCards(cards) with Hand {

  override protected def findSameRankCards(amount: Order)(cards: Cards): Option[Cards] =
    StCards(cards).asList
      .groupMapReduce(_.head)(identity)(_ + _)
      .filter(_._2.length == amount * 2)
      .values
      .map(StCards(_))
      .headOption

  override protected def findFlush(cards: Cards): Option[Cards] = {
    val stCards = StCards(cards)
    Option.when(stCards.asList.map(_.last).distinct.size == 1)(stCards)
  }
}

object StHand {
  def apply(stCards: StCards): StHand = new StHand(stCards.cards)
}
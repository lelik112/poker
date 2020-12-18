package net.cheltsov.poker.string

import net.cheltsov.poker.Hand

class StHand(override val cards: String) extends StCards(cards) with Hand

object StHand {
  def apply(stCards: StCards): StHand = new StHand(stCards.cards)
}
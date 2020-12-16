package net.cheltsov.poker.denary

import net.cheltsov.poker.Hand

class DeHand(override val cards: Set[Card]) extends DeCards(cards) with Hand

object DeHand {
  def apply(cards: Set[Card]): DeHand = new DeHand(cards)
  def apply(deCards: DeCards): DeHand = new DeHand(deCards.cards)
}
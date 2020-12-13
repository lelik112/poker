package net.cheltsov.poker.denary

import net.cheltsov.poker.denary.DeHand._
import net.cheltsov.poker.{Cards, Hand}

class DeHand(override val cards: Set[Card]) extends DeCards(cards) with Hand[DeHand, DeCards] {

  override val FlushFinder: CombinationFinder = findFlush
  override val StraightFinder: CombinationFinder = findStraight
  override val LowestStraightFinder: CombinationFinder = findLowestStraight

  override def sameRankFinder(amount: Int): CombinationFinder = findSameRank(amount)
}

object DeHand {
  def apply(cards: Set[Card]): DeHand = new DeHand(cards)
  def apply(deCards: DeCards): DeHand = new DeHand(deCards.cards)

  private def findSameRank(amount: Int)(deCards: DeCards): Option[DeCards] =
    deCards.cards
      .groupBy(_.rank)
      .filter(_._2.size == amount)
      .values
      .headOption
      .map(DeCards(_))

  private def findFlush(deCards: DeCards): Option[DeCards] = {
    deCards.cards.map(_.name).size match {
      case 1 => Some(deCards)
      case _ => None
    }
  }

  private def findStraight(deCards: DeCards): Option[DeCards] = {
    Option.when(Cards.Ranks.containsSlice(deCards.ranks.sorted))(deCards)
  }

  private def findLowestStraight(deCards: DeCards): Option[DeCards] =
    Option.when(deCards.ranks.sorted.toSet.subsetOf(Set(2, 3, 4, 5, 14)))(deCards)
}
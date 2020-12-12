package net.cheltsov.poker.denary

import net.cheltsov.poker.Cards

case class DeCards(cards: Set[Card]) extends Cards[DeCards]{

  override def +(that: DeCards): DeCards = DeCards(this.cards ++ that.cards)
  override def -(that: DeCards): DeCards = DeCards(this.cards -- that.cards)

  override val size: Int = cards.size

  override def compareByRank(that: DeCards): Int =
    (this.sortedRanks.reverse zip that.sortedRanks.reverse).foldLeft(0) {
      case (0, p) => p._1 - p._2
      case (res, _) => res
    }

  def sortedRanks: List[Int] = cards.toList.map(_.rank).sorted

  override def toString: String = cards.foldLeft("")(_ + _)
}

object DeCards {
  def apply(value: String): DeCards = {
    def parse(chars: List[Char], acc: List[Card]): List[Card] = chars match {
      case Nil => acc
      case r :: s :: xs => Card(r, s) :: parse(xs, acc)
    }
    new DeCards(parse(value.toCharArray.toList, Nil).toSet)
  }
}

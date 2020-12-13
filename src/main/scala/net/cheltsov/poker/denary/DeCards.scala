package net.cheltsov.poker.denary

import net.cheltsov.poker.Cards

case class DeCards(cards: Set[Card]) extends Cards[DeCards]{

  override def +(that: DeCards): DeCards = DeCards(this.cards ++ that.cards)
  override def -(that: DeCards): DeCards = DeCards(this.cards -- that.cards)

  override val size: Int = cards.size
  override val ranks: List[Int] = cards.toList.map(_.rank)

  override def combinations(n: Int): List[DeCards] =
    cards.toList.combinations(n).map(DeCards(_)).toList

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

  def apply(cards: Iterable[Card]): DeCards = new DeCards(cards.toSet)
}

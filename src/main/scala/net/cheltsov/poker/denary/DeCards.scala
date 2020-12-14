package net.cheltsov.poker.denary

import net.cheltsov.poker.{Cards, Clubs, Diamonds, Hearts, Spades, Suit}
import net.cheltsov.poker.Cards.{Rank, fromRank, toRank}

import scala.reflect.ClassTag

case class DeCards(cards: Set[Card]) extends Cards{

  override def +(that: Cards): DeCards = DeCards(this.cards ++ DeCards(that).cards)
  override def -(that: Cards): DeCards = DeCards(this.cards -- DeCards(that).cards)

  override lazy val asPairs: Set[(Rank, Suit)] = cards.map(c => (c.rank, c.suit))

  override def combineCards(n: Int): List[DeCards] =
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

  def apply[X: ClassTag](asPairs: Set[(Rank, Suit)]): DeCards =
    new DeCards(asPairs.map(p => Card(p._1, p._2)))

  def apply(cards: Cards): DeCards = cards match {
    case biCards: DeCards => biCards
    case _                => apply(cards.asPairs)
  }
}

sealed abstract class Card(val suit: Suit) {
  val rank: Rank
  override def toString: String = fromRank(rank) + suit
}

case class Spade  (rank: Rank) extends Card(Spades)
case class Heart  (rank: Rank) extends Card(Hearts)
case class Diamond(rank: Rank) extends Card(Diamonds)
case class Club   (rank: Rank) extends Card(Clubs)

object Card {
  def apply(r: Char, s: Char): Card =
    Card(toRank(r.toString), Suit(s.toString))

  def apply(rank: Rank, suit: Suit): Card = suit match {
    case Spades   => Spade(rank)
    case Hearts   => Heart(rank)
    case Diamonds => Diamond(rank)
    case Clubs    => Club(rank)
  }
}

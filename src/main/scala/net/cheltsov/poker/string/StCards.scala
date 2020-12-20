package net.cheltsov.poker.string

import net.cheltsov.poker.Cards.Rank
import net.cheltsov.poker.{Cards, Suit}

case class StCards(cards: String) extends Cards {

  override def toHand: Option[StHand] = if (size == 5) Some(StHand(this)) else None

  override def +(that: Cards): StCards = StCards(this.cards + that)

  override def -(that: Cards): StCards = StCards(
    StCards(that).asList.foldLeft(this.cards)(_.replaceAll(_, ""))
  )

  override def combineCards(n: Int): List[StCards] =
    asList.combinations(n).map(_.reduce(_ + _)).map(StCards(_)).toList

  override lazy val asPairs: Set[(Rank, Suit)] =
    asList.map(card => (Cards.toRank(card.head.toString), Suit.apply(card.last.toString))).toSet

  override lazy val size: Int = cards.length / 2

  override def toString: String = cards

  protected lazy val asList: List[String] = cards.grouped(2).toList
}

object StCards {

  def apply(asPairs: Set[(Rank, Suit)]): StCards =
    new StCards(asPairs.map(p => Cards.fromRank(p._1) + p._2).mkString(""))

  def apply(cards: Cards): StCards = cards match {
    case stCards: StCards => stCards
    case _                => StCards(cards.toString)
  }
}

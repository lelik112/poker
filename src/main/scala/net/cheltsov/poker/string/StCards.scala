package net.cheltsov.poker.string

import net.cheltsov.poker.Cards.Rank
import net.cheltsov.poker.{Cards, Suit}

case class StCards(cards: String) extends Cards {

  override def toHand: Option[StHand] = if (size == 5) Some(StHand(this)) else None
  override def +(that: Cards): Cards = StCards(this.cards + that)
  override def -(that: Cards): Cards = StCards(
    that.toString.grouped(2).foldLeft(this.cards)(_.replaceAll(_, ""))
  )

  override def combineCards(n: Int): List[StCards] =
    asList.combinations(n).map(_.reduce(_ + _)).map(StCards(_)).toList

  override val asPairs: Set[(Rank, Suit)] =
    asList.map(card => (Cards.toRank(card.head.toString), Suit.apply(card.last.toString))).toSet

  override def toString: String = cards

  private def asList: List[String] = cards.grouped(2).toList
}

object StCards {

  def apply(asPairs: Set[(Rank, Suit)]): StCards =
    new StCards(asPairs.map(p => Cards.fromRank(p._1) + p._2).mkString(""))
}

package net.cheltsov.poker.binary

import net.cheltsov.poker.Cards.Rank
import net.cheltsov.poker.binary.BiCards.positions
import net.cheltsov.poker.binary.BitUtil._
import net.cheltsov.poker.{Cards, Clubs, Diamonds, Hearts, Spades, Suit}

case class BiCards(cards: Long) extends Cards {

  override def +(that: Cards): BiCards = BiCards(this.cards | BiCards(that).cards)
  override def -(that: Cards): BiCards = BiCards(this.cards ^ BiCards(that).cards)

  override lazy val size: Int = cards.countBits

  override def compareByRank(that: Cards): Int =
    this.cards.collapseBitsToRightQuarter - BiCards(that).cards.collapseBitsToRightQuarter

  override def combineCards(n: Int): List[BiCards] =
    cards.splitBits.combinations(n).map(_.reduce(_ | _)).map(BiCards(_)).toList

  override lazy val asPairs: Set[(Rank, Suit)] =
    (for {
      rank              <- Cards.Ranks
      (suit, position)  <- positions.toSet
      if ((cards >> (16 * position + rank)) & 1) > 0
    } yield (rank, suit)).toSet

  override def toString: String =
    (for {
      rank              <- Cards.Ranks
      (name, position)  <- positions
      if ((cards >> (16 * position + rank)) & 1) > 0
    } yield Cards.fromRank(rank) + name).foldLeft("") {
      _ + _
    }
}

object BiCards {
  import net.cheltsov.poker.Cards._

  val positions: Map[Suit, Int] = Map(Spades -> 0, Hearts -> 1, Diamonds -> 2, Clubs -> 3)

  def apply(value: String): BiCards = {
    def parse(chars: List[String], acc: Long): Long = chars match {
      case Nil          => acc
      case r :: p :: xs => (1L << (toRank(r) + positions(Suit(p)) * 16)) | parse(xs, acc)
    }
    new BiCards(parse(value.split("").toList, 0L))
  }

  def apply(asPairs: Set[(Rank, Suit)]): BiCards =
    BiCards(asPairs.map(p => 1L << (p._1 + positions(p._2))).reduce(_ | _))

  def apply(cards: Cards): BiCards = cards match {
    case biCards: BiCards => biCards
    case _                => BiCards(cards.asPairs)
  }
}

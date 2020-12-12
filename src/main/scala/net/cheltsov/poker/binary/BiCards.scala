package net.cheltsov.poker.binary

import net.cheltsov.poker.binary.BiCards.positions
import net.cheltsov.poker.binary.BitUtil._
import net.cheltsov.poker.Cards

case class BiCards(cards: Long) extends Cards[BiCards] {

  override def +(that: BiCards): BiCards = BiCards(this.cards | that.cards)
  override def -(that: BiCards): BiCards = BiCards(this.cards ^ that.cards)

  override lazy val size: Int = cards.countBits

  override def compareByRank(that: BiCards): Int =
    this.cards.collapseBitsToRightQuarter - that.cards.collapseBitsToRightQuarter

  override def toString: String = {
    (for {
      rank <- Cards.Ranks
      (name, position) <- positions
      if ((cards >> (16 * position + rank)) & 1) > 0
    } yield Cards.fromRank(rank) + name).reduceLeft {
      _ + _
    }
  }
}

object BiCards {
  import net.cheltsov.poker.Cards._

  val positions: Map[String, Int] = Map(SpadesName -> 0, HeartsName -> 1, DiamondsName -> 2, ClubsName -> 3)

  def apply(value: String): BiCards = {
    def parse(chars: List[String], acc: Long): Long = chars match {
      case Nil => acc
      case r :: p :: xs => (1L << (toRank(r) + positions(p) * 16)) | parse(xs, acc)
    }
    new BiCards(parse(value.split("").toList, 0L))
  }
}

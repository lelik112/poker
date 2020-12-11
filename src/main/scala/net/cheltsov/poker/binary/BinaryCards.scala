package net.cheltsov.poker.binary

import net.cheltsov.poker.Converter._
import net.cheltsov.poker.binary.Converter._
import net.cheltsov.poker.Suit

case class BinaryCards(value: Long) {
  def +(that: BinaryCards): BinaryCards = BinaryCards(this.value | that.value)

  def -(that: BinaryCards): BinaryCards = BinaryCards(this.value ^ that.value)

  lazy val size: Int = value.countBits

  def compareByHeist(that: BinaryCards): Int = this.value.collapseBitsToFirstQuarter - that.value.collapseBitsToFirstQuarter

  lazy val suits: List[Suit] = Suit.suitByPosition.toList.map(p => p._2(((value >>> (16 * p._1)) & 0xFFFFL).toInt))

  override def toString: String = {
    (for {
      number <- 14 to 2 by -1
      suit <- suits
      if ((suit.value >> number) & 1) > 0
    } yield number.toStringRepresentation + suit.name).reduceLeft {
      _ + _
    }
  }
}

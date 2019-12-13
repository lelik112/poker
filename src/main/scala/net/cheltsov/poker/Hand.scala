package net.cheltsov.poker

import net.cheltsov.poker.Converter._

case class Hand(value: Long) extends Ordered[Hand]{
  def |(that: Hand): Hand = {
    Hand(this.value | that.value)
  }

  def ^(that: Hand): Hand = {
    Hand(this.value ^ that.value)
  }

  def size: Int = value.countBits

  lazy val suits: List[Suit] = this.toSuits

  override def toString: String = {
    (for {
      number <- 14 to 2 by -1
      suit <- suits
      if ((suit.value >> number) & 1) > 0
    } yield number.toStringRepresentation + suit.name).reduceLeft {
      _ + _
    }
  }

  override def compare(that: Hand): Int = {
    Hand.combinations.foldLeft[Option[Int]](None) {
      case (None, f) => defineWinner(that, f)
      case (result, _) => result
    }.getOrElse(compareByHighestCard(that))
  }

  private def defineWinner(that: Hand, findCombination: Hand => Option[Hand]): Option[Int] = {
    (findCombination(this), findCombination(that)) match {
      case (None, None) => None
      case (Some(_), None) => Some(1)
      case (None, Some(_)) => Some(-1)
      case (Some(s), Some(o)) if s.compareByHighestCard(o) != 0 => Some(s.compareByHighestCard(o))
      case (Some(s), Some(o)) => Some((this ^ s).compareByHighestCard(that ^ o))
    }
  }

  //TODO replace by findCombination
  private def compareByHighestCard(that: Hand): Int = {
    var mask: Long = (1 to 3).foldLeft(1L << 15)((acc, _) => (acc << 16) | acc)
    var self = 0L
    var other = 0L
    while (self == other && mask == (mask >>> 1) << 1) {
      self = value & mask
      other = that.value & mask
      mask = mask >>> 1
    }
    self.compareTo(other)
  }
}

object Hand {
  def apply(value: Long): Hand = new Hand(value)

  val StraightFlushMask: Long = 0x7c00000000000000L
  val FourCardsMask: Long = 0x4000400040004000L
  val SuitMask: Long = 0x7fff000000000000L
  val StraightMask: Long = 0x7c007c007c007c00L

  val combinations: List[Hand => Option[Hand]] = List(
    findCombination(StraightFlushMask, 5, 1),
    findCombination(FourCardsMask, 4, 1),
    combine(findCombination(FourCardsMask, 3, 1), findCombination(FourCardsMask, 2, 1)),
    findCombination(SuitMask, 5, 16),
    findCombination(StraightMask, 5, 1),
    findCombination(FourCardsMask, 3, 1),
    combine(findCombination(FourCardsMask, 2, 1), findCombination(FourCardsMask, 2, 1)),
    findCombination(FourCardsMask, 2, 1)
  )

  def findCombination(mask: Long, minCards: Int, step: Int): Hand => Option[Hand] = h => {
    var m = mask
    var result: Option[Long] = None
    while (result.isEmpty && m == (m >>> step) << step) {
      m = m >> step
      if ((m & h.value).countBits >= minCards) result = Some(m)
    }
    result.map(Hand(_))
  }

  private def combine(left: Hand => Option[Hand], right: Hand => Option[Hand]): Hand => Option[Hand] =
    h => left(h) match {
      case Some(l) => right(h ^ l)
      case _ => None
    }
}

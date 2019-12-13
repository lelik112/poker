package net.cheltsov.poker

import net.cheltsov.poker.Converter._

case class Hand(value: Long) extends Ordered[Hand]{
  def | (that: Hand): Hand = {
    Hand(this.value | that.value)
  }

  def ^ (that: Hand): Hand = {
    Hand(this.value ^ that.value)
  }

  def size: Int = value.countBits

  lazy val suits: List[Suit] = Suit.suitByPosition.toList.map(p => p._2((value >>> (16 * p._1)).toInt))

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
    if (this.size != 5 || that.size != 5)
      throw new IllegalStateException("Hands with 5 cards can be compared only")
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

  private def compareByHighestCard(that: Hand): Int = {
    this.collapse - that.collapse
  }
}

object Hand {
  def apply(value: Long): Hand = new Hand(value)

  val StraightFlushMask: Long = 0x7c00000000000000L
  val FourCardsMask: Long = 0x4000400040004000L
  val SuitMask: Long = 0x7fff000000000000L
  val StraightMask: Long = 0x7c00L

  val combinations: List[Hand => Option[Hand]] = List(
    findCombination(StraightFlushMask, 5),
    findCombination(FourCardsMask, 4),
    combine(findCombination(FourCardsMask, 3), findCombination(FourCardsMask, 2)),
    findCombination(SuitMask, 5, 16),
    h => findCombination(StraightMask, 5)(Hand(h.collapse)),
    findCombination(FourCardsMask, 3),
    combine(findCombination(FourCardsMask, 2), findCombination(FourCardsMask, 2)),
    findCombination(FourCardsMask, 2)
  )

  def findCombination(mask: Long, minCards: Int, step: Int = 1): Hand => Option[Hand] = h => {
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

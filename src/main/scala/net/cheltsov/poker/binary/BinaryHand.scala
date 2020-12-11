package net.cheltsov.poker.binary

import net.cheltsov.poker.binary.Converter._
import net.cheltsov.poker.binary.BinaryHand._

class BinaryHand(override val value: Long) extends BinaryCards(value) with Ordered[BinaryHand] {
  require(size == 5)

  override def compare(that: BinaryHand): Int = {
    BinaryHand.Finders.sortBy(_._1).map(_._2).foldLeft[Option[Int]](None) {
      case (None, f)    => defineWinner(this, that, f)
      case (result, _)  => result
    }.getOrElse(this.compareByHeist(that))
  }

  @scala.annotation.tailrec
  private def defineWinner(left: BinaryCards, right: BinaryCards, finder: CombinationFinder): Option[Int] = {
    (finder(left), finder(right)) match {
      case (None, None)       => None
      case (Some(_), None)    => Some(1)
      case (None, Some(_))    => Some(-1)
      case (Some(l), Some(r)) => (finder, l.compareByHeist(r)) match {
        case (FullHouseFinder, _)   => defineWinner(left, right, ThreeOfKindFinder)
        case (_, 0)                 => Some((left - l).compareByHeist(right - r))
        case (_, res)               => Some(res)
      }
    }
  }
}

object BinaryHand {
  def apply(value: Long): BinaryHand = new BinaryHand(value)

  type CombinationFinder = BinaryCards => Option[BinaryCards]

  val LowestStraightFlushMask: Long = 0x_201e_0000_0000_0000L
  val StraightFlushMask: Long =       0x_3e00_0000_0000_0000L
  val FourCardsMask: Long =           0x_2000_2000_2000_2000L
  val SuitMask: Long =                0x_3ffe_0000_0000_0000L

  val ShortStep: Int =  1
  val LongStep: Int =   16

  val StraightFlushFinder: CombinationFinder        = findCombination(StraightFlushMask, 5)
  val LowestStraightFlushFinder: CombinationFinder  = findCombination(LowestStraightFlushMask, 5, LongStep)
  val FourOfKindFinder: CombinationFinder           = findCombination(FourCardsMask, 4)
  val FlushFinder: CombinationFinder                = findCombination(SuitMask, 5, LongStep)
  val StraightFinder: CombinationFinder             = ignoreSuitFinder(StraightFlushFinder)
  val LowestStraightFinder: CombinationFinder       = ignoreSuitFinder(LowestStraightFlushFinder)
  val PairFinder: CombinationFinder                 = findCombination(FourCardsMask, 2)
  val ThreeOfKindFinder: CombinationFinder          = findCombination(FourCardsMask, 3)

  val FullHouseFinder: CombinationFinder            = unionFinders(ThreeOfKindFinder, PairFinder)
  val TwoPairFinder: CombinationFinder              = unionFinders(PairFinder, PairFinder)

  val Finders: List[(Int, CombinationFinder)] = List(
    (1, StraightFlushFinder),
    (2, LowestStraightFlushFinder),
    (3, FourOfKindFinder),
    (4, FullHouseFinder),
    (5, FlushFinder),
    (6, StraightFinder),
    (7, LowestStraightFinder),
    (8, ThreeOfKindFinder),
    (9, TwoPairFinder),
    (10, PairFinder)
  )

  @scala.annotation.tailrec
  private def findCombination(mask: Long, amount: Int, step: Int = ShortStep)(cards: BinaryCards): Option[BinaryCards] = {
    val nextMask = mask >>> step
    val combination = BinaryCards(mask & cards.value)

    if (combination.size == amount)
      Some(combination)
    else if (nextMask << step == mask)
      findCombination(nextMask, amount, step)(cards)
    else
      None
  }

  private def unionFinders(higher: CombinationFinder, lower: CombinationFinder): CombinationFinder =
    combination => for {
      highCombination <- higher(combination)
      lowCombination <- lower(combination - highCombination)
    } yield highCombination + lowCombination

  private def ignoreSuitFinder(flushFinder: CombinationFinder): CombinationFinder =
    cards => flushFinder(BinaryCards(cards.value.collapseBitsToLastQuarter)).flatMap(_ => Some(cards))
}
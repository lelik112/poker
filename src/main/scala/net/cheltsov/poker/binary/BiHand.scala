package net.cheltsov.poker.binary

import net.cheltsov.poker.Hand
import net.cheltsov.poker.binary.BitUtil._
import net.cheltsov.poker.binary.BiHand._

/*
         15 14 13 12 11 10 09 08 07 06 05 04 03 02 01 00
        |  | A| K| D| J| T| 9| 8| 7| 6| 5| 4| 3| 2|  |  |
        |  | *|  |  |  |  |  |  |  |  | *| *| *| *|  |  |
              4     |     0     |     3     |     C       LowestStraight
        |  | *| *| *| *| *|  |  |  |  |  |  |  |  |  |  |
              7     |     C     |     0     |     0       Straight
        |  | *| *| *| *| *| *| *| *| *| *| *| *| *|  |  |
              7     |     F     |     F     |     C       Suit
        |  | *|  |  |  |  |  |  |  |  |  |  |  |  |  |  |
              4     |     0     |     0     |     0       FourCards
 */

class BiHand(override val cards: Long) extends BiCards(cards) with Hand[BiHand, BiCards] {

  override val FlushFinder: CombinationFinder                = findCombination(SuitMask, Hand.HandSize, LongStep)
  override val StraightFinder: CombinationFinder             = findStraight(StraightMask)
  override val LowestStraightFinder: CombinationFinder       = findStraight(LowestStraightMask)

  override def sameRankFinder(amount: Int): CombinationFinder = findCombination(FourCardsMask, amount, ShortStep)
}

object BiHand {
  def apply(cards: Long): BiHand = new BiHand(cards)
  def apply(biCards: BiCards): BiHand = new BiHand(biCards.cards)

  val LowestStraightMask: Long = 0x_0000_0000_0000_403CL
  val StraightMask: Long =       0x_0000_0000_0000_7C00L
  val SuitMask: Long =           0x_7ffC_0000_0000_0000L
  val FourCardsMask: Long =      0x_4000_4000_4000_4000L

  val ShortStep: Int =  1
  val LongStep: Int =   16

  @scala.annotation.tailrec
  private def findCombination(mask: Long, amount: Int, step: Int)(biCards: BiCards): Option[BiCards] = {
    val nextMask = mask >>> step
    val combination = BiCards(mask & biCards.cards)

    if (combination.size == amount)
      Some(combination)
    else if (nextMask << step == mask)
      findCombination(nextMask, amount, step)(biCards)
    else
      None
  }

  private def findStraight(mask: Long)(biCards: BiCards): Option[BiCards] = {
    val unaryCards = BiCards(biCards.cards.collapseBitsToRightQuarter)
    findCombination(mask, Hand.HandSize, ShortStep)(unaryCards).flatMap(_ => Some(biCards))
  }
}
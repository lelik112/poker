package net.cheltsov.poker

import net.cheltsov.poker.Hand._
import scala.collection.immutable.ListMap

trait Hand extends Cards with Ordered[Hand]{

  type Order = Int
  type CombinationFinder = Cards => Option[Cards]

  require(size == HandSize)

  lazy val StraightFlushFinder: CombinationFinder = combineFinders(FlushFinder, StraightFinder, (l, _) => l)
  lazy val LowestStraightFlushFinder: CombinationFinder =
                                                      combineFinders(FlushFinder, LowestStraightFinder, (l, _) => l)
  lazy val FourOfKindFinder: CombinationFinder = sameRankFinder(4)
  lazy val FullHouseFinder: CombinationFinder = combineFinders(ThreeOfKindFinder, PairFinder, _ - _)
  lazy val FlushFinder: CombinationFinder = findFlush
  lazy val StraightFinder: CombinationFinder = findStraight
  lazy val LowestStraightFinder: CombinationFinder = findLowestStraight
  lazy val ThreeOfKindFinder: CombinationFinder = sameRankFinder(3)
  lazy val TwoPairFinder: CombinationFinder = combineFinders(PairFinder, PairFinder, _ - _)
  lazy val PairFinder: CombinationFinder = sameRankFinder(2)

  protected def sameRankFinder(amount: Int): CombinationFinder

  private lazy val Finders: ListMap[Order, CombinationFinder] = ListMap(
    (StraightFlushOrder, StraightFlushFinder),
    (LowestStraightFlushOrder, LowestStraightFlushFinder),
    (FourOfKindOrder, FourOfKindFinder),
    (FullHouseOrder, FullHouseFinder),
    (FlushOrder, FlushFinder),
    (StraightFlushOrder, StraightFinder),
    (LowestStraightOrder, LowestStraightFinder),
    (ThreeOfKindOrder, ThreeOfKindFinder),
    (TwoPairOrder, TwoPairFinder),
    (PairFinderOrder, PairFinder)
  )

  override def compare(that: Hand): Int = {
    Finders.foldLeft[Option[Int]](None) {
      case (None,   finder) => defineWinner(that, finder._1)
      case (result, _)      => result
    }.getOrElse(this.compareByRank(that))
  }

  @scala.annotation.tailrec
  private def defineWinner(that: Hand, order: Order): Option[Int] = {
    val thisResult = this.Finders(order)(this)
    val thatResult = that.Finders(order)(that)

           (thisResult,      thatResult) match {
      case (None,            None)            => None
      case (Some(_),         None)            => Some(1)
      case (None,            Some(_))         => Some(-1)
      case (Some(thisCards), Some(thatCards)) =>

               (order,          thisCards.compareByRank(thatCards)) match {
          case (FullHouseOrder, _)      => defineWinner(that, ThreeOfKindOrder)
          case (_,              0)      => Some((this - thisCards).compareByRank(that - thatCards))
          case (_,              result) => Some(result)
      }
    }
  }

  private def findFlush(cards: Cards): Option[Cards] =
    cards.suits.groupBy(identity).find(_._2.size == HandSize) match {
      case Some(_)  => Some(cards)
      case _        => None
    }

  private def findStraight(cards: Cards): Option[Cards] =
    Option.when(Cards.Ranks.containsSlice(cards.ranks.sorted))(cards)

  private def findLowestStraight(cards: Cards): Option[Cards] =
    Option.when(cards.ranks.sorted.toSet.subsetOf(LowerStraightRanks))(cards)

  private def combineFinders(left: CombinationFinder,
                             right: CombinationFinder,
                             combiner: (Cards, Cards) => Cards): CombinationFinder =
    cards => for {
      leftCards  <- left(cards)
      rightCards <- right(combiner(cards, leftCards))
    } yield leftCards + rightCards
}

object Hand {
  val HandSize = 5
  val LowerStraightRanks = Set(2, 3, 4, 5, 14)

  val StraightFlushOrder = 1
  val LowestStraightFlushOrder = 2
  val FourOfKindOrder = 3
  val FullHouseOrder = 4
  val FlushOrder = 5
  val StraightOrder = 6
  val LowestStraightOrder = 7
  val ThreeOfKindOrder = 8
  val TwoPairOrder = 9
  val PairFinderOrder = 10
}
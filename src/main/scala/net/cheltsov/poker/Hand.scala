package net.cheltsov.poker

import net.cheltsov.poker.Hand._
import scala.collection.immutable.ListMap

trait Hand extends Cards with Ordered[Hand]{

  type Order = Int
  type Finder = Cards => Option[Cards]

  require(size == HandSize)

  private lazy val RegularStraightFlushFinder = combineFinders(FlushFinder, RegularStraightFinder, FlashCombiner)
  private lazy val LowestStraightFlushFinder  = combineFinders(FlushFinder, LowestStraightFinder, FlashCombiner)
  private lazy val FourOfKindFinder           = findSameRankCards(4) _
  private lazy val FullHouseFinder            = combineFinders(ThreeOfKindFinder, PairFinder, RanksCombiner)
  private lazy val FlushFinder                = findFlush _
  private lazy val RegularStraightFinder      = findRegularStraight _
  private lazy val LowestStraightFinder       = findLowestStraight _
  private lazy val ThreeOfKindFinder          = findSameRankCards(3) _
  private lazy val TwoPairFinder              = combineFinders(PairFinder, PairFinder, RanksCombiner)
  private lazy val PairFinder                 = findSameRankCards(2) _

  protected def findSameRankCards(amount: Int)(cards: Cards): Option[Cards] =
    cards.combineCards(1)
      .groupMapReduce(_.ranks.head)(identity)(_ + _)
      .filter(_._2.size == amount)
      .values
      .headOption

  protected def findFlush(cards: Cards): Option[Cards] =
    cards.suits.groupBy(identity).find(_._2.size == HandSize).flatMap(_ => Some(cards))

  protected def findRegularStraight(cards: Cards): Option[Cards] =
    Option.when(Cards.Ranks.containsSlice(cards.ranks.sorted))(cards)

  protected def findLowestStraight(cards: Cards): Option[Cards] =
    Option.when(LowerStraightRanks.subsetOf(cards.ranks.sorted.toSet))(cards)

  private def combineFinders(left: Finder, right: Finder, combiner: (Cards, Cards) => Cards): Finder =
    cards => for {
      leftCards  <- left(cards)
      rightCards <- right(combiner(cards, leftCards))
    } yield leftCards + rightCards

  private lazy val Finders: ListMap[Order, Finder] = ListMap(
    StraightFlushOrder       -> RegularStraightFlushFinder,
    LowestStraightFlushOrder -> LowestStraightFlushFinder,
    FourOfKindOrder          -> FourOfKindFinder,
    FullHouseOrder           -> FullHouseFinder,
    FlushOrder               -> FlushFinder,
    StraightFlushOrder       -> RegularStraightFinder,
    LowestStraightOrder      -> LowestStraightFinder,
    ThreeOfKindOrder         -> ThreeOfKindFinder,
    TwoPairOrder             -> TwoPairFinder,
    PairFinderOrder          -> PairFinder
  )

  override def compare(that: Hand): Int = {
    Finders.foldLeft[Option[Int]](None) {
      case (None,   finder) => defineWinner(that, finder._1)
      case (result, _)      => result
    }.getOrElse(byRank(this, that))
  }

  @scala.annotation.tailrec
  private def defineWinner(that: Hand, order: Order): Option[Int] = {
    val thisCards = this.Finders(order)(this)
    val thatCards = that.Finders(order)(that)

           (thisCards, thatCards) match {
      case (None     , None     ) => None
      case (Some(_)  , None     ) => Some(LeftWinner)
      case (None     , Some(_)  ) => Some(RightWinner)
      case (Some(l)  , Some(r)  ) =>

               (order         , byRank(l, r)) match {
          case (FullHouseOrder, _           ) => defineWinner(that, ThreeOfKindOrder)
          case (_             , Draw        ) => Some(byRank(this - l, that - r))
          case (_             , result      ) => Some(result)
      }
    }
  }

  private def byRank(left: Cards, right: Cards): Int =
    left.compareByRank(right) match {
      case r if r > Draw => LeftWinner
      case r if r < Draw => RightWinner
      case _             => Draw
    }
}

object Hand {
  val HandSize = 5
  val LowerStraightRanks = Set(2, 3, 4, 5, 14)

  val Draw        =  0
  val LeftWinner  =  1
  val RightWinner = -1

  val StraightFlushOrder       = 1
  val LowestStraightFlushOrder = 2
  val FourOfKindOrder          = 3
  val FullHouseOrder           = 4
  val FlushOrder               = 5
  val StraightOrder            = 6
  val LowestStraightOrder      = 7
  val ThreeOfKindOrder         = 8
  val TwoPairOrder             = 9
  val PairFinderOrder          = 10

  val FlashCombiner: (Cards, Cards) => Cards = (allCards, _)         => allCards
  val RanksCombiner: (Cards, Cards) => Cards = (allCards, leftCards) => allCards - leftCards
}
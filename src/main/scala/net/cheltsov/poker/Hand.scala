package net.cheltsov.poker

trait Hand[H <: Hand[H, C], C <: Cards[C]] extends Cards[C] with Ordered[H]{

  require(size == Hand.HandSize)

  lazy val StraightFlushFinder: CombinationFinder = combineFiveCardsFinders(FlushFinder, StraightFinder)
  lazy val LowestStraightFlushFinder: CombinationFinder = combineFiveCardsFinders(FlushFinder, LowestStraightFinder)
  lazy val FourOfKindFinder: CombinationFinder = sameRankFinder(4)
  lazy val FullHouseFinder: CombinationFinder = unionFinders(ThreeOfKindFinder, PairFinder)
  val FlushFinder: CombinationFinder
  val StraightFinder: CombinationFinder
  val LowestStraightFinder: CombinationFinder
  lazy val ThreeOfKindFinder: CombinationFinder = sameRankFinder(3)
  lazy val TwoPairFinder: CombinationFinder = unionFinders(PairFinder, PairFinder)
  lazy val PairFinder: CombinationFinder = sameRankFinder(2)

  def sameRankFinder(amount: Int): CombinationFinder

  lazy val Finders: List[CombinationFinder] = List(
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
  ).sortBy(_._1).map(_._2)

  override def compare(that: H): Int = {
    Finders.foldLeft[Option[Int]](None) {
      case (None, f)    => defineWinner(this.asInstanceOf[C], that.asInstanceOf[C], f)
      case (result, _)  => result
    }.getOrElse(this.compareByRank(that.asInstanceOf[C]))
  }

  @scala.annotation.tailrec
  private def defineWinner(left: C, right: C, finder: CombinationFinder): Option[Int] = {
            (finder(left),  finder(right)) match {
      case  (None,          None)       => None
      case  (Some(_),       None)       => Some(1)
      case  (None,          Some(_))    => Some(-1)
      case  (Some(l),       Some(r))    => (finder,           l.compareByRank(r)) match {
                                      case (FullHouseFinder,  _)        => defineWinner(left, right, ThreeOfKindFinder)
                                      case (_,                0)        => Some((left - l).compareByRank(right - r))
                                      case (_,                result)   => Some(result)
      }
    }
  }

  private def unionFinders(higher: CombinationFinder, lower: CombinationFinder): CombinationFinder =
    combination => for {
      highCombination <- higher(combination)
      lowCombination <- lower(combination - highCombination)
    } yield highCombination + lowCombination

  private def combineFiveCardsFinders(left: CombinationFinder, right: CombinationFinder): CombinationFinder =
    combination => for {
      _ <- left(combination)
      _ <- right(combination)
    } yield combination
}

object Hand {
  val HandSize: Int = 5
}
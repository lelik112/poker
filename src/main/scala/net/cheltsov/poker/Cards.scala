package net.cheltsov.poker

import net.cheltsov.poker.Cards.Rank

trait Cards extends {

  def +(that: Cards): Cards
  def -(that: Cards): Cards
  def combineCards(n: Int): List[Cards]
  val asPairs: Set[(Rank, Suit)]

  lazy val size:  Int        = asPairs.size
  lazy val ranks: List[Rank] = asPairs.toList.map(_._1)
  lazy val suits: List[Suit] = asPairs.toList.map(_._2)

  def compareByRank(that: Cards): Int =
    (this.ranks.sorted.reverse zip that.ranks.sorted.reverse).foldLeft(0) {
      case (0,   p) => p._1 - p._2
      case (res, _) => res
    }
}

object Cards {

  type Rank = Int

  val Ranks: Range = 2 to 14

  def toRank(value: String): Int = {
    value match {
      case v if "[2-9]".r matches v => v.toInt
      case "T" | "t" => 10
      case "J" | "j" => 11
      case "Q" | "q" => 12
      case "K" | "k" => 13
      case "A" | "a" => 14
      case _ => throw new IllegalStateException(s"$value is not a valid card rank")
    }
  }

  def fromRank(value: Int): String = {
    value match {
      case v if (2 to 9).contains(v) => v.toString
      case 10 => "T"
      case 11 => "J"
      case 12 => "Q"
      case 13 => "K"
      case 14 => "A"
      case _ => throw new IllegalStateException(s"$value is not a valid card rank")
    }
  }
}

sealed abstract class Suit(val name: String) {
  override def toString: String = name
}

case object Spades    extends Suit(Suit.SpadesName)
case object Hearts    extends Suit(Suit.HeartsName)
case object Diamonds  extends Suit(Suit.DiamondsName)
case object Clubs     extends Suit(Suit.ClubsName)

object Suit{
  val SpadesName: String = "s"
  val HeartsName: String = "h"
  val DiamondsName: String = "d"
  val ClubsName: String = "c"

  def apply(name: String): Suit = name match {
    case SpadesName   => Spades
    case HeartsName   => Hearts
    case DiamondsName => Diamonds
    case ClubsName    => Clubs
  }
}

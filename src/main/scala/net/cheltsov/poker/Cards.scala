package net.cheltsov.poker

trait Cards[C <: Cards[C]] {

  type CombinationFinder = C => Option[C]

  def +(that: C): C
  def -(that: C): C
  val size: Int
  def compareByRank(that: C): Int
}

object Cards {
  val SpadesName: String = "s"
  val HeartsName: String = "h"
  val DiamondsName: String = "d"
  val ClubsName: String = "c"

  val Ranks: Range = 2 to 14

  def toRank(value: String): Int = {
    value match {
      case v if "[2-9]".r matches v => v.toInt
      case "T" => 10
      case "J" => 11
      case "Q" => 12
      case "K" => 13
      case "A" => 14
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

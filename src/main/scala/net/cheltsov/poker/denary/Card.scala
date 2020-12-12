package net.cheltsov.poker.denary

import net.cheltsov.poker.Cards._

sealed abstract class Card(val name: String) {
  val rank: Int

  override def toString: String = fromRank(rank) + name
}

case class Spades(rank: Int) extends Card(SpadesName)
case class Hearts(rank: Int) extends Card(HeartsName)
case class Diamonds(rank: Int) extends Card(DiamondsName)
case class Clubs(rank: Int) extends Card(ClubsName)

object Card {
  def apply(r: Char, suit: Char): Card = {
    val rank = toRank(r.toString)
    suit.toString match {
      case SpadesName => Spades(rank)
      case HeartsName => Hearts(rank)
      case DiamondsName => Diamonds(rank)
      case ClubsName => Clubs(rank)
    }
  }
}

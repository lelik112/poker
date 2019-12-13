package net.cheltsov.poker

import net.cheltsov.poker.Suit._

sealed abstract class Suit(val position: Int, val name: String) {
  val value: Int
}

case class Spades(value: Int) extends Suit(positions(SpadesName), SpadesName)
case class Hearts(value: Int) extends Suit(positions(HeartsName), HeartsName)
case class Diamonds(value: Int) extends Suit(positions(DiamondsName), DiamondsName)
case class Clubs(value: Int) extends Suit(positions(ClubsName), ClubsName)

object Suit {
  val SpadesName: String = "s"
  val HeartsName: String = "h"
  val DiamondsName: String = "d"
  val ClubsName: String = "c"
  val positions: Map[String, Int] = Map(SpadesName -> 0, HeartsName -> 1, DiamondsName -> 2, ClubsName -> 3)
  val suitByPosition: Map[Int, Int => Suit] = Map(0 -> Spades, 1 -> Hearts, 2 -> Diamonds, 3 -> Clubs)
  val Mask: Long = 0xFFFF
}







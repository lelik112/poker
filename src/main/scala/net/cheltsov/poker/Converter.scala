package net.cheltsov.poker

import net.cheltsov.poker.Suit._

object Converter {

  implicit final class IntToStringOps(val value: Int) extends AnyVal {
    def toStringRepresentation: String = value match {
      case v if (2 to 9).contains(v) => v.toString
      case 10 => "T"
      case 11 => "J"
      case 12 => "Q"
      case 13 => "K"
      case 14 => "A"
      case _ => throw new IllegalStateException(s"$value is not a valid card rang")
    }
  }

  implicit final class StringToIntOps(val value: String) extends AnyVal {
    def toIntRepresentation: Int = value match {
      case v if "[2-9]".r matches v => v.toInt
      case "T" => 10
      case "J" => 11
      case "Q" => 12
      case "K" => 13
      case "A" => 14
      case _ => throw new IllegalStateException(s"$value is not a valid card rang")
    }
  }

  implicit final class HandToSuitOps(val hand: Hand) extends AnyVal {
    def toSuits: List[Suit] = {
      suitByPosition.toList.map(p => p._2((hand.value >>> (16 * p._1)).toInt))
    }
  }

  implicit final class SuitToHandOps(val suit: Suit) extends AnyVal {
    def toHand: Hand = {
      Hand(suit.value.toLong << suit.position)
    }
  }

  implicit final class StringToHandOps(val value: String) extends AnyVal {
    def toHand: Hand = {
      @scala.annotation.tailrec
      def parse(chars: List[String], hand: Hand): Hand = chars match {
        case Nil => hand
        case r :: s :: xs => parse(xs, hand | Hand(1L << (r.toIntRepresentation + positions(s) * 16)))
      }
      parse(value.split("").toList, Hand(0))
    }
  }

  implicit final class LongToCountBitsOps(val value: Long) extends AnyVal {
    def countBits: Int = {
      @scala.annotation.tailrec
      def bits(i: Long, acc: Int): Int = if (i == 0) acc else bits (i >> 1, (1 & i).toInt + acc)
      bits(value, 0)
    }
  }

}

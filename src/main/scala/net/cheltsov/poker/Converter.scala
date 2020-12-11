package net.cheltsov.poker

import net.cheltsov.poker.Suit._
import net.cheltsov.poker.binary.BinaryHand

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

  implicit final class CollapseHandOps(val hand: BinaryHand) extends AnyVal {
    def collapse: Int = {
      hand.suits.foldLeft(0)(_ | _.value)
    }
  }

  implicit final class SuitToHandOps(val suit: Suit) extends AnyVal {
    def toHand: BinaryHand = {
      BinaryHand(suit.value.toLong << suit.position)
    }
  }

  implicit final class StringToHandOps(val value: String) extends AnyVal {
    def toHands: List[BinaryHand] = {
      def parse(chars: List[String], hands: List[BinaryHand]): List[BinaryHand] = chars match {
        case Nil => hands
        case r :: s :: xs => BinaryHand(1L << (r.toIntRepresentation + positions(s) * 16)) :: parse(xs, hands)
      }

      parse(value.split("").toList, Nil)
    }
  }



}

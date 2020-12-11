package net.cheltsov.poker

import net.cheltsov.poker.Converter._
import net.cheltsov.poker.Validator._
import net.cheltsov.poker.binary.BinaryHand

object Parser {
  def pars(input: String, isOmaha: Boolean = false): Either[String, List[List[BinaryHand]]] = {
    val handCardsSize = if (isOmaha) 4 else 2
    val inputArray = input.split("\\s")

    (isValidInput(input, handCardsSize), inputArray) match {
      case (false, _) =>
        Left(s"Input is not valid: $input")
      case (_, a) =>
        val hands = a.tail.foldLeft(List(a.head.toHands))((l: List[List[BinaryHand]], c: String) => c.toHands :: l).reverse
        if (areCardsUnique(hands.flatten))
          Right(hands)
        else
          Left(s"There are some not unique cards: $input")
    }
  }
}

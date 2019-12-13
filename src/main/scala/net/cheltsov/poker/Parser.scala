package net.cheltsov.poker

import net.cheltsov.poker.Converter._
import net.cheltsov.poker.Validator._

object Parser {
  def pars(input: String, isTexasHoldem: Boolean = true): Either[String, List[List[Hand]]] = {
    val handCardsSize = if (isTexasHoldem) 2 else 4
    val inputArray = input.split("\\s")

    (isValidInput(input, handCardsSize), inputArray) match {
      case (false, _) =>
        Left(s"Input is not valid: $input")
      case (_, a) if isNotValidHandsNumber(a(1).toInt, a.size - 2) =>
        Left(s"Wrong amount of hands: ${a(1)}")
      case (_, a) =>
        val hands = a.slice(2, a(1).toInt + 2)
          .foldLeft(List(a(0).toHands)) ((l: List[List[Hand]], c: String) => c.toHands :: l).reverse
        if (areCardsUnique(hands.flatten))
          Right(hands)
        else
          Left(s"There are some not unique cards: $input")
    }
  }
}

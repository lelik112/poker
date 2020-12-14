package net.cheltsov.poker

import net.cheltsov.poker.Validator._
import net.cheltsov.poker.binary.BiCards

object Parser {

  def pars(input: String): Either[String, (String, List[(BiCards, String)])] = {

    val gameName :: gameHands = input.split(BlankRegex).toList

    if (input.isEmpty || input.isBlank)
      Left("Input is empty")
    else if (!Solver.SupportedGames.contains(gameName))
      Left("Unrecognized game type")
    else if (!isValidInput(input))
      Left("Invalid input")
    else
      Right((gameName, gameHands.map(BiCards(_)) zip gameHands))
        .filterOrElse(p => areCardsUnique(p._2.map(_._1)), "Cards are not unique")
  }
}

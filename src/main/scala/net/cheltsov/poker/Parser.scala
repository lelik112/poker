package net.cheltsov.poker

import net.cheltsov.poker.Solver._
import net.cheltsov.poker.Validator._
import net.cheltsov.poker.binary.BiCards

object Parser {

  def pars(input: String): Either[String, (String, List[BiCards])] = {

    val inputList: List[String] = input.split(BlankRegex).toList
    val gameName: String = inputList.head

    if (inputList.isEmpty)
      Left(s"$ErrorPrefix Input is empty")
    else if (!Solver.SupportedGames.contains(gameName))
      Left(s"$ErrorPrefix Unrecognized game type")
    else if (!isValidInput(input))
      Left(s"$ErrorPrefix Invalid input")
    else
      Right((gameName, inputList.tail.map(BiCards(_))))
        .filterOrElse(p => areCardsUnique(p._2), s"$ErrorPrefix Cards are not unique")
  }
}

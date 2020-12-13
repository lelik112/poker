package net.cheltsov.poker

object Solver {
  val TexasHoldEm: String = "texas-holdem"
  val OmahaHoldEm: String = "omaha-holdem"
  val FiveCardDraw: String = "five-card-draw"
  val SupportedGames: List[String] = List(TexasHoldEm, OmahaHoldEm, FiveCardDraw)
  val ErrorPrefix = "Error:"

  def process(line: String): String = {
    Parser.pars(line).toString
  }

}

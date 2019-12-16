package net.cheltsov.poker

object Validator {
  val CardRegex: String = "([AKQJT3-9][hdcs])"
  val MaxHandNumber: Int = 256 //Why? We have 48 cards in a deck

  def isValidInput(input: String, handCardsSize: Int): Boolean = {
    val regex = s"$CardRegex{5}\\s+[0-9]{1,3}(\\s+$CardRegex{$handCardsSize})+".r
    regex matches input
  }

  def isNotValidHandsNumber(declared: Int, actual: Int): Boolean =
    declared > actual || !(1 to MaxHandNumber).contains(declared)

  def areCardsUnique(hands: List[Hand]): Boolean = {
    val (acc, size) = hands.foldLeft((Hand(0), 0))((p, h) => (p._1 + h, p._2 + h.size))
    acc.size == size
  }
}

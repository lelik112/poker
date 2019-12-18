package net.cheltsov.poker

object Validator {
  val CardRegex: String = "([AKQJT3-9][hdcs])"

  def isValidInput(input: String, handCardsSize: Int): Boolean = {
    val regex = s"$CardRegex{5}(\\s+$CardRegex{$handCardsSize})+".r
    regex matches input
  }

  def areCardsUnique(hands: List[Hand]): Boolean = {
    val (acc, size) = hands.foldLeft((Hand(0), 0))((p, h) => (p._1 + h, p._2 + h.size))
    acc.size == size
  }
}

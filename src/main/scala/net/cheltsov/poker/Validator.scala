package net.cheltsov.poker

import net.cheltsov.poker.binary.BinaryHand

object Validator {
  val CardRegex: String = "([AKQJT2-9][hdcs])"

  def isValidInput(input: String, handCardsSize: Int): Boolean = {
    val regex = s"$CardRegex{5}(\\s+$CardRegex{$handCardsSize})+".r
    regex matches input
  }

  def areCardsUnique(hands: List[BinaryHand]): Boolean = {
    val (acc, size) = hands.foldLeft((BinaryHand(0), 0))((p, h) => (p._1 + h, p._2 + h.size))
    acc.size == size
  }
}

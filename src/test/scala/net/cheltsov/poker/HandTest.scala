package net.cheltsov.poker

import net.cheltsov.poker.Converter._
import net.cheltsov.poker.binary.BinaryHand
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

class HandTest extends AnyFlatSpec {

  for (_ <- 0 to 100) {
    val randomLongs: List[Long] = HandTest.randomHands
    val first: List[BinaryHand] = randomLongs.map(BinaryHand(_)).sorted.reverse
    println(first)
  }

}

object HandTest {

  def randomHands: List[Long] = {
    val size = Random.nextInt(6) + 2
    var result: List[Long] = Nil
    while (result.size != size) {
      val nextHand = randomHand
      val currentCards = result.foldLeft(0L)(_ | _)
      if ((nextHand | currentCards).countBits - currentCards.countBits == 5) {
        result = nextHand :: result
      }
    }
    result
  }

  def randomHand: Long = {
    var value = 0l

    while (value.countBits != 5) {
      value = value | randomCard
    }
    value
  }

  def randomCard: Long = randomRang << (randomSuit * 16)
  def randomRang: Long = 2L << Random.nextInt(13)
  def randomSuit: Int = Random.nextInt(4)
}

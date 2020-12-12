package net.cheltsov.poker

import java.time.LocalTime

import net.cheltsov.poker.binary.BitUtil._
import net.cheltsov.poker.binary.{BiCards, BiHand}
import net.cheltsov.poker.denary.{DeCards, DeHand}
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

class HandTest extends AnyFlatSpec {

  var inputs: Set[Set[String]] = Set(Set())

  for (_ <- 0 to 500_000) {
    val randomLong: List[Long] = HandTest.randomHands
    val strings: Set[String] = randomLong.map(BiHand(_).toString).toSet
    inputs = inputs + strings
  }
  println(LocalTime.now())
  inputs.foreach{input =>
    input.map(i => BiHand(BiCards(i))).toList.sorted.reverse
  }
  println(LocalTime.now())
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
  def randomRang: Long = 2L << (Random.nextInt(12) + 2)
  def randomSuit: Int = Random.nextInt(4)
}

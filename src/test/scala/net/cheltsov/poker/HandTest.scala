package net.cheltsov.poker

import java.time.LocalTime

import net.cheltsov.poker.binary.BitUtil._
import net.cheltsov.poker.binary.{BiCards, BiHand}
import net.cheltsov.poker.denary.{DeCards, DeHand}
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

class HandTest extends AnyFlatSpec {

  var inputs: List[List[String]] = Nil
//  var inputsH: Set[List[BiHand]] = Set()
  var inputsH: Set[List[DeHand]] = Set()

  for (_ <- 0 to 1000_000) {
    val randomLong: List[Long] = HandTest.randomHands
//    val hands: List[BiHand] = randomLong.map(BiHand(_))
    val hands: List[DeHand] = randomLong.map(v => DeHand(DeCards(BiCards(v).toString)))
    inputsH = inputsH + hands
  }
  println(LocalTime.now())
  inputsH.foreach { input =>
    input.sorted.reverse
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

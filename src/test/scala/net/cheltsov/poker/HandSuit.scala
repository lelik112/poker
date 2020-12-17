package net.cheltsov.poker

import net.cheltsov.poker.binary.{BiCards, BiHand}
import net.cheltsov.poker.denary.{DeCards, DeHand}
import org.scalacheck.{Gen, Prop}
import org.scalactic.anyvals.PosInt
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.Checkers

import scala.util.Random

class HandSuit extends AnyPropSpec with Checkers {

  val MinSuccessfulTests: PosInt = 966_966
  val MaxSameOrderRatio:  Double = 0.0_966
  val MinSameOrderRatio:  Double = 0.00966

  val cardGen: Gen[String] = for {
    suitGen <- Gen.oneOf("s", "h", "d", "c")
    genRank <- Gen.oneOf(2 to 14).map(Cards.fromRank)
  } yield genRank + suitGen

  val handGen: Gen[String] = Gen.containerOfN[Set, String](5, cardGen).suchThat(_.size == 5).map(_.toList.mkString(""))

  val similarHandsGen: Gen[(String, String)] = for {
    hand <- handGen
    card <- cardGen suchThat(!hand.contains(_))
  } yield (hand, card + hand.tail.tail)

  def shuffleCards(cards: String): String = Random.shuffle(cards.grouped(2)).mkString

  property("BiHand and DeHand that created from the same cards must be in the same order") {
    check(
      Prop.forAll(handGen) { hand: String =>

        val biHand: BiHand = BiHand(BiCards(hand))
        val deHand: DeHand = DeHand(DeCards(shuffleCards(hand)))

          biHand.compare(deHand) == 0
      }
    , MinSuccessful(MinSuccessfulTests))
  }

  var notChangedOrderCounter = 0

  property("BiHand and DeHand that differ only on one card must be NOT unordered") {
    check(
      Prop.forAll(similarHandsGen) { case (original, changed) =>

        val biHand: BiHand = BiHand(BiCards(original))
        val deHand: DeHand = DeHand(DeCards(changed))
        val leftResult  = biHand.compare(deHand)
        val rightResult = deHand.compare(biHand)

        notChangedOrderCounter = leftResult match {
          case 0 => notChangedOrderCounter + 1
          case _ => notChangedOrderCounter
        }

        leftResult + rightResult == 0
      }
      , MinSuccessful(MinSuccessfulTests))

    check {
      val sameOrderRatio = 1.0 * notChangedOrderCounter / MinSuccessfulTests
      sameOrderRatio > MinSameOrderRatio && sameOrderRatio < MaxSameOrderRatio
    }
  }

  property("BiHand and DeHand that totally differ must be NOT unordered") {
    check(
      Prop.forAll(handGen, handGen) { (left, right) =>

        val biHand: BiHand = BiHand(BiCards(left))
        val deHand: DeHand = DeHand(DeCards(right))
        val leftResult  = biHand.compare(deHand)
        val rightResult = deHand.compare(biHand)

        leftResult + rightResult == 0
      }
      , MinSuccessful(MinSuccessfulTests))
  }
}


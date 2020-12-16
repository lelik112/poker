package net.cheltsov.poker

import java.time.LocalTime

import net.cheltsov.poker.binary.{BiCards, BiHand}
import net.cheltsov.poker.denary.{DeCards, DeHand}
import org.scalacheck.{Arbitrary, Gen, Prop}
import org.scalatest.matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.Checkers

class HandSuit extends AnyPropSpec with TableDrivenPropertyChecks with Checkers{

  val cardGen: Gen[String] = for {
    suitGen <- Gen.oneOf("s", "h", "d", "c")
    genRank <- Gen.oneOf(2 to 14).map(Cards.fromRank)
  } yield genRank + suitGen

  val handGen: Gen[String] = Gen.containerOfN[Set, String](5, cardGen).map(_.toList.mkString(""))
  val similarHandsGen: Gen[(String, String)] = Gen.containerOfN[Set, String](6, cardGen).map(_.toList).map {
    case h :: xs => (xs.mkString(""), (h :: xs.tail).mkString(""))
  }

//  implicit lazy val handsArbitrary: Arbitrary[Hand] = Arbitrary(handGen.map(s => BiHand(BiCards(s)).asInstanceOf[Hand]))
//  implicit lazy val similarHandsArbitrary = Arbitrary(similarHandsGen)


//  property("Interval are generated correctly") {
//    check{
//      (interval: (Int, Int)) =>
//        val (i, j) = interval
//        (i >= 0) && (j >= i) && (supremum >= j) && (maximalLength > j-i)
//    }
//  }

//  property("****************ssssssssssss") {
//    check {
//      Prop.forAll { hands: Int =>
//        println(hands)
//        hands == (hands + 1)
//      }
//    }
//  }

  property("****************ssssssssssss") {
    check {
      Prop.forAll { hands: Int =>
        println(hands)
        hands == (hands + 1)
      }
    }
  }



  println(handGen.sample)

//
//  var inputs: List[List[String]] = Nil
////  var inputsH: Set[List[BiHand]] = Set()
//  var inputsH: Set[List[DeHand]] = Set()
//
//  for (_ <- 0 to 10_000) {
//    val randomLong: List[Hand] = CardsGen.randomBiHands
////    val hands: List[BiHand] = randomLong.map(BiHand(_))
//    val hands: List[DeHand] = randomLong.map(v => DeHand(DeCards(BiCards(v).toString)))
//    inputsH = inputsH + hands
//  }
//  println(LocalTime.now())
//  inputsH.foreach { input =>
//    input.sorted.reverse
//  }
//  println(LocalTime.now())
}


package net.cheltsov.poker



object Main extends App{
  val vS1: String = "0000000000000000_0000000000000000_0000000000000000_0000000000000100".replaceAll("_", "")
  val vS2: String = "0001000000000000_0000000000000000_0000000000000000_0000000000000000".replaceAll("_", "")
  val vL1: Long = java.lang.Long.parseLong(vS1, 2)
  val vL2: Long = java.lang.Long.parseLong(vS2, 2)
//  Hand.findCombination(Hand.FourCardsMask, 4)(Hand(vL1))
  println(Hand(vL1).compare(Hand(vL2)))

//  val fourMask = "0100000000000000_0100000000000000_0100000000000000_0100000000000000".replaceAll("_", "")
//  val fourMaskLong = java.lang.Long.parseLong(fourMask, 2)
//  println(Hand(fourMaskLong))
//  println(java.lang.Long.toHexString(fourMaskLong))
//  println(java.lang.Long.toHexString((1 to 3).foldLeft(1L << 14)((acc, _) => (acc << 16) | acc)))
//
//  val flushMask = "0111110000000000_0000000000000000_0000000000000000_0000000000000000".replaceAll("_", "")
//  val flushMaskLong = java.lang.Long.parseLong(flushMask, 2)
//  println(Hand(flushMaskLong))
//  println(java.lang.Long.toHexString(flushMaskLong))
//  println(java.lang.Long.toHexString(((1L << 5) - 1) << 58))

//    val flushMask = "0111111111111110_0000000000000000_0000000000000000_0000000000000000".replaceAll("_", "")
//    val flushMaskLong = java.lang.Long.parseLong(flushMask, 2)
//    println(Hand(flushMaskLong))
//    println(java.lang.Long.toHexString(flushMaskLong))
//    println(java.lang.Long.toHexString(0xFFFF000000000000L))
//
//  val straightMask = "0111110000000000_0111110000000000_0111110000000000_0111110000000000".replaceAll("_", "")
//  val straightMaskLong = java.lang.Long.parseLong(straightMask, 2)
//  println(Hand(straightMaskLong))
//  println(java.lang.Long.toHexString(straightMaskLong))
//  println(java.lang.Long.toHexString(0xFFFF000000000000L))



//  println(List(Hand(vL1), Hand(vL2)).sorted)

//  val o = List(1, 2)

//  println(o.foldLeft(0)((_, b) => b))
//  Parser.pars("4cKs4h8s7s 5 Ad4s Ac4d As9s KhKd 5d6d") match {
//    case Right(value) => println(value)
//    case Left(message) => println(message)
//  }

//  println(java.lang.Long.toBinaryString((1 to 3).foldLeft(1L << 15)((acc, _) => (acc << 16) | acc)))

//  val CardRegex: String = "[AKQJT3-9][hdcs]"
//
//  val r = s"$CardRegex\\s+1".r
//  println(r matches "Ah 2")
//  println("sdsda".split("").toList)


}

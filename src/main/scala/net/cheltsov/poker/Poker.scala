package net.cheltsov.poker

object Poker extends App{
  val input: String = "4cKs4h8s7s 5 Ad4s Ac4d As9s KhKd 5d6d"
  val input2: String = "4cKs4h8s7s 2 Ad4sAc4d As9sKhKd"

  Parser.pars(input2, false) match {
    case Left(m) => println(m)
    case Right(value) => {
      println(value.head.combinations(3).toList)
    }
  }
}

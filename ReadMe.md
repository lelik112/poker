This is app for comparing the strength of Texas Hold'em or Omaha hold'em Hands
All options from the task were implemented

For starting:

Run sbt from project folder (https://www.scala-sbt.org/1.x/docs/Running.html)

Then:
* Type 'compile' for compilation or
* Type 'run' for running Texas Hold'em hands or
* Type 'run --omaha' for running Omaha hold'em hands

******************************************************************

The task:
Your task is to develop an algorithm for comparing the strength of Texas Hold'em Hands. A value of a Texas Hold'em Hand is the best possible value out of all possible subsets of 5 cards from the 7 cards which are formed by 5 board cards and 2 hand cards.

The details of Texas Hold'em hand values are available on Wikipedia: https://en.wikipedia.org/wiki/Texas_hold_%27em#The_showdown, in particular, from weakest to strongest:

·       High card - the "fallback" in case no other hand value rule applies

·       Pair - two cards of the same value

·       Two pairs - Two times two cards with the same value

·       Three of a kind - Three cards with the same value

·       Straight - Sequence of 5 cards in increasing value (Ace can precede 2 and follow up King)

·       Flush - 5 cards of the same suit

·       Full House - Combination of three of a kind and a pair

·       Four of a kind - Four cards of the same value

·       Straight Flush - straight of the same suit

In case of ties the ranks of the cards forming the combinations decide the highest value. In case of further ties, the ranks of remaining cards (in the order of the rank) decide the highest value. All suits are considered equal in strength.

The input is to be read from the standard input in the form of:

<5 board cards> <hand 1> <hand 2> <...> <hand N>

... where: 

·       <5 board cards> is a 10 character string where each 2 characters encode a card 

·       <hand X> is a 4 character string where each 2 characters encode a card, with 2 cards per hand

·       <card> is a 2 character string with the first character representing the rank (one of "A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2") and the second character representing the suit (one of "h", "d", "c", "s") .

The output is to be written to standard output using the format:

<hand block 1> <hand block 2> <...> <hand block n>

... where:

·       <hand block 1> is the hand block with the weakest value

·       <hand block 2> is the hand block with the second weakest value

·       ... and so forth.

·       <hand block n> is the hand block with the strongest value

·       Each hand block consists of one or multiple hands (each represented by 4 character string with 2 characters to encode a card, with 2 cards per hand) with equal strength.

·       In case there are multiple hands with the same value on the same board they should be ordered alphabetically and separated by "=" signs,

For example:

Input:

4cKs4h8s7s Ad4s Ac4d As9s KhKd 5d6d

 

Output:

Ac4d=Ad4s 5d6d As9s KhKd

Your code should read from standard input until EOF is reached, responding with each input line with exactly one output line. 

The homework can be written in any programming language which can be run on Ubuntu Linux and should include a "ReadMe.md" file with documentation on how to compile and run it. The documentation should be in English.

For extra credit you should:

§  Implement the algorithm using Scala, and/or

§  Implement a command line switch `--omaha` which uses 8 character strings representing 4 cards for each hand and evaluating hand values according to https://en.wikipedia.org/wiki/Omaha_hold_%27em rules,

§  In case the input line is invalid, output a clear & easy to understand error message.

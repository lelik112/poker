## Poker Hand Strength Evaluator

Algorithm for sorting poker hands according to their strength

## Implementation

There are two implementations on the solution:
 - Hands are represented as Set of Cards
 - Hands are represented as a single value of Long

#### Run on a local machine

`sbt run` or `sbt run -b`   runs the "binary" version 
`sbt run -d`                runs the "denary" version 

#### Input

 The input is to be read from the standard input, with one test case per line:
 
 ```
 <game-type> [<5 board cards>] <hand 1> <hand 2> <...> <hand N>
 ```
 
 ...where: 
 
 * `game-type` specifies the game type for this test case, one of:
   * `texas-holdem` - for Texas Hold'em
   * `omaha-holdem` - for Omaha Hold'em
   * `five-card-draw` - for Five Card Draw
 
 * `<5 board cards>` is a 10 character string where each 2 characters encode a card, only used for Texas and 
 Omaha Hold' ems
  
 * `<hand X>` is a 4, 8 or 10 character string (depending on game type) where each 2 characters encode a card
 * `<card>` is a 2 character string with the first character representing the rank 
 (one of `A`, `K`, `Q`, `J`, `T`, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`) and the second character representing 
 the suit (one of `h`, `d`, `c`, `s`). Jokers are not used. 
 
 #### Output
 
Format:
 
 ```
 <hand block 1> <hand block 2> <...> <hand block n>
 ```
 ... where:
 
 * `<hand block 1>` is the hand block with the weakest value
 * `<hand block 2>` is the hand block with the second weakest value
 * ... and so forth.
 * `<hand block n>` is the hand block with the strongest value
 
 Each hand block consists of one or multiple hands (each represented by 4, 8 or 10 character string, depending 
 on game type, with 2 characters to encode a card) with equal hand value.
 
 In case there are multiple hands with the same value on the same board they should be ordered alphabetically 
 and separated by `=` signs.
 
 The order of the cards in each hand should remain the same as in the input, e.g., don't reorder `2h3s` into 
 `3s2h`.
 
 #### Examples
 
 Example input:
 ```
 texas-holdem 4cKs4h8s7s Ad4s Ac4d As9s KhKd 5d6d
 texas-holdem 2h3h4h5d8d KdKs 9hJh
 omaha-holdem 3d3s4d6hJc Js2dKd8c KsAsTcTs Jh2h3c9c Qc8dAd6c 7dQsAc5d
 five-card-draw 7h4s4h8c9h Tc5h6dAc5c Kd9sAs3cQs Ah9d6s2cKh 4c8h2h6c9c
 ```
  
 Example output:
 ```
 Ac4d=Ad4s 5d6d As9s KhKd
 KdKs 9hJh
 Qc8dAd6c KsAsTcTs Js2dKd8c 7dQsAc5d Jh2h3c9c
 4c8h2h6c9c Ah9d6s2cKh Kd9sAs3cQs 7h4s4h8c9h Tc5h6dAc5c
 ```
package net.cheltsov.poker.binary

object BitUtil {
  implicit final class LongToCountBitsOps(val value: Long) extends AnyVal {
    def countBits: Int = {
      @scala.annotation.tailrec
      def bits(i: Long, acc: Int): Int =
        if (i == 0)
          acc
        else
          bits(i >> 1, (1 & i).toInt + acc)

      bits(value, 0)
    }
  }

  implicit final class CollapseBitsToLastQuarterOps(val value: Long) extends AnyVal {
    def collapseBitsToLeftQuarter: Long = {
      (value | value << 16 | value << 32 | value << 48) & 0x_ffff_0000_0000_0000L
    }
  }

  implicit final class CollapseBitsToFirstQuarterOps(val value: Long) extends AnyVal {
    def collapseBitsToRightQuarter: Int = {
      ((value | value >>> 16 | value >>> 32 | value >>> 48) & 0x_0000_0000_0000_ffffL).toInt
    }
  }
}

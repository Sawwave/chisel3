// See LICENSE for license details.

package chisel3.util

import chisel3._

object RegEnable {
  /** Returns a register with the specified next, update enable gate, and no reset initialization.
    *
    * @example {{{
    * val regWithEnable = RegEnable(nextVal, ena)
    * }}}
    */
  def apply[T <: Data](next: T, enable: Bool): T = {
    val r = Reg(chiselTypeOf(next))
    when (enable) { r := next }
    r
  }

  /** Returns a register with the specified next, update enable gate, and reset initialization.
    *
    * @example {{{
    * val regWithEnableAndReset = RegEnable(nextVal, 0.U, ena)
    * }}}
    */
  def apply[T <: Data](next: T, init: T, enable: Bool): T = {
    val r = RegInit(init)
    when (enable) { r := next }
    r
  }

  /** Returns a register from a Valid value that updates to the given value when Valid is true.
   */
  def apply[T <: Data](next: Valid[T]): T = {
    RegEnable(next.bits, next.valid)
  }

  /** Returns a register with reset initialization from a Valid value that updates to the given value when Valid is true.
   */
  def apply[T <: Data](next: Valid[T], init: T): T = {
    RegEnable(next.bits, init, next.valid)
  }

  /** Returns a register from a ReadyValidIO value that updates to the given value when ready and valid are both true.
   */
  def apply[T <: Data](next: ReadyValidIO[T]): T = {
    RegEnable(next.bits, next.ready && next.valid)
  }

  /** Returns a register with reset initialization from a ReadyValidIO value that updates to the given value when ready and valid are both true.
   */
  def apply[T <: Data](next: ReadyValidIO[T], init: T): T = {
    RegEnable(next.bits, init, next.ready && next.valid)
  }

}

object ShiftRegister
{
  /** Returns the n-cycle delayed version of the input signal.
    *
    * @param in input to delay
    * @param n number of cycles to delay
    * @param en enable the shift
    *
    * @example {{{
    * val regDelayTwo = ShiftRegister(nextVal, 2, ena)
    * }}}
    */
  def apply[T <: Data](in: T, n: Int, en: Bool = true.B): T = {
    // The order of tests reflects the expected use cases.
    if (n != 0) {
      RegEnable(apply(in, n-1, en), en)
    } else {
      in
    }
  }

  /** Returns the n-cycle delayed version of the input signal with reset initialization.
    *
    * @param in input to delay
    * @param n number of cycles to delay
    * @param resetData reset value for each register in the shift
    * @param en enable the shift
    *
    * @example {{{
    * val regDelayTwoReset = ShiftRegister(nextVal, 2, 0.U, ena)
    * }}}
    */
  def apply[T <: Data](in: T, n: Int, resetData: T, en: Bool): T = {
    // The order of tests reflects the expected use cases.
    if (n != 0) {
      RegEnable(apply(in, n-1, resetData, en), resetData, en)
    } else {
      in
    }
  }
}

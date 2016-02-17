package slab_test

class IPConversion extends SlabSpec {
  "Conversion" in {
    val f = ipFun.ipToLong _
    f("0.0.0.1") should be (1)
    f("1.0.0.0") should be (1<<24)
    f("127.0.0.1") should be ((127<<24) + 1)
  }
}

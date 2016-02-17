package slab_test

class IPConversion extends SlabSpec {
  val f = ipFun.ipToLong _
  "Test conversion from string to Long" in {
    f("0.0.0.1") should be (1)
    f("1.0.0.0") should be (1<<24)
    f("127.0.0.1") should be ((127<<24) + 1)
  }
  "Test invalid IP" in {
    f("asdsaf") should be (0)
  }
}

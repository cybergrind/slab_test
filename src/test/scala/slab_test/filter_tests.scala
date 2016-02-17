package slab_test

import org.scalatest._

abstract class SlabSpec extends FreeSpec with Matchers

class SampleTest extends SlabSpec {
  "Main class" - {
    "test it" in {
      assert(Main.main(Array("test")) === (():Unit))
    }
  }
}

class FilterTest extends SlabSpec {
  "Networks" - {
    var n = new Networks()
    "1 10 n1" in {
      n.add(1, 10, "n1")
    }
    "check it" in {
      n.find(5) should be (List("n1"))
    }
    "5 20 n2" in {
      n.add(5, 20, "n2")
    }
    "check n2" in {
      n.find(11) should be (List("n2"))
      n.find(10) should be (List("n2", "n1"))
      n.find(5) should be (List("n2", "n1"))
      n.find(4) should be (List("n1"))
      n.find(21) should be (List())
      println(n.stored)
    }
  }
}
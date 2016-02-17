package slab_test

import org.scalatest._
import scala.io._

abstract class SlabSpec extends FreeSpec with Matchers


class FilterTest extends SlabSpec {
  "Networks class tests" - {
    val n = new Networks()
    "Add 1-10 to n1" in {
      n.add(1, 10, "n1")
    }
    "Check n1 range" in {
      n.find(5) should be (List("n1"))
      n.find(0) should be (List())
    }
    "Add 5-20 to n2" in {
      n.add(5, 20, "n2")
    }
    "Check n2 and n1 ranges" in {
      n.find(11) should be (List("n2"))
      n.find(10) should be (List("n2", "n1"))
      n.find(5) should be (List("n2", "n1"))
      n.find(4) should be (List("n1"))
      n.find(21) should be (List())
    }
  }
  "Load networks from file" in {
    val n = Networks.fromFile("ranges.tsv")
  }
}

class TransactionsTest extends SlabSpec {
  "Test full workflow by reading 'transactions.tsv'" in {
    val n = Networks.fromFile("ranges.tsv")
    Source.fromFile("transactions.tsv").getLines foreach {
      line:String => {
        val Array(user, ip) = line.split('\t')
        helpers.processUser(user, ip, n)
      }
    }
  }
}

package slab_test
import scala.io._
import java.io._

object Main {
  def main(args: Array[String]): Unit = {
    println("Execute main")
    val t = new Transactions(Networks.fromFile("ranges.tsv"), "transactions.tsv", "output.tsv")
    t.processTransactions
  }
}


object ipFun {
  def ipToLong(ip:String): Long = {
    // a.b.c.d -> split by . -> toLong -> (a<<24 + b<<16 + c<<8 + d)
    try {
      ip.split('.').map(_.toLong)
        .foldRight((0, 0):(Long, Long)){
        (i, acc) => ((acc._1 + (i<<acc._2)), (acc._2 + 8))} _1
    } catch {
      case e: Exception => 0
    }
  }
}


class Transactions (networks:Networks, source:String="", output:String=""){
  def processTransactions():Unit = {
    val n = Networks.fromFile("ranges.tsv")
    val writer = new PrintWriter(new File(output))

    Source.fromFile(source).getLines foreach {
      line:String => {
        val Array(user, ip) = line.split('\t')
        processUser(user, ip).map( (data:List[String]) => {
          writer.write(data.mkString("\t"))
          writer.write("\n")
        })
      }
    }
    writer.close()
  }

  def processUser(user:String, rawIp:String):List[List[String]] = {
    networks.find(ipFun.ipToLong(rawIp)) match {
      case List() => List(List(user, "NO_SEGMENT"))
      case other => other.map((segment:String) => List(user, segment))
    }
  }
}

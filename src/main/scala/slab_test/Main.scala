package slab_test

object Main {
  def main(args: Array[String]): Unit = {
    println("Execute main")
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

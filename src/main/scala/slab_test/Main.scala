package slab_test

import java.io._
import java.util.concurrent.{Semaphore}
import scala.io.{Source}
import monifu.reactive._
import monifu.concurrent._
import monifu.concurrent.Implicits.globalScheduler


object Main {
  def main(args: Array[String]): Unit = {
    println("Execute main")
    runReactive()
  }

  def runReactive(): Unit = {
    val n = Networks.fromFile("ranges.tsv")

    val lock = new Semaphore(1)
    val input = helpers.fromFile("transactions.tsv")
    helpers.processTransactions(lock, "output.tsv", n, input)
    println(s"Waiting lock ${System.currentTimeMillis()}...")
    lock.acquire()
    println(s"Acuired lock ${System.currentTimeMillis()}")
  }
}


object helpers {
  def processTransactions(lock:Semaphore, out:String, n:Networks, 
                          input:Observable[String]): Unit = {
    lock.acquire()
    val writer = new PrintWriter(new File(out))

    input
      .map((s:String) => s.split('\t'))
      .flatMap( s => {
        val Array(user:String, ip:String) = s
        Observable.fromIterable(helpers.processUser(user, ip, n))
      })
      .map((a:List[String]) => writer.println(a.mkString("\t")))
      .last.map(_ => { writer.close(); lock.release() })
      .subscribe()
  }

  def fromFile(path:String): Observable[String] = {
    // We're have to check errors here
    Observable.fromIterable(Source.fromFile(path).getLines.toIterable)
  }

  def processUser(user:String, rawIp:String, networks: Networks):List[List[String]] = {
    networks.find(helpers.ipToLong(rawIp)) match {
      case List() => List(List(user, "NO_SEGMENT"))
      case other => other.map((segment:String) => List(user, segment))
    }
  }

  def ipToLong(ip:String): Long = {
    // a.b.c.d -> split by . -> toLong -> (a<<24 + b<<16 + c<<8 + d)
    try {
      ip.split('.').map(_.toLong)
        .foldRight((0, 0):(Long, Long)){
        (i, acc) => ((acc._1 + (i<<acc._2)), (acc._2 + 8))}._1
    } catch {
      case e: Exception => 0
    }
  }
}

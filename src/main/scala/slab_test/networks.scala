package slab_test
import scala.io._


object Networks {
  def fromFile(path:String): Networks = {
    val n = new Networks;
    Source.fromFile(path).getLines foreach {
      (line:String) => {
        val Array(range, name) = line.split('\t')
        val Array(start, end) = range.split('-').map(helpers.ipToLong _)
        n.add(start, end, name)
      }
    }
    n
  }
}

class Networks {
  val stored = new java.util.TreeMap[Long, List[String]]
  stored.put(0, List())

  def add(startIp: Long, endIp: Long, name: String): Unit = {
    freeze(endIp + 1)
    setTo(endIp, startIp, name)
  }

  def freeze(ip: Long): Unit = {
    val e = stored.lowerEntry(ip)
    e.getKey() match {
      case `ip` => {}
      case _ => {
        val v = e.getValue();
        stored.put(ip, v)
      }
    }
  }

  def setTo(currIp: Long, targetIp: Long, network: String): Unit = {
    val e = stored.lowerEntry(currIp)
    (e.getKey(), e.getKey() > targetIp) match {
      case (ip, true) => {
        stored.put(ip, network :: e.getValue())
        setTo(ip - 1, targetIp, network)
      }
      case _ => {
        stored.put(targetIp, network :: e.getValue())
      }
    }
  }


  def find(ip: Long): List[String] = {
    stored.lowerEntry(ip+1) match {
      case null =>
        return List():List[String]
      case out =>
        val value = out.getValue()
        return value
    }
  }
}

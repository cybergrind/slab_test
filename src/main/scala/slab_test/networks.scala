package slab_test


class Networks {
  val stored = new java.util.TreeMap[Int, List[String]]
  stored.put(0, List())

  def add(startIp: Int, endIp: Int, name: String): Unit = {
    freeze(endIp + 1)
    setTo(endIp, startIp, name)
  }

  def freeze(ip: Int): Unit = {
    val e = stored.lowerEntry(ip)
    e.getKey() match {
      case `ip` => {}
      case _ => {
        val v = e.getValue();
        stored.put(ip, v)
      }
    }
  }

  def setTo(currIp: Int, targetIp: Int, network: String): Unit = {
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


  def find(ip: Int): List[String] = {
    stored.lowerEntry(ip+1) match {
      case null =>
        return List():List[String]
      case out =>
        val value = out.getValue()
        return value
    }
  }
}

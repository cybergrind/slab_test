# ScalaLab test task


## General aglorithm

1. Read network segments and build BTree to perform searches (we're converting IPv4 -> uint32)
2. Lazy read transactions lines
3. Perform per-line stream transformations: `transaction -> map(getNetworkSegments) -> flatMap(writeOutLines)`


## Network segments storing and filtering

RBTree (implementation from java.util.TreeMap, because scala implementation missing NavigableMap interface) is used to store network segments due it `lowerEntry` method, it has `O(log(n))` complexity and ideal to range searches by memory/performance ratio. We can use HashMap with whole IPv4 network address space - but it will have significant memory requirements.


If we have no segments, we're just store:

```
0.0.0.0 => []
```

If we add `192.168.1.0/24 => seg1` segment, we'll have:

```
192.168.2.1 => []
192.168.1.1 => [seg1]
0.0.0.0 => []
```

If we add `192.168.1.10-20 => seg2` we'll have:

```
192.168.2.1  => []
192.168.1.21 => [seg1]
192.168.1.10 => [seg1, seg2]
192.168.1.1  => [seg1]
0.0.0.0 => []
```

And so on for all possible network segments. When we have to find out segments for ip address, we just call `lowerEntry(intIP)` and tree will give us list with network segments for given IPv4 address.


# ScalaLab test task


## General aglorithm

1. Read network segments and build BTree to perform searches (we're converting IPv4 -> uint32)
2. Build stream from transactions
3. Perform per-line stream transformations: `transaction -> map(getNetworkSegments) -> flatMap(writeOutLines)`


## Network segments storing and filtering

BTree (implementation from [JDBM3](https://github.com/jankotek/JDBM3)) is used to store network segments due it `findGreatOrEqual` method, it has `O(log(n))` complexity and ideal to range searches by memory/performance ratio. We can use HashMap with whole IPv4 network address space - but it will have significant memory requirements.


If we have no segments, we're just store:

```
255.255.255.255 => []
```

If we add `192.168.1.0/24 => seg1` segment, we'll have:

```
255.255.255.255 => []
192.168.1.255 => [seg1]
192.168.0.255 => []
```

If we add `192.168.1.10-20 => seg2` we'll have:

```
255.255.255.255 => []
192.168.1.255 => [seg1]
192.168.1.20 => [seg1, seg2]
192.168.1.9 => [seg1]
192.168.0.255 => []
```

And so on for all possible network segments. When we have to find out segments for ip address, we just call `findGreatOrEqual(intIP)` and tree will give us list with network segments for given IPv4 address.


## Stream operations

Streams are used to ease logic of our program. Stream generated from `transactions.tsv` input file, so we're operating only on single line of transactions per processing cycle (it's possible to switch it to network and perform realtime processing). [Monix](https://github.com/monixio/monix) is used as implementation library, general conceptions and overview can be found on any [FRP](https://en.wikipedia.org/wiki/Functional_reactive_programming) article or library.
# ScalaLab test task

## Usage

* `sbt run` - to run program, it'll read data from `ranges.tsv`, `transactions.tsv` and write output to `output.tsv`
* `sbt test` - to run tests

## General aglorithm

1. Read network segments and build BTree to perform searches (we're converting IPv4 -> uint32)
2. Lazy read transactions lines
3. Perform per-line stream transformations: `transaction -> map(getNetworkSegments) -> flatMap(writeOutLines)`

## What's different in reactive

In reactive version is used slightly different approach to handle input data - Streams, you can read more in any [FRP](https://en.wikipedia.org/wiki/Functional_reactive_programming) article. In this example I've used [monix](https://github.com/monixio/monix) library.

When you use you data input as stream - you can write more simple code, you can use well-defined [ReactiveX](http://reactivex.io/) APIs and split your code into the simple *pure functions* (it's ease to test and write). Also you can use different input streams as a source and combine them together - files, network, database and etc.

And reactive libraries provide ability to enable concurrency execution without any additional movements: if you've written your code as bunch of simple pure functions you can put it on without any doubts.


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


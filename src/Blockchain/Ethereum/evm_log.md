# EVM Log & Solidity Event

## EVM Log

- Log的结构如下
```
{
    "blockNumber"  // log所在的block height
    "blockHash"
    "transactionIndex"  // log所属的tx的index
    "transactionHash"
    "logIndex"  // 这个log在block里面的index
    "transactionLogIndex" //应该是这个log在这个tx里面的index
    "address"  // 发起这个log的address，因为log是由tx发起的，所以这个实际是那个tx的发起人地址，可以是外部地址，也可以是contract地址。
    "topics" // topic，一个log最多可以有4个topic
    "data"  // 实际需要log的数据，经过ABI encoding
}
```

- Log里面的`blockNumber`，`transactionHash`，`address`和`topics`等字段都称之为indexed字段，也就是说可以根据这些字段来查询log。
- Indexed的字段也是用来计算BloomFilter的值的
- 一个log最多可以有4个topic，对应于EVM的四个Log Primitives
```
    log0(data)
    log1(data, topic0)
    log2(data, topic0, topic1)
    log3(data, topic0, topic1, topic2)
    log4(data, topic0, topic1, topic2, topic3)
```
- 每个topic都正好是32bytes的数据，如果数据长度超过32bytes，那么会把那个数据的hash当成topic，而非那个数据本身。
- 对于string类型的topic，永远都存的是他的hash，所以如果你真的想要把那个string类型的topic存下来，那么你必须把他放到data里面去。
- Data域是经过ABI Encoding过后的数据。


## Solidity Event

Solidity Event，固定使用一个EVM的topic，所以最多只支持3个topic

### 不带Index参数的event。

假设我们有如下代码：

```solidity
    pragma solidity ^0.4.18;
    
    contract Logger {
        event Log(uint256 a, uint256 b, uint256 c);
        function log(uint256 a, uint256 b, uint256 c) public {
            Log(a, b, c);
        }
    }
```
    
如果我们call `log(1，2，3)`这个function的话，我们会看到如下log被记录下来
```
    topics -> 0x00032a912636b05d31af43f00b91359ddcfddebcffa7c15470a13ba1992e10f0
    data   -> 0x0000000000000000000000000000000000000000000000000000000000000001
              0x0000000000000000000000000000000000000000000000000000000000000002
              0x0000000000000000000000000000000000000000000000000000000000000003
```

其中topics是event signature的hash：
```
    >>> sha3("Log(uint256,uint256,uint256)").hex()
    '00032a912636b05d31af43f00b91359ddcfddebcffa7c15470a13ba1992e10f0'
```
    
而data分别是1，2，3的ABI Encoding过后的值

### 带Indexed参数的event
假设我们有如下代码:
```solidity
    pragma solidity ^0.4.18;
    
    contract Logger {
        event Log(string a, string indexed b, string c);
        function log(string a, string b, string c) public {
            Log(a, b, c);
        }
    }
```

如果我们call `log("1", "2", "3")`这个function的话，我们会看到如下log被记录下来
```
    topics -> 0xb857d3ea78d03217f929ae616bf22aea6a354b78e5027773679b7b4a6f66e86b
              0xb5553de315e0edf504d9150af82dafa5c4667fa618ed0a6f19c69b41166c5510
    data -> 0x0000000000000000000000000000000000000000000000000000000000000040
            0x0000000000000000000000000000000000000000000000000000000000000080
            0x0000000000000000000000000000000000000000000000000000000000000001
            0x6100000000000000000000000000000000000000000000000000000000000000
            0x0000000000000000000000000000000000000000000000000000000000000001
            0x6300000000000000000000000000000000000000000000000000000000000000
```

topic0 是event signature的hash：
```
    >>> sha3("Log(string,string,string)").hex()
    'b857d3ea78d03217f929ae616bf22aea6a354b78e5027773679b7b4a6f66e86b'
```

topic1 是"b"的hash, 此处之所以要hash b是因为string的长度可以大于32字节，但是topic的每一个item最多只放32字节，所以不得不hash。但是如果b的类型是uint256，或者byte32等大小合适的字段，就可以直接放进去topic里面，而不用hash
```
    >>> sha3("b").hex()
    'b5553de315e0edf504d9150af82dafa5c4667fa618ed0a6f19c69b41166c5510'
```

而data分别是"a", "c" 的ABI Encoding过后的值

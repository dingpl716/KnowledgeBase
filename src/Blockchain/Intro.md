

## Decentralization

As of today when people talk about blockchain, the first thing they think about is decentralization. So first, let's talk about what decrelization is and how it works.

In a traditional business, the server is the center and users or clients are just consumers, for example, the traditional banking business. If the server of the bank is down for whatever reason, users will not be able to do any transactions. Even though the server is unlikely down in real life, but all users still have to follow their rules which is subject to change anytime. Think about those users agreements that you have agreed in your life, you will know what I am talking about. So this is traditional centralized service.

In decentralized world, there isn't a central server, instead, everyone is both server and client at the same time. In order to do so, let's take the banking service as a example, everyone needs to have a copy of the ledger where the information of who has how much money is kept. When there is an incoming transaction, for example Alice is sending some money to Bob, their balance need to be updated all over places. Although updating each and every copy of the ledger seems to bring a lot of overhead, it does bring us the benefit of decentralization as well. We don't need to worry about server crash anymore since anyone can be the server, as long as there is client there is server. We don't need to worry about rules being changed neither. For example, in traditional banking system, it is very easy for a bank to freeze Alice's account by stopping providing service to her. But in the decentralized world such thing is unlikely to happen, because other servers will still process Alice's transaction even if some servers rejected it. In this situation, it is those rejecting servers who got evicted from the business, because they no longer achieve consensus with rest of servers, rather than Alice.

So this is a high level picture of decentralization.

## Bitcoin

Decentralization is not a new idea, it has appeared for many years, for example DNS is a decentralized service and it has served the Internet as an essential component since the beginning. But Bitcoin, as a decentralized system, is the first blockchain. So why it is called block chain. What is a block, what's inside a block and why it is a chain? So now let's talk about it. 

Assuming we are building an electronic cash system, and there are four users right now in the system, Alice, Bob, Charley and David. This is the table used to record everyone's balance. But instead of keeping a balance number for everyone, we record each and every bill in everyone's wallet. So as the diagram showed, Alice has two bills, $100 and $10 in her wallet and Charley has a $50 bill.

讲Bitcoin例子
Now let's say Alice wants to buy a bottle of wine from Bob and it costs $80. In a real life scenario, Alice must give the $100 bill to Bob and Bob will return a $20 bill to Alice as change. But in Bitcoin system, Bob doesn't need to do that, instead, Alice will send $80 to Bob and also send $20 back to her own account. After the transaction is processed, the original $100 bill is gone, and Bob and Alice will have a $80 and $20 bill in their wallet respectively.


打包TX
This is very like a database system, we use table to record data and use queries to update data. And you may know that in order to save network trips, sometimes we send queries in batch to a database server. Well in blockchain, we do the same thing. We pack transactions in blocks and broadcast blocks over the network.

八卦
Unlike the centralized system where direction of information flow is straight froward, nodes in a decentralized system gossip with each other. It takes time for nodes to relay information with each other.

Bitcoin State
This table is actually the state of the system. It describes who has how much money. The state changes as new blocks come. 

Ethereum

The essence os Ethereum is to simulate all the nods in the network as a single computer.

So now the target of Ethereum is to create a Turing-complete machine in blockchain. But how should we do that? Well, we have said that the sketch of blockchain is transiting state upon on transactions in blocks. So what should be the state of Ethereum then, or say, what should be the state of a Turing-complete machine? Well, that's easy to answer. All the PCs, laptops and phones that you are using everyday are Turing-complete machines, so what's their state? The answer is memory. By persisting the whole memory of your laptop to disk before shutting it down, you can restore it to the exact same state where it was. OK, so now just need to figure out how to mimic memory over blockchain network.

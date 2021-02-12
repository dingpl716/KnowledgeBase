## Kafka

### Topic & Partition
- A topic can have multiple partitions
- The producer needs to know which partition to send data to.
- The consumer needs to know which partition to get data from.
- One consumer can consume multiple partitions, but one partition should be consumed by ONE consumer.

### Consumer Group
- Queue: Message published once, consumed once.
- Pub Sub: Message published once, consumed multiple times.
- Kafka: How can we do both?
- Answer: Consumer Group
- One partition could be consumed by MULTIPLE consumer groups.
- To act like a queue, put all your consumers in one group.
- To act like a pub/sub, put each consumer in a unique group.

## Kafka Broker
- Kafka broker is the Kafka server
- Partition level Leader/Follower rather than server level
- 

## Toolings
- https://github.com/edenhill/kafkacat
- https://github.com/obsidiandynamics/kafdrop

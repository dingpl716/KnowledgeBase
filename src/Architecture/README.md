- [Reference](#reference)
- [Availability](#availability)
  - [Replication](#replication)
    - [Master-Master](#master-master)
    - [Master-Slave](#master-slave)
    - [Buddy Replication](#buddy-replication)
  - [Failover](#failover)
- [Stability](#stability)
  - [Circuit Breaker](#circuit-breaker)
  - [Timeouts](#timeouts)
  - [Let it crash/Supervisors](#let-it-crashsupervisors)
  - [Crash Early](#crash-early)
  - [Bulkheads](#bulkheads)
  - [Steady state (clean up resources)](#steady-state-clean-up-resources)
  - [Throttling](#throttling)
    - [SEDA](#seda)
- [Scalability](#scalability)
  - [State](#state)
    - [Distributed Caching](#distributed-caching)
      - [Write-behind](#write-behind)
      - [Write-through](#write-through)
      - [Eviction-Policy](#eviction-policy)
        - [TTL](#ttl)
        - [FIFO](#fifo)
        - [LIFO](#lifo)
      - [Peer-to-peer](#peer-to-peer)
    - [Data Grids](#data-grids)
      - [In-memory storage](#in-memory-storage)
      - [Durable storage](#durable-storage)
    - [Service of Records](#service-of-records)
      - [NoSQL](#nosql)
        - [Key-value Database](#key-value-database)
        - [Document Database](#document-database)
        - [Graph Database](#graph-database)
        - [Data Structure Database](#data-structure-database)
      - [RDBMS](#rdbms)
        - [Sharding](#sharding)
          - [Partitioning](#partitioning)
          - [Replication](#replication)
        - [Denormalization](#denormalization)
        - [ORM + Rich domain model anti pattern](#orm--rich-domain-model-anti-pattern)
    - [HTTP Caching](#http-caching)
      - [Reverse Proxy](#reverse-proxy)
      - [CDN](#cdn)
    - [CAP Theory](#cap-theory)
      - [Consistent/Atomic Data](#consistentatomic-data)
      - [Eventual consistent data](#eventual-consistent-data)
    - [Concurrency](#concurrency)
      - [Message-passing Concurrency](#message-passing-concurrency)
      - [Software Transactional Memory](#software-transactional-memory)
      - [Dataflow Concurrency](#dataflow-concurrency)
      - [Shared-state Concurrency](#shared-state-concurrency)
    - [Paritioning](#paritioning)
    - [Replication](#replication)
  - [Behavior](#behavior)
    - [Compute Girds](#compute-girds)
    - [Event-driven Architecture](#event-driven-architecture)
      - [Messaging](#messaging)
      - [Actors](#actors)
      - [Enterprise Service Bus](#enterprise-service-bus)
      - [Domain Events](#domain-events)
      - [Event Stream Processing](#event-stream-processing)
      - [Event Sourcing](#event-sourcing)
      - [CQRS](#cqrs)
    - [Load Balancing](#load-balancing)
      - [Round-robin allocation](#round-robin-allocation)
      - [Random Allocation](#random-allocation)
      - [Weighted Allocation](#weighted-allocation)
      - [Dynamic Load Balancing](#dynamic-load-balancing)
        - [Work-stealing](#work-stealing)
        - [Work-donation](#work-donation)
        - [Queue-depth querying](#queue-depth-querying)
      - [Consistent Hashing](#consistent-hashing)
    - [Parallel Computing](#parallel-computing)
        - [SPMD Pattern](#spmd-pattern)
        - [Master/Worker Pattern](#masterworker-pattern)
        - [Loop Parallelism Pattern](#loop-parallelism-pattern)
        - [Fork/join Pattern](#forkjoin-pattern)
        - [MapReduce Pattern](#mapreduce-pattern)

# Reference
https://www.slideshare.net/jboner/scalability-availability-stability-patterns

# Availability

## Replication

### Master-Master

### Master-Slave

### Buddy Replication

## Failover

# Stability

## Circuit Breaker

## Timeouts

## Let it crash/Supervisors

## Crash Early

## Bulkheads

## Steady state (clean up resources)

## Throttling

### SEDA

# Scalability

## State

### Distributed Caching

#### Write-behind

#### Write-through

#### Eviction-Policy

##### TTL
##### FIFO
##### LIFO

#### Peer-to-peer

### Data Grids
#### In-memory storage
#### Durable storage

### Service of Records

#### NoSQL
##### Key-value Database
##### Document Database
##### Graph Database
##### Data Structure Database

#### RDBMS

##### Sharding
###### Partitioning
###### Replication

##### Denormalization
##### ORM + Rich domain model anti pattern

### HTTP Caching

#### Reverse Proxy
#### CDN

### CAP Theory
#### Consistent/Atomic Data
#### Eventual consistent data

### Concurrency
#### Message-passing Concurrency
#### Software Transactional Memory
#### Dataflow Concurrency
#### Shared-state Concurrency

### Paritioning
### Replication

## Behavior

### Compute Girds

### Event-driven Architecture
#### Messaging
#### Actors
#### Enterprise Service Bus
#### Domain Events
#### Event Stream Processing
#### Event Sourcing
#### CQRS

### Load Balancing
#### Round-robin allocation
#### Random Allocation
#### Weighted Allocation

#### Dynamic Load Balancing
##### Work-stealing
##### Work-donation
##### Queue-depth querying 

#### Consistent Hashing

### Parallel Computing
##### SPMD Pattern
##### Master/Worker Pattern
##### Loop Parallelism Pattern
##### Fork/join Pattern
##### MapReduce Pattern

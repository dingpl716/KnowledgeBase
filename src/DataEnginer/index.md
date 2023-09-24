## Data Engineering Tools

- Hive
  - A data warehousing and SQL-like query language tool that runs on top of the Hadoop ecosystem
  - Can Read data from HDFS, S3
  - HiveQL gets translated into MapReduce or Tez jobs that are executed on the Hadoop cluster

- Presto
  - Presto is an open-source distributed SQL query engine developed by Facebook
  - High Performance: Presto is designed for low-latency query processing, making it suitable for interactive and ad-hoc queries. It achieves this by utilizing an in-memory processing model and optimizing query execution plans.
  - Can read data from Hadoop HDFS, Amazon S3, relational databases, NoSQL stores, and more
  - 

- Spark
  - An open-source, distributed data processing and analytics framework
  - Designed to address the limitations of the traditional MapReduce computing model in terms of speed and ease of use
  - In-Memory Processing: One of the defining features of Spark is its ability to perform in-memory data processing. This means that intermediate data is stored in memory, reducing the need for frequent disk I/O and significantly improving processing speed.
  - Can read data from HDFS, HBase, Apache Cassandra, and Amazon S3. It also supports various file formats like Parquet, ORC, Avro, and more.
  - RDD, resilient distributed database is the foundmental part of Spark

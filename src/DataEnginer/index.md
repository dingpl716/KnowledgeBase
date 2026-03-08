## Knowledge Structure
  1. Programming and Scripting Languages
     - Python: For data manipulation, scripting, and automation.
     - SQL: Essential for querying and managing relational databases.
     - Java or Scala: Often used in big data frameworks like Hadoop and Spark.
     - Shell Scripting: For automating tasks in Unix/Linux environments.
1. Data Warehousing and ETL/ELT Processes
Data Warehousing Concepts: Understanding schemas (star, snowflake), OLAP vs. OLTP.
ETL/ELT Tools: Proficiency with tools like Informatica, Talend, or custom ETL scripts.
Data Integration: Techniques for combining data from different sources.
1. Databases and Storage Systems
Relational Databases (RDBMS): MySQL, PostgreSQL, Oracle.
NoSQL Databases: MongoDB, Cassandra, HBase for unstructured data.
Data Lakes: Understanding storage of raw data in systems like Hadoop HDFS or cloud storage.
1. Big Data Technologies
Hadoop Ecosystem: HDFS, MapReduce for distributed storage and processing.
Apache Spark: In-memory data processing for speed and efficiency.
Distributed Computing: Concepts of parallel processing and cluster management.
1. Data Pipelines and Workflow Orchestration
Workflow Tools: Apache Airflow, Luigi, or Prefect for scheduling tasks.
Pipeline Design: Building robust and scalable data pipelines.
7. Real-time Data Streaming
Apache Kafka: For building real-time data pipelines and streaming apps.
Apache Flink or Spark Streaming: For processing streaming data.
8. Data Modeling and Architecture
Schema Design: Normalization and denormalization techniques.
Data Modeling: Conceptual, logical, and physical data models.
Database Design Principles: Indexing, partitioning, sharding.

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

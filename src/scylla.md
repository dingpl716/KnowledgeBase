# Scylla

## Check keyspace and tables

```
SELECT keyspace_name, table_name, compaction
FROM system_schema.tables
WHERE keyspace_name in ('mes_prod', 'ad_quality_prod')
```

```
SELECT * FROM system_schema.keyspaces
WHERE keyspace_name = 'your_keyspace_name';
```

```
SELECT column_name, type, kind
FROM system_schema.columns
WHERE keyspace_name = 'prod' AND table_name = 'nova_id_mapping_target_users_daily_v2';
```

```
SELECT SUM(partitions_count) AS total_partitions
FROM system.size_estimates
WHERE keyspace_name = 'nebula_prod'
  AND table_name = 'user_info_index_v3';
```

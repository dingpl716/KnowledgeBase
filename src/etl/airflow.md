## Airflow

### Back Fill data

```shell
nohup airflow dags backfill nova_sample_count_ctr_hourly \
  --task-regex '^sync.manhattan$' \
  --start-date 2025-0-02 \
  --end-date 2025-05-17 \
  --reset-dagruns \
  --yes \
  --ignore-dependencies \
  > nova_sample_count_ctr_hourly.log 2>&1 &
```

```shell
nohup airflow dags backfill nova_sample_count_ctr_hourly \
  --task-regex '^insert_ad_data_nova_sample_count_ctr_hourly$' \
  --start-date 2025-05-07T01:00:00 \
  --start-date 2025-05-07T01:00:00 \
  --reset-dagruns \
  --yes \
  --ignore-dependencies \
  > nova_sample_count_ctr_hourly.log 2>&1 &
```


```shell
airflow dags backfill nebula_user_info_index_dump_hourly_v4 \
  --start-date 2025-01-30T09:00:00 \
  --end-date 2025-01-30T11:00:00 \
  --mark-success
```

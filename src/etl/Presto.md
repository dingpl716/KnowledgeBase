

### UTC `pdate`, `phour` to PST/PDT `pdate`, `phour`
```sql
SELECT 
    pdate,
    phour,
    -- Convert pdate and phour to a timestamp
    date_parse(pdate || ' ' || phour, '%Y-%m-%d %H') AS utc_timestamp,
    -- Convert the UTC timestamp to PST
    date_format(
        (date_parse(pdate || ' ' || phour, '%Y-%m-%d %H') AT TIME ZONE 'UTC') AT TIME ZONE 'America/Los_Angeles',
        '%Y-%m-%d'
    ) AS pst_date,
    -- Extract the hour from the converted timestamp
    hour((date_parse(pdate || ' ' || phour, '%Y-%m-%d %H') AT TIME ZONE 'UTC') AT TIME ZONE 'America/Los_Angeles') AS pst_hour
FROM your_table_name;
```

### Query data X hour before based on `pdate`, `phour`
```sql
with a as (
  select
      date_parse(pdate || ' ' || phour, '%Y-%m-%d %H') AS utc_timestamp,
      *
  from nova.ods.ods_nb_ads_offline_log
)

SELECT
  *
from a
where utc_timestamp >= date_add('hour', -6, current_timestamp)
```

### Filter item in an array
```sql
  where cardinality(filter(exps, x -> x like 'ads_android_rank_v1-%')) > 0
```


### JsonPath
```sql
select json_extract_scalar(json_extract_scalar(context,'$.app_list'),'$["com.tencent.mm"]')
```

### UTC `pdate`, `phour` to PST/PDT `pdate`, `phour`

```sql

SELECT
  pdate,
  phour,
  from_utc_timestamp(concat(pdate, ' ', lpad(phour, 2, '0'), ':00:00'), 'PST') AS pdate_phour_pst,
  to_date(from_utc_timestamp(concat(pdate, ' ', lpad(phour, 2, '0'), ':00:00'), 'PST')) AS pdate_pst,
  hour(from_utc_timestamp(concat(pdate, ' ', lpad(phour, 2, '0'), ':00:00'), 'PST')) AS phour_pst
FROM
  your_table;
```

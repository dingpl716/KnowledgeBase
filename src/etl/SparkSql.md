### JsonPath
```sql
    select get_json_object(get_json_object(context,'$.app_list'),"$['com.tencent.mm']") as wechat3,
```

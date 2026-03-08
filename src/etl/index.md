src层：原始json/avro数据
ods层：从src层到处的高度压缩parquet文件表
dwd层：从ods生成的轻量化处理数据，例如去重，轻量化解析
dws层：带有维度聚合的数据层，可以用于图形化展示
ads层：最高度聚合层，直接体现业务最终结果，例如DAU，Revenue等等

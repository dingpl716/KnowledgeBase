- 3 principles
  - Unique identification via content addressing
    - Content ID (CID):
      - binary: <cid-version><ipld-format(文件编码格式，json, protobuf等)><multihash(hash算法，长度，hash值)>
      - string: <base>base(<cid-version><ipld-format><multihash>)
      - Example:
        - `Qm`开头的CID是version 0, 这个CID里面只有multihash，没有前面的东西，并且以Base 58 BTC编码
  - Content linking via directed acyclic graphs (DAGs)
    - Merkle DAG, just like Merkle Tree with multiple roots
    - Block size, cannot be too big nor too small
      - Read v.s. Write
      - Creation v.s. mutation
  - Content discovery via distributed hash tables (DHTs)

`ipfs dag stat [CID]` , command to check file size

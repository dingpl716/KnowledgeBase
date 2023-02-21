## Singing algorithm use by Bitcoin and Ethereum

Both BTC and ETH use Elliptic Curve Digital Signature Algorithm (`ECDSA`) and `Keccak256` for sining transactions. ECDSA is a serious of curves, and the curved used by BTC and ETH is `secp256k1`.


### ECDSA

- ECDS签名总共65字节由三部分构成，`{r, s, recover_id}`， 其中
  - `r`是前32字节，其物理意义是曲线上的横坐标
  - `s`是紧接着的32字节
  - `recover_id`是最后一字节，其可能的值为，这是因为对于一个给定的横坐标，我们可以找到四个点
    - `<<0>>`, 对应`r`是偶数的情况
    - `<<1>>`, 对应`r`是奇数的情况
    - `<<2>>`, 这种情况相当罕见，0.000000000000000000000000000000000000373%
    - `<<3>>`, 这种情况相当罕见，0.000000000000000000000000000000000000373%

### BTC

在BTC的tx里面，我们并不是直接把`recover_id`放进去，而是需要计算一个`v`出来，计算规则如下：

- `v = 27 + recover_id = 27` uncompressed public key, y-parity 0, magnitude of x lower than the curve order
- `v = 27 + recover_id = 28` uncompressed public key, y-parity 1, magnitude of x lower than the curve order
- `v = 27 + recover_id = 29` uncompressed public key, y-parity 0, magnitude of x greater than the curve order
- `v = 27 + recover_id = 30` uncompressed public key, y-parity 1, magnitude of x greater than the curve order
- `v = 31 + recover_id = 31` compressed public key, y-parity 0, magnitude of x lower than the curve order
- `v = 31 + recover_id = 32` compressed public key, y-parity 1, magnitude of x lower than the curve order
- `v = 31 + recover_id = 33` compressed public key, y-parity 0, magnitude of x greater than the curve order
- `v = 31 + recover_id = 34` compressed public key, y-parity 1, magnitude of x greater than the curve order

由于`<<2>>`，`<<3>>`基本不会发生，所以你基本上只能看到v = 27, 28 或 v = 31, 32的情况

### ETH

eip155引入了`chainId`所以v的计算如下：

`v = recover_id + CHAIN_ID * 2 + 35`

所以对于main net，（chain_id = 1）来说，v基本上要么是37要么是38


### EIP712

在EIP712中，`v`只会是27，或者是28，因为eip155只用来签transaction，不用来签struct message

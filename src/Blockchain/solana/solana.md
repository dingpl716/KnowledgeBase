```shell
$ solana config get
$ solana config set --url http://127.0.0.1:8899
$ solana config set --url https://api.devnet.solana.com


$ solana-keygen new --outfile solana-wallet/keypair.json

$ solana airdrop 1 $(solana-keygen pubkey solana-wallet/keypair.json)


$ solana deploy -v --keypair solana-wallet/keypair.json dist/solana/program/helloworld.so
RPC URL: http://127.0.0.1:8899
Default Signer Path: solana-wallet/keypair.json
Commitment: confirmed
Program Id: A3r6zeG64HvPY6TB9e3yocBmhnLw8jhrt8fgfk6xbQ5t


$ solana rent 15000
Rent per byte-year: 0.00000348 SOL
Rent per epoch: 0.000288276 SOL
Rent-exempt minimum: 0.10529088 SOL

```


## Accounts

### Account Structure

- `address`	Usually a public key 
- `owner`	The program owner of this account
- `lamports`	The number of lamports owned by this account
- `executable`	Whether this account can process instructions
- `data`	The raw data byte array stored by this account
- `rent_epoch`	The next epoch that this account will owe rent

- Data accounts store data
  - System owned accounts, max 10MB
  - PDA (Program Derived Address) accounts, max 10KB
    - 这是一个假的PubKey, 它不在ED25519曲线上，所以也没有对应的私钥
    - 拥有PDA的Program可以为它签名
- Program accounts store executable programs, max 10MB
- Native accounts that indicate native programs on Solana such as System, Stake, and Vote, max 10MB

```rust
pub struct AccountInfo<'a> {
    /// Public key of the account
    pub key: &'a Pubkey,
    /// Was the transaction signed by this account's public key?
    pub is_signer: bool,
    /// Is the account writable?
    pub is_writable: bool,
    /// The lamports in the account.  Modifiable by programs.
    pub lamports: Rc<RefCell<&'a mut u64>>,
    /// The data held in this account.  Modifiable by programs.
    pub data: Rc<RefCell<&'a mut [u8]>>,
    /// Program that owns this account
    pub owner: &'a Pubkey,
    /// This account's data contains a loaded program (and is now read-only)
    pub executable: bool,
    /// The epoch at which this account will next owe rent
    pub rent_epoch: Epoch,
}
```

```rust
pub fn create_with_seed(
    base: &Pubkey,
    seed: &str,
    program_id: &Pubkey,
) -> Result<Pubkey, SystemError> {
    if seed.len() > MAX_ADDRESS_SEED_LEN {
        return Err(SystemError::MaxSeedLengthExceeded);
    }

    Ok(Pubkey::new(
        hashv(&[base.as_ref(), seed.as_ref(), program_id.as_ref()]).as_ref(),
    ))
}

/// Generate a derived program address
///     * seeds, symbolic keywords used to derive the key
///     * program_id, program that the address is derived for
pub fn create_program_address(
    seeds: &[&[u8]],
    program_id: &Pubkey,
) -> Result<Pubkey, PubkeyError>

/// Find a valid off-curve derived program address and its bump seed
///     * seeds, symbolic keywords used to derive the key
///     * program_id, program that the address is derived for
pub fn find_program_address(
    seeds: &[&[u8]],
    program_id: &Pubkey,
) -> Option<(Pubkey, u8)> {
    let mut bump_seed = [std::u8::MAX];
    for _ in 0..std::u8::MAX {
        let mut seeds_with_bump = seeds.to_vec();
        seeds_with_bump.push(&bump_seed);
        if let Ok(address) = create_program_address(&seeds_with_bump, program_id) {
            return Some((address, bump_seed[0]));
        }
        bump_seed[0] -= 1;
    }
    None
}
```

## Transactions

```rust
struct Transaction {
    // Each signature is 64 bytes, ED25519
    signatures: CompactArray<[u8; 64]>, 
    message: Message
}

struct Message {
    header: MessageHeader,

    // Need signature, read-write, first
    // Need signature, read-only, second
    // Do not need signature, read-write, third
    // Do not need signature, read-only, last 
    // Ed25519 PubKey
    account_keys: CompactArray<[u8; 32]>,

    // SHA256 
    recent_blockhash: [u8; 32],

    // 
    instructions: CompactArray<Instruction>
}

struct MessageHeader {
    // The first value is the number of required signatures in the containing transaction
    num_required_signatures: u8;

    // The second value is the number of those corresponding account addresses that are read-only
    num_readonly_signed_accounts: u8;

    // The third value in the message header is the number of read-only account addresses not requiring signatures
    num_readonly_unsigned_accounts: u8;
}

struct Instruction {
    // Message.account_keys里的坐标
    program_id_index: u8,

    // Message.account_keys里的坐标
    account_address_indexes: [ 0, 1 ],

    data: CompactArray<u8>,
}

struct CompactArray<T> {
    length: u16;
    data: Vec<u8>
}

```

```javascript
{
  blockTime: 1659395140,
  meta: {
    err: null,
    fee: 5000,
    innerInstructions: [],
    loadedAddresses: { readonly: [], writable: [] },
    logMessages: [
      'Program 11111111111111111111111111111111 invoke [1]',
      'Program 11111111111111111111111111111111 success'
    ],
    postBalances: [ 999995999980000, 4000000000, 1 ],
    postTokenBalances: [],
    preBalances: [ 999996999985000, 3000000000, 1 ],
    preTokenBalances: [],
    rewards: [],
    status: { Ok: null }
  },
  slot: 17251,
  transaction: {
    message: Message {
      header: {
        numReadonlySignedAccounts: 0,
        numReadonlyUnsignedAccounts: 1,
        numRequiredSignatures: 1
      },
      accountKeys: [
        PublicKey {
          _bn: <BN: d72dbe11ebc9b20c16d327d47283bf1142e2fd5659e109ed0e9bcb4995c77608>
        },
        PublicKey {
          _bn: <BN: 2cedc6947c56185ce82211ae79427ee94fac0f1991a4d7ea9952728f171497d4>
        },
        PublicKey { _bn: <BN: 0> }
      ],
      recentBlockhash: 'FNWwtEU9CWEkTc7a5G7tm2CoqGXXYKF8dZmaqPfJfZSr',
      instructions: [
        {
          accounts: [ 0, 1 ],
          data: '3Bxs3zzLZLuLQEYX',
          programIdIndex: 2
        }
      ],
      indexToProgramIds: Map(1) { 2 => PublicKey { _bn: <BN: 0> } }
    },
    signatures: [
      '4wyXLcGEW1Z8cahBoTQUefq8hNYHHPsqjU48piJUkmj7549eUqN4spSzXzk8FfpmYqa9AfiTGTgAWrdFEwMs2Zjx'
    ]
  }
}
```


## Native Programs

### SystemProgram
- create new accounts
- allocation account data
- 

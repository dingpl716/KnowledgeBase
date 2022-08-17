```shell
solana config get
solana config set --url http://127.0.0.1:8899
solana config set --url https://api.devnet.solana.com


solana-keygen new --outfile solana-wallet/keypair.json

solana airdrop 1 $(solana-keygen pubkey solana-wallet/keypair.json)


solana deploy -v --keypair solana-wallet/keypair.json dist/solana/program/helloworld.so
RPC URL: http://127.0.0.1:8899
Default Signer Path: solana-wallet/keypair.json
Commitment: confirmed
Program Id: A3r6zeG64HvPY6TB9e3yocBmhnLw8jhrt8fgfk6xbQ5t

```

## History

- Secure Sockets Layer, or SSL, was the original name of the protocol when it was developed in the mid-1990s by Netscape
- SSL 1.0 was never released to the public
- And SSL 2.0 had serious flaws. 
- SSL 3.0, released in 1996, was completely revamped, and set the stage for what followed
- When the next version of the protocol was released in 1999, it was standardized by the Internet Engineering Task Force (IETF) and given a new name: Transport Layer Security, or TLS

## TLS 1.2 Handshake

- Hand-shake (2 round trips)
  - Client send request saying: "hey server, my name is client, I support algorithms A, B, C, D for asymmetric encryption and support X, Y, Z algorithms for symmetric encryption".
  - Server picks the supported algorithms and responses its certificate containing the public key
  - Client receive public key and verify it
  - Client generates a random number as the symmetric key, encrypts the symmetric key by using the public key and send back to server.
  - Server got the symmetric key and decrypts it by its private key.
  - Server responds, saying I am read. Connection established
- Client sends requests that are encrypted by the symmetric key.
- Keep-alive.

## Diffie Hellman

- Four keys in total:
  - `Private Key 1`
  - `Public Key`
  - `Private Key 2`
  - `Symmetric Key`
- Combining the first three keys, you will get the Symmetric key
  `Private Key 1 + Public Key + Private Key 2 -> Symmetric Key`
- Combining the Private key 1 with the Public key, you will get a unbreakable bundle that can be transferred over network
  `Private Key 1 + Public Key -> unbreakable bundle`
- Combining the Private key 2 with the Public key, you will get a unbreakable bundle that can be transferred over network
  `Private Key 2 + Public Key -> unbreakable bundle`

## TLS 1.3 Improvements

- Almost same as TLS 1.2, but only uses Diffie Hellman during hand-shake
- Hand-shake (Only 1 round trip)
  - Client generates `Private Key 1` and `Public Key`
  - Client sends the `Private Key 1` and `The bundle of Private Key 1 + Public Key` to the server.
  - The server generates the `Private Key 2` and calculates the `Symmetric Key` by combining the `Private Key 2` and `The bundle of Private Key 1 + Public Key` 
  - The server returns the `The bundle of Private Key 2 + Public Key` plus its certificate.
  - The client calculates the `Symmetric Key` by combining the `Private Key 1` and `The bundle of Private Key 2 + Public Key`
  - The clients verifies the certificate
  - Connection established.

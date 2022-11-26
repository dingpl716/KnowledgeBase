# OpenVPN

Tunelblick is just a UI for OpenVPN

## Basics

To set up an OpenVPN, you need to create a root key pair, a.k.a root CA (Certificate Authority). Then use this root ca to sign key pairs for server and each client respectively. Then distribute the key pairs to where they need to be used. A more formal way to generate server and client key and certificate is to generate them on separated machine instead of distributing them latter. To do this, the root CA should be placed in a dedicated machine without internet access for sake of security. Server and clients, then generate their public and private key. Root CA import requests, which are unsigned public keys of server and clients. After Root CA signs them, they become certificates. You can find more detailed info [here](https://community.openvpn.net/openvpn/wiki/EasyRSA3-OpenVPN-Howto).


## How to set up OpenVPN server

Following steps are summarized from [here](https://openvpn.net/community-resources/how-to/#openvpn-quickstart)

1. Install OpenVPN and easy-rsa
```
sudo apt-get install openvpn easy-rsa
```

2. Copy over the Easy-RSA generation scripts
```
sudo cp -r /usr/share/easy-rsa /etc/openvpn
```

3. Go to the folder, and you should find easy-rsa program there.
```
cd /etc/openvpn/easy-rsa
```

4. Build Root Certificate Authority
```
 ./easyrsa init-pki
 ./easyrsa build-ca
```

5. Build Server certificate (public key) and private key:
   
   Replace `<SERVER_NAME>` with your server name. eg. Server-01
   Option `nopass` can be used to disable password locking the key.
```
./easyrsa build-server-full <SERVER_NAME>
```

6. Build a client certificate (public key) and private key:
   
   Replace `<CLIENT_NAME>` with your client name. eg. Client-01 or alice
   Option `nopass` can be used to disable password locking the key.
   Repeat for all clients.
```
./easyrsa build-client-full <CLIENT_NAME>
```



7. Generate DH parameters used during the TLS handshake with connecting clients. The DH params are not security sensitive and are used only by an OpenVPN server.
```
./easyrsa gen-dh
```

8. Now you need to distribute theses files to server and client machine.

   For Server, you need to copy following files:
   ```
   ca.crt
   SERVER_NAME.crt
   SERVER_NAME.key
   dh.pem
   ```

   For Client, you need to copy following files:
   ```
   ca.crt
   CLIENT_NAME.crt
   CLIENT_NAME.key
   ```

9. Editing server configuration file
   - You can find example under `/usr/share/doc/openvpn/examples/sample-config-files/server.conf`
   - First, make a copy of this file and do following modifications:
   - Edit `ca`, `cert`, `key` and `dh` parameter to point files mentioned above.
   - Uncomment out the client-to-client directive if you would like connecting clients to be able to reach each other over the VPN. By default, clients will only be able to reach the server.

10. Editing client configuration file, 
    - You can find example under `/usr/share/doc/openvpn/examples/sample-config-files/client.conf`
    - First, make a copy of this file and do following modifications:
    - Edit `ca`, `cert` and `key` parameter to point files mentioned above.
    - Edit `remote` to point to the hostname/IP address

11. Start OpenVPN server
    - Make sure UDP 1194 is open on the server
    - run command `sudo openvpn --config server.conf`
    - `--daemon` option can run it in background.

12. Starting the client,
    - Either use TunelBlick or `sudo openvpn --config client.conf --daemon`


## Questions:

1. When we start a machine in a private subnet, we cannot ssh to it. So how can we let this newly created machine connect to VPN server upon creation?
2. The VPN requires an address range not overlap with the one that's currently being used. For example, our VPC has address range 10.0.0.0/16, and OpenVPN by default use 10.8.0.0/24. These two ranges are OK since they are not overlapped. But in this case, you can only ssh to other machines by using their VPN address, such as 10.8.0.6 or 10.8.0.10. So how can we ssh to machines using VPC address, such as 10.0.15.23?

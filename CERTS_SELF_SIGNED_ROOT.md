## Create a self-signed root CA certificate

1. Prepare a directory for the root CA, where the private key of the root certificate is stored.
```shell script
mkdir rootCA
mkdir rootCA/{certs,db,private}
chmod 700 rootCA/private
touch rootCA/db/db
touch rootCA/db/db.attr
```

2. Create a root-csr.conf configuration file. Example is in /src/main/resources/certs/root-csr.conf

3. Run the following command to create a new root key and a self-signed root certificate:
```shell script
openssl req -x509 -sha256 -days 3650 -newkey rsa:3072 \
    -config root-csr.conf -keyout rootCA/private/rootCA.key \
    -out rootCA/rootCA.crt
```

4. Run the following command to check what information the created certificate contains:
```shell script
openssl x509 -in rootCA/rootCA.crt -text -noout
```


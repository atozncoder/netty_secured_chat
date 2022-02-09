## Create a leaf certificate for the webserver of the web UI

[Original doc](https://help.f-secure.com/product.html#business/threatshield/latest/en/task_D81B8959CD3643C5A9E8DD0E2A4EF32E-threatshield-latest-en)

Follow these instructions to create a certificate signing request and then use the intermediate CA certificate and its key to sign the leaf certificate in the request.

1. Create a server-csr.conf: /src/main/resources/certs/server-csr.conf

2. Run the following command to create a certificate signing request with the new private key:
```shell script
openssl req -new -config server-csr.conf -out server.csr \
        -keyout server.key
```

3. Create a new CA.conf configuration file  as /src/main/resources/certs/CA.conf

4. Run the following command to sign the server certificate in the request with the intermediate CA certificate:
```shell script
openssl ca -config CA.conf -days 365 -create_serial \
    -in server.csr -out server.crt -extensions leaf_ext -notext
```

5. Link certificates together to have the certificate chain in one file:
```shell script
cat server.crt CA/CA.pem >server.pem
```


### Create an intermediate CA certificate for creating other certificates

[Original doc](https://help.f-secure.com/product.html#business/threatshield/latest/en/task_9EF132D90B9241268DF4EC8CA5FADBBF-threatshield-latest-en).

You need to create an intermediate certificate signed with the root certificate to sign the certificate for the server that hosts the web portal and that can be used to create certificates for other internal servers.

Follow these instructions to create a certificate signing request and then use the root certificate and the key to sign the intermediate certificate in the request.

1. Prepare a directory for the intermediate CA, where the private key of the CA certificate is stored.
```shell script
mkdir CA
mkdir CA/{certs,db,private}
chmod 700 CA/private
touch CA/db/db
touch CA/db/db.attr
```

2. Create a CA-csr.conf configuration file. Example: /materials/intermidiate/CA-csr.conf

3. Run the following command to create a certificate signing request with the new private key:
```shell script
openssl req -new -config CA-csr.conf -out CA.csr \
        -keyout CA/private/CA.key
```
4. Create a rootCA.conf:  /materials/intermidiate/rootCA.conf

5. Run the following command to sign the certificate in the request with the root certificate:
```shell script
openssl ca -config rootCA.conf -days 365 -create_serial \
    -in CA.csr -out CA/CA.crt -extensions ca_ext -notext
```

6. Link certificates together to have the certificate chain in one file:
```shell script
cat CA/CA.crt rootCA/rootCA.crt >CA/CA.pem
```

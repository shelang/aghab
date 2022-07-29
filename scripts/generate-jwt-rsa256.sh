#!/usr/bin/env bash

export PEM_RSA256_FILENAME=jwtRS256.pem

openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem
openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem


echo "
    Move publicKey.pem to resources/publicKey.pem
    $ cp ./scripts/publicKey.pem src/main/resources/publicKey.pem

    Move privateKey.pem to resources/privateKey.pem
    $ cp ./scripts/privateKey.pem src/main/resources/privateKey.pem
"
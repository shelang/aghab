#!/usr/bin/env bash

export PEM_RSA256_FILENAME=jwtRS256.pem

openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem
openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem

cp ./scropts/publicKey.pem src/main/resources/META-INF/resources/publicKey.pem
cp ./scropts/privateKey.pem src/main/resources/privateKey.pem


echo "
    Move publicKey.pem to resources/META-INF/resources/publicKey.pem
    Move privateKey.pem to resources/privateKey.pem
"
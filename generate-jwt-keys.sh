#!/bin/bash

# Script to generate public and private keys for JWT token signing and verification
# This script should be run from the project root directory

# Create certs directory if it doesn't exist
mkdir -p src/main/resources/certs

echo "Generating RSA key pair for JWT tokens..."
# shellcheck disable=SC2164
cd src/main/resources/certs

# Generate RSA key pair
openssl genrsa -out keypair.pem 2048

# Extract public key
openssl rsa -in keypair.pem -pubout -out public.pem

# Convert private key to PKCS#8 format
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem

# Remove the keypair file as it's no longer needed
rm keypair.pem

echo "Key generation completed successfully!"
echo "Public key: src/main/resources/certs/public.pem"
echo "Private key: src/main/resources/certs/private.pem"

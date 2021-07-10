#!/usr/bin/env bash

curl -XPOST --header "content-type: application/json" -d @newProduct.json "http://localhost:8080/api/v1/product"

curl -XPUT --header "content-type: application/json" -d @updateProduct.json "http://localhost:8080/api/v1/product/666"
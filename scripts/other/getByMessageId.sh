#!/usr/bin/env bash

aws dynamodb get-item --table-name 'table-name' \
--profile sm-dev --key '{"messageId": {"S": "306ga591"}}' --consistent-read
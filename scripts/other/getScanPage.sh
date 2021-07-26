#!/usr/bin/env bash

# aws dynamodb scan --page-size 10 --table-name 'table-name' \
# --profile sm-dev --consistent-read

aws dynamodb scan \
    --table-name 'table-name' \
    --limit 10 \
    --profile sm-dev --debug
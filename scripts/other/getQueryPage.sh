#!/usr/bin/env bash

# aws dynamodb query --page-size 10  \
# --profile sm-dev --consistent-read

#     --starting-token '<VALUE_OF_NEXT_TOKEN_FROM_PREV_OPERATION>'
#     --filter-expression 'attribute_not_exists(updatedAt)' \
#     --scan-index-forward \


aws dynamodb query \
    --table-name 'table-name' \
    --key-condition-expression "messageId = :msgId" \
    --expression-attribute-values  '{":msgId":{"S":"306ga591"}}' \
    --limit 10 \
    --profile sm-dev
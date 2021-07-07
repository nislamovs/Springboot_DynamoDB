
# Cleanup dynamo db

aws dynamodb list-tables --endpoint-url http://dynamodb-local:8000 --region us-east-1  \
| jq .'TableNames[]' -r | xargs -ITABLE -n 1 aws dynamodb delete-table  \
--endpoint-url http://dynamodb-local:8000 --region us-east-1 --table-name TABLE

# Create tables

FILES=$(ls ./ | grep "_schema.json")
for file in $FILES
do
	echo "Processing schema : $file"

	aws dynamodb create-table --cli-input-json file://$file \
  --endpoint-url http://dynamodb-local:8000 --region us-east-1

done

# Populate those with data
FILES=$(ls ./ | grep "_content.json")
for file in $FILES
do
	echo "Processing content : $file"

aws dynamodb batch-write-item --request-items file://$file \
--endpoint-url http://dynamodb-local:8000 --region us-east-1

done
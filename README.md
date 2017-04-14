# Introduction #

This project is a data pipeline build for moving files from AWS S3 over to dynamoDB using AWS-Java-SDK.

# Prerequisites #

* JDK 1.8
* Maven 3


# Step-by-step Application Flow #

1. User execute the jar by passing bucket name and directory prefix in it.

2. Then script list all the files from bucket-name/directory-prefix and then it checks for csv files.

3. Then script download the csv files in temporary location and then the script perform the csv parsing.

4. After Csv parsing is completed, the system connect with dynamoDB and store the records in dynamoDB with batch size we have set in config file. Right now batch size used is 25 (recommended by AWS).

5. And that is it! We are done.  

6. I have also added code for downloading zip files from s3 and then extracting csv files and  uploading it to DynamoDB.But I haven't tested it yet. I will add this feature soon.

7. Also I can add Amazon SNS(Simple Notification Service) feature on our bucket so that whenever any file is uploaded to our bucket,and then our script's S3 bucket-listener will get invoked and csv or zip files will be uploaded to DynamoDB.
   
# Installation Steps#

1. Firstly we need to set following environment variables:  
export AWS_ACCESS_KEY_ID=xxxxxxxxxxx  
export AWS_SECRET_ACCESS_KEY=xxxxxxxxxxxxx  
export AWS_REGION=xxxxxxxxxxxxx

2. Please update the config variables in config file(located at src/main/resources/config.properties)
required variables are ->   files.tempPath(it is used to store temporary files)

3. If we want to change table name then cross-check with the table name defined in DataDto which is using dynamomapper (src/main/java/com/delphync/script/aws_data_pipeline/entity/DataDto.java)  
   Reference: http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.html
```
@DynamoDBTable(tableName = "new_bags")
public class DataDto {
```

4. Also provide read and write access-rights in DynamoDB for the user running this script. 

5. Next we can just move the csv file into S3.

6. Then update the region of DynamoDB in src/main/java/com/delphync/script/aws_data_pipeline/dynamodb/DynamoDBWriter.java
```
static {
		amazonDynamoDBClient.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
		mapper = new DynamoDBMapper(amazonDynamoDBClient);
		dynamoDB = new DynamoDB(amazonDynamoDBClient);
	}
```

7. Next build this project by using maven.
```
mvn clean install
```

8. Then execute the script with following command
```
java -jar target/aws-data-pipeline-0.0.1-SNAPSHOT-jar-with-dependencies.jar -b bucket-name -p directory-prefix
```  
For example:  
I have a bucket name testquery2 in s3 and in that bucket I have a folder test-aws-pipeline which consists of csv file.  
So My command will be  
```
java -jar target/aws-data-pipeline-0.0.1-SNAPSHOT-jar-with-dependencies.jar -b testquery2 -p test-aws-pipeline
```

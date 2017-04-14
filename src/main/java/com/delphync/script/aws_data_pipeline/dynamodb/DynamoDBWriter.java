package com.delphync.script.aws_data_pipeline.dynamodb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.delphync.script.aws_data_pipeline.entity.DataDto;
import com.delphync.script.aws_data_pipeline.utils.PropertyUtils;
import com.delphync.script.aws_data_pipeline.utils.Singletons;

/**
 * The class DynamoDBWriter
 * 
 * @author mattoop
 */
public class DynamoDBWriter {
	/**
	 * dynamoDB
	 */
	static DynamoDB dynamoDB = null;
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger("DynamoDBWriter");
	/**
	 * propertyUtils
	 */
	PropertyUtils propertyUtils;
	/**
	 * amazonDynamoDBClient
	 */
	static AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient();
	/**
	 * mapper
	 */
	static DynamoDBMapper mapper = null;
	static {
		amazonDynamoDBClient.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
		mapper = new DynamoDBMapper(amazonDynamoDBClient);
		dynamoDB = new DynamoDB(amazonDynamoDBClient);
	}

	/**
	 * DynamoDBWriter
	 */
	public DynamoDBWriter() {
		propertyUtils = Singletons.INSTANCE.propertyUtils();
	}

	/**
	 * createTable
	 * 
	 * @throws Exception
	 */
	public void createTable() throws Exception {
		CreateTableRequest request = new CreateTableRequest()
				.withAttributeDefinitions(new AttributeDefinition("number", ScalarAttributeType.N),
						new AttributeDefinition("postcode", ScalarAttributeType.S))
				.withKeySchema(new KeySchemaElement("postcode", KeyType.HASH),
						new KeySchemaElement("number", KeyType.RANGE))
				.withProvisionedThroughput(new ProvisionedThroughput(new Long(10), new Long(10)))
				.withTableName(propertyUtils.getTableName());
		try {
			CreateTableResult result = amazonDynamoDBClient.createTable(request);
			logger.log(Level.INFO, "Table created successfully " + propertyUtils.getTableName());
		} catch (AmazonServiceException e) {
			logger.log(Level.WARNING, propertyUtils.getTableName() + " table already exists");
			System.err.println();
			// System.exit(1);
		}
	}

	/**
	 * writeCsvToDynamoDB
	 * 
	 * @param tableData
	 */
	public void writeCsvToDynamoDB(List<String[]> tableData) {
		try {
			logger.log(Level.INFO, "Calling writeCsvToDynamoDB() ");
			int batchSize = propertyUtils.getBatchSize();
			int recordsWritten = 0;
			int totalrecordsWritten = 0;
			List<DataDto> dataDto = new ArrayList<>();
			for (String[] values : tableData) {
				DataDto dto = null;
				try {
					dto = new DataDto(values[propertyUtils.getPostCodeIndex()],
							Integer.parseInt(values[propertyUtils.getNumberIndex()]),
							values[propertyUtils.getCityIndex()], values[propertyUtils.getMunicipalityIndex()],
							values[propertyUtils.getProvinceIndex()], values[propertyUtils.getStreetIndex()],
							new BigDecimal(values[propertyUtils.getLatitudeIndex()]),
							new BigDecimal(values[propertyUtils.getLongtitudeIndex()]));
				} catch (ResourceNotFoundException e) {
					logger.log(Level.WARNING, "Error: The table \"%s\" can't be found.\n",
							propertyUtils.getTableName());
					System.err.println("Be sure that it exists and that you've typed its name correctly!");
					System.exit(1);
				} catch (AmazonServiceException e) {
					logger.log(Level.WARNING, e.getMessage());
					System.exit(1);
					continue;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				} catch (Exception e) {
					logger.log(Level.WARNING, e.getMessage());
					System.exit(1);
				}
				if (recordsWritten < batchSize) {
					dataDto.add(dto);
					recordsWritten++;
					continue;
				}
				totalrecordsWritten += recordsWritten;
				recordsWritten = 0;
				mapper.batchSave(dataDto);
				logger.log(Level.INFO, "Records Written: " + totalrecordsWritten);
				dataDto = new ArrayList<>();
			}
			if (dataDto != null && dataDto.size() > 0) {
				totalrecordsWritten += dataDto.size();
				mapper.batchSave(dataDto);
				logger.log(Level.INFO, "Total Records Written: " + totalrecordsWritten);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

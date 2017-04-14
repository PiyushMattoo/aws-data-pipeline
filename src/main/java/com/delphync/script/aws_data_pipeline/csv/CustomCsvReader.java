package com.delphync.script.aws_data_pipeline.csv;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.delphync.script.aws_data_pipeline.dynamodb.DynamoDBWriter;
import com.delphync.script.aws_data_pipeline.utils.Singletons;

import au.com.bytecode.opencsv.CSVReader;

/**
 * The class CustomCsvReader
 * 
 * @author mattop
 */
public class CustomCsvReader {
	/**
	 * dynamoDBWriter
	 */
	DynamoDBWriter dynamoDBWriter;
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger("CustomCsvReader");

	/**
	 * CustomCsvReader
	 * 
	 */
	public CustomCsvReader() {
		dynamoDBWriter = Singletons.INSTANCE.dynamoDBWriter();
	}

	/**
	 * readCsvFile
	 * 
	 * @param csvFile
	 * @throws Exception
	 */
	public void readCsvFile(File csvFile) throws Exception {
		logger.log(Level.INFO, "Loading Csv File " + csvFile.getPath());
		System.out.println(csvFile.getPath());
		CSVReader reader = new CSVReader(new FileReader(csvFile));
		String[] headers = reader.readNext();
		String[] item;
		List<String[]> tableItems = new ArrayList<>();
		while ((item = reader.readNext()) != null) {
			tableItems.add(item);
		}
		dynamoDBWriter.writeCsvToDynamoDB(tableItems);
	}

}

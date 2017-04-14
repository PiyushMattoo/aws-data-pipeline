package com.delphync.script.aws_data_pipeline.utils;

import java.io.Serializable;

import com.delphync.script.aws_data_pipeline.csv.CustomCsvReader;
import com.delphync.script.aws_data_pipeline.dynamodb.DynamoDBWriter;

/*
 * The enum Singletons
 * 
 * @author mattoop
 * */
public enum Singletons implements Serializable {

	INSTANCE;

	public static PropertyUtils propertyUtils;
	public static CustomCsvReader customCsvReader;
	public static DynamoDBWriter dynamoDBWriter;
	/*
	 * propertyUtils
	 * 
	 * 
	 * @returns PropertyUtils
	 */
	public PropertyUtils propertyUtils() {
		try {
			if (propertyUtils == null) {
				propertyUtils=new PropertyUtils();
			}
		} catch (Exception e) {
			System.out.println("#### getPropertyUtils " + e);
		}
		return propertyUtils;
	}
	/*
	 * csvReader
	 * 
	 * 
	 * @returns CustomCsvReader
	 */
	public CustomCsvReader csvReader() {
		try {
			if (customCsvReader == null) {
				customCsvReader=new CustomCsvReader();
			}
		} catch (Exception e) {
			System.out.println("#### getcustomCsvReader " + e);
		}
		return customCsvReader;
	}
	
	/*
	 * dynamoDBWriter
	 * 
	 * 
	 * @returns dynamoDBWriter
	 */
	public DynamoDBWriter dynamoDBWriter() {
		try {
			if (dynamoDBWriter == null) {
				dynamoDBWriter=new DynamoDBWriter();
			}
		} catch (Exception e) {
			System.out.println("#### dynamoDBWriter " + e);
		}
		return dynamoDBWriter;
	}
}

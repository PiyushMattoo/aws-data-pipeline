package com.delphync.script.aws_data_pipeline.entity;

import java.math.BigDecimal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
/**
* The class DataDto
* 
* @author mattoop
*/
@DynamoDBTable(tableName = "new_bags")
public class DataDto {
	String postcode;
	Integer number;
	String city;
	String municipality;
	String provice;
	String street;
	BigDecimal latitude;
	BigDecimal longitude;

	public DataDto() {
	}
	
	public DataDto(String postcode, Integer number, String city, String municipality, String provice, String street,
			BigDecimal latitude, BigDecimal longitude) {
		super();
		this.postcode = postcode;
		this.number = number;
		this.city = city;
		this.municipality = municipality;
		this.provice = provice;
		this.street = street;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@DynamoDBHashKey(attributeName = "postcode")
	public String getPostcode() {
		return postcode;
	}

	@DynamoDBRangeKey(attributeName = "number")
	public Integer getNumber() {
		return number;
	}

	@DynamoDBAttribute(attributeName = "city")
	public String getCity() {
		return city;
	}

	@DynamoDBAttribute(attributeName = "municipality")
	public String getMunicipality() {
		return municipality;
	}

	@DynamoDBAttribute(attributeName = "provice")
	public String getProvice() {
		return provice;
	}

	@DynamoDBAttribute(attributeName = "street")
	public String getStreet() {
		return street;
	}

	@DynamoDBAttribute(attributeName = "latitude")
	public BigDecimal getLatitude() {
		return latitude;
	}

	@DynamoDBAttribute(attributeName = "longitude")
	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

}

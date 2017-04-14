package com.delphync.script.aws_data_pipeline.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import com.delphync.script.aws_data_pipeline.csv.CustomCsvReader;

/*
 * The class PropertyUtils
 * 
 * @author mattoop
 * */
public class PropertyUtils implements Serializable {

	/*
	 * props
	 * 
	 */
	Properties props = new Properties();

	/**
	 * PropertyUtils
	 * 
	 * @throws Exception
	 */
	public PropertyUtils() throws Exception {
		InputStream inputStream = new FileInputStream(System.getenv("DELPHYNC_AWS_PIPELINE"));
		props.load(inputStream);
	}

	/**
	 * getFilesTempPath
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getFilesTempPath() throws Exception {
		return props.getProperty("files.tempPath");
	}
	
	/**
	 * getCityIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getCityIndex() throws Exception {
		return Integer.parseInt(props.getProperty("city.index"));
	}

	/**
	 * getBatchSize
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getBatchSize() throws Exception {
		return Integer.parseInt(props.getProperty("batchsize"));
	}

	/**
	 * getTableName
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getTableName() throws Exception {
		return props.getProperty("tablename");
	}

	/**
	 * getLatitudeIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getLatitudeIndex() throws Exception {
		return Integer.parseInt(props.getProperty("latitude.index"));
	}

	/**
	 * getLongtitudeIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getLongtitudeIndex() throws Exception {
		return Integer.parseInt(props.getProperty("longtitude.index"));
	}

	/**
	 * getProvinceIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getProvinceIndex() throws Exception {
		return Integer.parseInt(props.getProperty("province.index"));
	}

	/**
	 * getMunicipalityIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getMunicipalityIndex() throws Exception {
		return Integer.parseInt(props.getProperty("municipality.index"));
	}

	/**
	 * getStreetIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getStreetIndex() throws Exception {
		return Integer.parseInt(props.getProperty("street.index"));
	}

	/**
	 * getPostCodeIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getPostCodeIndex() throws Exception {
		return Integer.parseInt(props.getProperty("postcode.index"));
	}

	/**
	 * getNumberIndex
	 * 
	 * @return String
	 * @throws Exception
	 */
	public Integer getNumberIndex() throws Exception {
		return Integer.parseInt(props.getProperty("number.index"));
	}
}

package com.delphync.script.aws_data_pipeline.s3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.delphync.script.aws_data_pipeline.csv.CustomCsvReader;
import com.delphync.script.aws_data_pipeline.utils.PropertyUtils;
import com.delphync.script.aws_data_pipeline.utils.Singletons;

/**
 * The class S3JavaSDK
 * 
 * @author mattoop
 */
public class S3JavaSDK {
	/**
	 * s3
	 */
	final AmazonS3 s3 = new AmazonS3Client();

	/**
	 * propertyUtils
	 */
	PropertyUtils propertyUtils;

	/**
	 * customCsvReader
	 */
	CustomCsvReader customCsvReader;
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger("S3JavaSDK");

	/**
	 * S3JavaSDK
	 * 
	 */
	public S3JavaSDK() {
		// set your region for s3
		// s3.setRegion(Region.getRegion(Regions.US_WEST_2));
		propertyUtils = Singletons.INSTANCE.propertyUtils();
		customCsvReader = Singletons.INSTANCE.csvReader();
	}

	/**
	 * downloadFile
	 * 
	 * @param bucketName
	 * @param keyName
	 * @throws Exception
	 */
	public void downloadFile(String bucketName, String keyName) throws Exception {
		try {
			S3Object o = s3.getObject(bucketName, keyName);
			S3ObjectInputStream s3is = o.getObjectContent();

			keyName = keyName.replaceAll("/", File.separator);
			String completeFilePath = propertyUtils.getFilesTempPath() + File.separator + keyName;

			File parentFile = new File(completeFilePath).getParentFile();

			if (!parentFile.isDirectory()) {
				parentFile.mkdirs();
			}

			FileOutputStream fos = new FileOutputStream(new File(completeFilePath));
			byte[] read_buf = new byte[1024];
			int read_len = 0;
			while ((read_len = s3is.read(read_buf)) > 0) {
				fos.write(read_buf, 0, read_len);
			}
			s3is.close();
			fos.close();
			logger.log(Level.INFO, "CSV File Downloaded Succesfully  " + keyName);
			customCsvReader.readCsvFile(new File(completeFilePath));
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * listFiles
	 * 
	 * @param bucketName
	 * @param prefix
	 * @throws Exception
	 */
	public void listFiles(String bucketName, String prefix) throws Exception {
		logger.log(Level.INFO, "Calling S3 Rest API for listing files ");
		ObjectListing listing = s3.listObjects(bucketName, prefix);
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		while (listing.isTruncated()) {
			listing = s3.listNextBatchOfObjects(listing);
			summaries.addAll(listing.getObjectSummaries());
		}
		if (summaries == null || summaries.size() == 0) {
			logger.log(Level.INFO, "No Files found in " + bucketName + "/" + prefix);
			System.exit(1);
		}
		boolean hasCsvFile = false;
		for (S3ObjectSummary summary : summaries) {
			if (summary.getKey() != null && (summary.getKey().endsWith(".csv"))) {
				downloadFile(bucketName, summary.getKey());
				hasCsvFile = true;
			}
		}
		if (!hasCsvFile) {
			logger.log(Level.INFO, "No Csv Files found in " + bucketName + "/" + prefix);
			System.exit(1);
		}
	}

}
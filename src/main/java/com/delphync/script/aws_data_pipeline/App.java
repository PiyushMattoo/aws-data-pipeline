package com.delphync.script.aws_data_pipeline;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import com.delphync.script.aws_data_pipeline.dynamodb.DynamoDBWriter;
import com.delphync.script.aws_data_pipeline.s3.S3JavaSDK;

/**
 * The class App
 * 
 * @author mattoop
 *
 */
public class App {
	/**
	 * parser
	 */
	static CommandLineParser parser = new DefaultParser();
	/**
	 * options
	 */
	static Options options = new Options();
	/**
	 * line
	 */
	static CommandLine line;

	/**
	 * keyBucket
	 */
	static String keyBucket = "";
	/**
	 * keyPrefix
	 */
	static String keyPrefix = "";
	/**
	 * KEY_BUCKET,KEY_PREFIX
	 */
	private static final String[] KEY_BUCKET = { "b", "bucket-name" }, KEY_PREFIX = { "p", "prefix" };
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger("S3-To-DynamoDB");

	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (System.getenv("DELPHYNC_AWS_PIPELINE") != null) {
				S3JavaSDK s3JavaSDK = new S3JavaSDK();
				addCommandLineOptions();
				logger.log(Level.INFO, "Parsing Command line options");
				line = parser.parse(options, args);
				setUpCommandLineParameters();
				logger.log(Level.INFO, "Creating Table if it doesn't exists ");
				new DynamoDBWriter().createTable();
				logger.log(Level.INFO,
						"Calling s3JavaSDk with Bucket: " + keyBucket + " and Prefix Directory: " + keyPrefix);
				s3JavaSDK.listFiles(keyBucket, keyPrefix);
				logger.log(Level.INFO, "Done...");
			} else {
				System.out.println("Please add DELPHYNC_AWS_PIPELINE environment variable to launch your application.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * addCommandLineOptions
	 */
	static void addCommandLineOptions() {
		options.addOption(KEY_BUCKET[0], KEY_BUCKET[1], true, "your main S3 bucket name");
		options.addOption(KEY_PREFIX[0], KEY_PREFIX[1], true,
				"prefix of your s3 directory which you want to upload to dynamoDB");
	}

	/**
	 * printHelp
	 * 
	 * @param options
	 * @param printedRowWidth
	 * @param header
	 * @param footer
	 * @param spacesBeforeOption
	 * @param spacesBeforeOptionDescription
	 * @param displayUsage
	 * @param out
	 */
	static void printHelp(final Options options, final int printedRowWidth, final String header, final String footer,
			final int spacesBeforeOption, final int spacesBeforeOptionDescription, final boolean displayUsage,
			final OutputStream out) {
		final String commandLineSyntax = "java -jar target/aws-data-pipeline-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		final PrintWriter writer = new PrintWriter(out);
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(writer, printedRowWidth, commandLineSyntax, header, options, spacesBeforeOption,
				spacesBeforeOptionDescription, footer, displayUsage);
		writer.close();
	}

	/**
	 * setUpCommandLineParameters
	 * 
	 */
	static void setUpCommandLineParameters() {
		if ((line.getArgList() == null || line.getArgList().size() == 0) && !checkArgument()) {
			printHelp(options, 80, "AWS S3 to DynamoDB Pipeline Script", "End of AWS S3 to DynamoDB Pipeline Script", 5,
					3, true, System.out);
		} else {
			setCommandLineValue(KEY_BUCKET[1]);
			setCommandLineValue(KEY_PREFIX[1]);
		}
	}

	/**
	 * checkArgument
	 * 
	 * @return boolean
	 */
	static boolean checkArgument() {
		if (line.hasOption("b") && line.hasOption("p"))
			return true;
		else
			return false;
	}

	/**
	 * setCommandLineValue
	 * 
	 * @param key
	 */
	static void setCommandLineValue(String key) {
		String temp = "";
		if (line.hasOption(key)) {
			temp = line.getOptionValue(key);
		} else {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("JAR_NAME", options);
			System.out.println("Error in arguements");
			System.exit(0);
			return;
		}
		switch (key) {
		case "bucket-name":
			keyBucket = temp;
			break;
		case "prefix":
			keyPrefix = temp;
			break;
		}
	}

}

package com.delphync.script.aws_data_pipeline.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/**
* The class UnZipUtils
* 
* @author mattoop
*/
public class UnZipUtils {
	/*
	 * propertyUtils
	 * 
	 */
	private PropertyUtils propertyUtils;
	/**
	 * UnZipUtils
	 * 
	 */
	public UnZipUtils() {
		propertyUtils = Singletons.INSTANCE.propertyUtils();
	}
	/**
	 * unZip
	 * @param inputStream
	 * @return List<String>
	 */
	public List<String> unZip(InputStream inputStream) {
		try {
			String filename = new Date().getTime() + ".zip";
			String zipFileTempPath = propertyUtils.getFilesTempPath() + File.separator + filename;
			File pDir = new File(zipFileTempPath).getParentFile();
			if (!pDir.isDirectory()) {
				pDir.mkdirs();
			}

			try (FileOutputStream fos = new FileOutputStream(zipFileTempPath);) {
				int bytesCount = 0;
				byte[] buffer = new byte[4096];

				while ((bytesCount = inputStream.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesCount);
				}
				fos.flush();
				ZipFile zipFile = new ZipFile(zipFileTempPath);

				Enumeration<? extends ZipEntry> entries = zipFile.entries();

				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					InputStream stream = zipFile.getInputStream(entry);
					try {
						// TODO: processEachFile
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (stream != null)
							stream.close();
					}
				}
				new File(zipFileTempPath).delete();
			} catch (Exception e) {
				new File(zipFileTempPath).delete();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}
}

package com.richard.test.hadoop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 12, 2017 10:01:43 AM
*/
public class WriteFileToHdfs {
	
	public static void main(String[] args) throws Exception {
		String hdfsAddress = "hdfs://hadoop-master:9000";
		String filePath = "/user/hadoop/input/test.log";
		Configuration configuration = new Configuration();
		configuration.set("fs.defaultFS",hdfsAddress);
		readHdfsFile(filePath, configuration);
	}
	
	/**
	 * Read hdfs file from java api
	 * @param filePath
	 * @param conf
	 * @throws Exception
	 */
	public static void readHdfsFile(String filePath, Configuration conf) throws Exception {
		InputStream input = null;
		FileSystem hdfs = FileSystem.get(conf);
		BufferedReader buffer = null;
		Path file = new Path(filePath);
		input = hdfs.open(file);
		buffer = new BufferedReader(new InputStreamReader(input));
		String string = "";
		while((string = buffer.readLine()) != null) {
			System.out.println(string);
		}
	}

	public static void mainExecute(String[] args) {
		String localFile,hdfsUrl;
		if(args != null && args.length == 2) {
			localFile = args[0];
			hdfsUrl = args[1];
		} else {
			localFile = "test.log";
			hdfsUrl = "/user/hadoop/input/test.log";
		}
		writeLocalFile2Hdfs(localFile, hdfsUrl);
	}
	
	/**
	 * Use java code to write a file into hdfs
	 * @param localFile
	 * @param hdfsUrl
	 */
	public static void writeLocalFile2Hdfs(String localFile, String hdfsUrl) {
		FileInputStream input = null;
		OutputStream output = null;
		Configuration configuration = new Configuration();
		try {
			File file = new File(localFile);
			if(file.exists()) {
				input = new FileInputStream(file);
				
				FileSystem fs = FileSystem.get(URI.create(hdfsUrl), configuration);
				output = fs.create(new Path(hdfsUrl), new Progressable() {
					
					@Override
					public void progress() {
						System.out.println("****");
					}
				});
				byte[] buffer = new byte[1024];
				int readLength = 0;
				while((readLength = input.read(buffer)) != -1) {
					output.write(Arrays.copyOf(buffer, readLength));
				}
			} else {
				System.out.println("Cannot read the specify file!");
				System.exit(0);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(input);
			IOUtils.closeStream(output);
		}
	}

}

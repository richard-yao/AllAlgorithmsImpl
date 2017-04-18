package com.richard.test.spark.initialize;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 14, 2017 6:12:30 PM
*/
public class SimpleApp {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String logFile = "hdfs://hadoop-master:9000/user/hadoop/input/test.log";
		String masterAddress = "spark://hadoop-master:7077";
		SparkConf conf = new SparkConf().setAppName("Simple-application").setMaster(masterAddress);
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> logData = sc.textFile(logFile).cache();
		
		long numClicks = logData.filter(new Function<String, Boolean>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -4785811461539824734L;

			@Override
			public Boolean call(String arg0) throws Exception {
				return arg0.toLowerCase().contains("click");
			}
		}).count();
		
		long numGreens = logData.filter(new Function<String, Boolean>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -397674428172714969L;

			@Override
			public Boolean call(String arg0) {
				return arg0.toLowerCase().contains("green");
			}
		}).count();
		System.out.println("Lines with click: " + numClicks + ", lines with green: " + numGreens);
		sc.stop();
	}

}

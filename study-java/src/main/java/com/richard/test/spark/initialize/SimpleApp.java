package com.richard.test.spark.initialize;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.util.LongAccumulator;

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
		
		//JavaRDD filter method
		long numClicks = logData.filter(new Function<String, Boolean>() {
			
			private static final long serialVersionUID = -4785811461539824734L;

			@Override
			public Boolean call(String arg0) throws Exception {
				return arg0.toLowerCase().contains("click");
			}
		}).count();
		long numGreens = logData.filter(new Function<String, Boolean>() {
			
			private static final long serialVersionUID = -397674428172714969L;

			@Override
			public Boolean call(String arg0) {
				return arg0.toLowerCase().contains("green");
			}
		}).count();
		System.out.println("Lines with click: " + numClicks + ", lines with green: " + numGreens);
		
		//JavaRDD use map method
		JavaRDD<Long> result = logData.map(new Function<String, Long>() {

			private static final long serialVersionUID = -3131890969468932636L;

			@Override
			public Long call(String text) throws Exception {
				if(text != null) {
					return (long) text.split(" ").length;
				}
				return 0L;
			}
		});
		
		//be saved in memory after the first time it is computed
		result.persist(StorageLevel.MEMORY_ONLY());
		
		List<Long> listResult = null;
		//This operation may caused of  out of memory, because collect() fetches the entire RDD to a single machine in cluster
		//listResult = result.collect();
		if(result.count() > 10) {
			//if you only need to print a few elements of the RDD, a safer approach is to use the take()
			listResult = result.take(10);
			for (int i=0;i<listResult.size();i++) {
				System.out.println("The line number is " + (i+1) + " and the value is " + listResult.get(i));
			}
		}
		
		//普通的变量在spark集群中执行时是不可见的，无法在foreach中正常循环
		final LongAccumulator totalWordNumber = sc.sc().longAccumulator();
		result.foreach(new VoidFunction<Long>() {
			
			private static final long serialVersionUID = 2522757840211085553L;

			@Override
			public void call(Long parameter) throws Exception {
				totalWordNumber.add(parameter);
			}
		});
		
		//JavaRDD use reduce method
		Long wordNumber = result.reduce(new Function2<Long, Long, Long>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Long call(Long input1, Long input2) throws Exception {
				return input1 + input2;
			}
		});
		System.out.println("This text words' number is " + wordNumber + " and myself count value is "+totalWordNumber.value());
		
		sc.stop();
	}

}

package com.richard.test.spark.initialize;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.util.LongAccumulator;

import scala.Tuple2;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 14, 2017 6:12:30 PM
*/
public class SimpleApp {

	public static void main(String[] args) {
		String logFile, masterAddress;
		if(args != null && args.length == 2) {
			logFile = args[0];
			masterAddress = args[1];
		} else {
			logFile = "hdfs://hadoop-master:9000/user/hadoop/input/test.log";
			masterAddress = "spark://hadoop-master:7077";
		}
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
		
		useMapReduce(logData);
		useParallelize(sc);
		sc.stop();
	}
	
	public static void useMapReduce(JavaRDD<String> logData) {
		if(logData != null) {
			
			JavaPairRDD<String, Integer> pairCollect = logData.flatMapToPair(new PairFlatMapFunction<String, String, Integer>() {

				private static final long serialVersionUID = 2843331512319295449L;

				@Override
				public Iterator<Tuple2<String, Integer>> call(String line) throws Exception {
					String[] words = line.split(" ");
					List<Tuple2<String, Integer>> result = new ArrayList<Tuple2<String, Integer>>();
					for(int i=0;i<words.length;i++) {
						Tuple2<String, Integer> temp = new Tuple2<String, Integer>(words[i], 1);
						result.add(temp);
					}
					return result.iterator();
				}
			});
			
			/*JavaPairRDD<String, Integer> pairs = logData.mapToPair(new PairFunction<String, String, Integer>() {

				private static final long serialVersionUID = -5217153855000840482L;

				@Override
				public Tuple2<String, Integer> call(String input) throws Exception {
					Tuple2<String, Integer> addOne = new Tuple2<String, Integer>(input, 1);
					return addOne;
				}
			});*/
			JavaPairRDD<String, Integer> counts = pairCollect.reduceByKey(new Function2<Integer, Integer, Integer>() {

				private static final long serialVersionUID = 6650917574451071587L;

				@Override
				public Integer call(Integer count1, Integer count2) throws Exception {
					return count1 + count2;
				}
			});
			counts.sortByKey();
			
			System.out.println("---------------insertKeyNumberTodb() method start time:"+System.currentTimeMillis()+"---------------");
			insertKeyNumberToDb(counts);
			System.out.println("---------------insertKeyNumberTodb() method end time:"+System.currentTimeMillis()+"---------------");
			
			List<Tuple2<String, Integer>> collectResult = counts.collect();
			System.out.println("---------------Use collec() method start---------------");
			for(int i=0;i<collectResult.size();i++) {
				System.out.println("The key is "+collectResult.get(i)._1()+" and the value is "+collectResult.get(i)._2());
			}
			System.out.println("---------------Use collec() method end---------------");
			System.out.println("---------------Use collectAsMap() method start---------------");
			Map<String, Integer> result = counts.collectAsMap();
			for(String key:result.keySet()) {
				System.out.println("The key is " + key + " and the value is " + result.get(key));
			}
			System.out.println("---------------Use collectAsMap() method end---------------");
		}
	}
	
	public static void useParallelize(JavaSparkContext sc) {
		JavaRDD<Integer> dataSet = sc.parallelize(Arrays.asList(new Integer[] {1,2,3,4,5}));
		final LongAccumulator accumulator = sc.sc().longAccumulator("CountNumber");
		dataSet.foreach(new VoidFunction<Integer>() {

			private static final long serialVersionUID = 7241318845305003358L;

			@Override
			public void call(Integer value) throws Exception {
				accumulator.add(value);;
			}
		});
		System.out.println("Use parallelize function to convert a list to RDD and count the sum: " + accumulator.value());
		
		final LongAccumulator countNumber = sc.sc().longAccumulator("CountNumberWithPartition");
		dataSet.foreachPartition(new VoidFunction<Iterator<Integer>>() {

			private static final long serialVersionUID = -2417711780351402266L;

			@Override
			public void call(Iterator<Integer> partitionList) throws Exception {
				while(partitionList.hasNext()) {
					countNumber.add(partitionList.next());
				}
			}
		});
		System.out.println("Use foreachPartition to count the sum: " + countNumber.value());
	}
	
	public static void insertKeyNumberToDb(JavaPairRDD<String, Integer> counts) {
		counts.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Integer>>>() {

			private static final long serialVersionUID = 2638267996836384562L;

			@Override
			public void call(Iterator<Tuple2<String, Integer>> result) throws Exception {
				Connection connection = DriverManager.getConnection("jdbc:mysql://10.12.22.78:3306/statistic_data",
						"root", "tvu1p2ack3");
				Statement statement = null;
				try {
					statement = connection.createStatement();
					connection.setAutoCommit(false);
					String sql = "insert into word_count values('{0}', {1}) ";
					while (result.hasNext()) {
						Tuple2<String, Integer> temp = result.next();
						String executeSql = sql;
						executeSql = executeSql.replace("{0}", temp._1()).replace("{1}", String.valueOf(temp._2()));
						statement.addBatch(executeSql);
					}
					statement.executeBatch();
					connection.commit();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					statement.close();
					connection.close();
				}
			}
		});
	}
	
}

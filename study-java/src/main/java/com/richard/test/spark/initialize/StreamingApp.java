package com.richard.test.spark.initialize;

import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.dstream.DStream;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 19, 2017 3:46:20 PM
*/
public class StreamingApp {
	
	public static void main(String[] args) {
		String masterAddress;
		if(args != null && args.length == 1) {
			masterAddress = args[0];
		} else {
			masterAddress = "spark://hadoop-master:7077";
		}
		SparkConf conf = new SparkConf().setAppName("NetworkStreamingApplication").setMaster(masterAddress);
		//创建StreamingContext每秒处理数据
		StreamingContext ssc = new StreamingContext(conf, new Duration(1000));
		//使用StreamingContext创建DStream，DStream表示TCP源中的流数据. lines这个DStream表示接收到的服务器数据，每一行都是文本
		DStream<String> lines = ssc.socketTextStream("hadoop-master", 9999, StorageLevel.MEMORY_ONLY());
		
		
	}

}

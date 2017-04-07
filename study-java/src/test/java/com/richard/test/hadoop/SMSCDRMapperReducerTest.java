package com.richard.test.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 1:15:28 PM
*/
public class SMSCDRMapperReducerTest {
	
	private static final Logger logger = Logger.getLogger(SMSCDRMapper.class); 
	
	MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;
	
	public static void main(String[] args) {
		DOMConfigurator.configure("target/log4j.xml");
		logger.info("Start record log");
	}
	
	@Ignore
	@Before
	public void init() {
		
		SMSCDRMapper mapper = new SMSCDRMapper();
		SMSCDRReducer reducer = new SMSCDRReducer();
		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
	}
	
	@Ignore
	@Test
	public void testMapper() throws IOException {
		mapDriver.withInput(new LongWritable(), new Text("655209;1;796764372490213;804422938115889;6"));
		mapDriver.withOutput(new Text("6"), new IntWritable(1));
		mapDriver.runTest();
	}
	
	@Ignore
	@Test
	public void testReducer() throws IOException {
		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(1));
		values.add(new IntWritable(1));
		reduceDriver.withInput(new Text("6"), values);
		reduceDriver.withOutput(new Text("6"), new IntWritable(2));
		reduceDriver.runTest();
	}
	
	@Ignore
	@Test
	public void testMR() throws IOException {
		mapReduceDriver.withInput(new LongWritable(), new Text("655209;1;796764372490213;804422938115889;6"));
		mapReduceDriver.withInput(new LongWritable(), new Text("655209;1;796764372490213;804422938115889;6"));
		mapReduceDriver.withOutput(new Text("6"), new IntWritable(2));
		mapReduceDriver.runTest();
	}
}

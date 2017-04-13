package com.richard.test.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.richard.test.hadoop.wordcount.chain.WordCountChain;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 13, 2017 2:17:23 PM
*/
public class WordCountChainTest {
	
	MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;
	
	@Before
	public void init() {
		WordCountChain.TokenizerMapper mapper = new WordCountChain.TokenizerMapper();
		WordCountChain.IntSumReducer reducer = new WordCountChain.IntSumReducer();
		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
	}
	
	@Test
	public void testMapper() throws IOException {
		mapDriver.withInput(new LongWritable(), new Text("6"));
		mapDriver.withOutput(new Text("6"), new IntWritable(1));
		mapDriver.runTest();
	}
	
	@Test
	public void testCount() throws IOException {
		mapReduceDriver.withInput(new LongWritable(), new Text("6"));
		mapReduceDriver.withInput(new LongWritable(), new Text("6"));
		mapReduceDriver.withOutput(new Text("6"), new IntWritable(2));
		mapReduceDriver.runTest();
	}

}

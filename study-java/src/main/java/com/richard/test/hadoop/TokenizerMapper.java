package com.richard.test.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 6, 2017 11:43:27 AM
*/
public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

	IntWritable one = new IntWritable(1);
	Text word = new Text();
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		while(itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
		}
	}
}

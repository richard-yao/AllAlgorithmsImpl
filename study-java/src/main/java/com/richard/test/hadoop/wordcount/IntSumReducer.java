package com.richard.test.hadoop.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 6, 2017 1:13:06 PM
*/
public class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	IntWritable result = new IntWritable();
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for(IntWritable val:values) {
			sum += val.get();
		}
		result.set(sum);
		context.write(key, result);
	}
}

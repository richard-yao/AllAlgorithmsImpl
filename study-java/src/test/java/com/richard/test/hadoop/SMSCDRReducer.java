package com.richard.test.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 1:13:08 PM
*/
public class SMSCDRReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for(IntWritable value:values) {
			sum += value.get();
		}
		context.write(key, new IntWritable(sum));
	}
}

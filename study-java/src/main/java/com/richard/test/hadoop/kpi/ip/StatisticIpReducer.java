package com.richard.test.hadoop.kpi.ip;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.richard.test.hadoop.util.DBOutputWritable;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 5:56:48 PM
*/
public class StatisticIpReducer extends Reducer<Text, IntWritable, DBOutputWritable, NullWritable> {

	@Override
	public void setup(Context context) {
		
	}
	
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for(IntWritable val:values) {
			sum += val.get();
		}
		context.write(new DBOutputWritable(key.toString(), sum), NullWritable.get());
	}
	
	@Override
	public void cleanup(Context context) {
		
	}
}

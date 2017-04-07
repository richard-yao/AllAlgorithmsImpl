package com.richard.test.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 1:07:32 PM
*/
public class SMSCDRMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	private Text status = new Text();
	private final static IntWritable addOne = new IntWritable(1);
	
	/**
	 * Returns the SMS status code and its count
	 */
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if(value != null) {
			//655209;1;796764372490213;804422938115889;6 is the sample record format
			String[] line = value.toString().split(";");
			if(Integer.parseInt(line[1]) == 1) {
				status.set(line[4]);
				context.write(status, addOne);
			}
		}
	}
}

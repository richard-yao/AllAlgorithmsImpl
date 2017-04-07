package com.richard.test.hadoop.kpi.browser;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.richard.test.hadoop.kpi.util.Kpi;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 4:49:38 PM
*/
public class StatisticBrowserMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private IntWritable addOne = new IntWritable(1);
	private Text browser = new Text();
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		Kpi kpi = Kpi.parse(value.toString());
		if(kpi.getIs_validate()) {
			browser.set(kpi.getUser_agent());
			context.write(browser, addOne);
		}
	}
}

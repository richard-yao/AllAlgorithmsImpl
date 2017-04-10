package com.richard.test.hadoop.kpi.browser;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.richard.test.hadoop.util.BaseDriver;
import com.richard.test.hadoop.util.JobInitModel;
/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 4:57:02 PM
*/
public class BrowserMR {

	public void submitJob(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		String[] input = null;
		String output = null;
		if(args.length > 2) {
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		} else if(args.length == 2) {
			input = new String[]{args[0]};
			output = args[1];
		} else {
			input = new String[]{"hdfs://hadoop-master:9000/user/hadoop/accesslog/*"};
			output = "hdfs://hadoop-master:9000/user/hadoop/accesslog-out/kpi/browser";
		}
		Configuration conf = new Configuration();
		String jobName = "kpi-browser-statistic";
		JobInitModel model = new JobInitModel(input, output, conf, null, jobName,
				BrowserMR.class, StatisticBrowserMapper.class, StatisticBrowserReducer.class, StatisticBrowserReducer.class, 
				Text.class, IntWritable.class);
		BaseDriver.submitJob(new JobInitModel[]{model});
	}

}

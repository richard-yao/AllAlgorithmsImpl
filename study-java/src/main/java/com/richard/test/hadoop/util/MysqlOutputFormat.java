package com.richard.test.hadoop.util;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 11, 2017 2:51:58 PM
*/
public class MysqlOutputFormat extends FileOutputFormat<Text, IntWritable> {

	@Override
	public RecordWriter<Text, IntWritable> getRecordWriter(TaskAttemptContext paramTaskAttemptContext)
			throws IOException, InterruptedException {
		return new MysqlRecordWriter();
	}
}

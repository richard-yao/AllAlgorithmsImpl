package com.richard.test.hadoop.sort;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;


/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 13, 2017 10:37:30 AM
*/
public class SortNumberFile extends Configured implements Tool {
	
	public static class SortNumberMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
		
		private static IntWritable number = new IntWritable();
		
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			number.set(Integer.parseInt(line));
			context.write(number, new IntWritable(1));
		}
	}
	
	public static class SortNumberReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
		
		private static IntWritable lineNumber = new IntWritable();
		
		@Override
		public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			for(IntWritable temp:values) {
				System.out.println(temp);
				context.write(lineNumber, key);
			}
			lineNumber = new IntWritable(lineNumber.get() + 1);
		}
	}
	
	public static class SortPartition extends Partitioner<IntWritable, IntWritable> {

		@Override
		public int getPartition(IntWritable key, IntWritable value, int numPartitions) {
			int maxNumber = 739;
			int bound = maxNumber / numPartitions + 1;
			int keyNumber = key.get();
			for(int i=0;i<numPartitions;i++) {
				if(keyNumber < bound * (i + 1) && keyNumber >= bound * i) {
					return i;
				}
			}
			return -1;
		}
		
	}

	@Override
	public int run(String[] paramters) throws Exception {
		String input, output;
		if(paramters != null && paramters.length == 2) {
			input = paramters[0];
			output = paramters[1];
		} else {
			input = "hdfs://hadoop-master:9000/user/hadoop/definedInput";
			output = "hdfs://hadoop-master:9000/user/hadoop/definedOutput";
		}
		Configuration conf = new Configuration();
		conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 10);
		conf.setInt("mapreduce.tasktracker.reduce.tasks.maximum", 10);

		Job job = Job.getInstance(conf, "sort-number-file");
		job.setJarByClass(SortNumberFile.class);
		job.setMapperClass(SortNumberMapper.class);
		job.setReducerClass(SortNumberReducer.class);
		job.setPartitionerClass(SortPartition.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		return job.waitForCompletion(true) ? 0 : 1;
	}

}

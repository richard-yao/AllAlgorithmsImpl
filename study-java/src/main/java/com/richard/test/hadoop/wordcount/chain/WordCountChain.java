package com.richard.test.hadoop.wordcount.chain;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import com.richard.test.hadoop.wordcount.WordCount;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 13, 2017 1:09:54 PM
*/
public class WordCountChain extends Configured implements Tool {
	
	public static class TokenizerMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		IntWritable one = new IntWritable(1);
		Text word = new Text();
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while(itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}
	
	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

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
	
	/**
	 * Used to filter the reducer result with specify condition
	 * @author RichardYao
	 */
	public static class RangeFilterMapper extends Mapper<Text, IntWritable, Text, IntWritable> {
		
		public void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
			Configuration configuration = context.getConfiguration();
			int numberLimit = Integer.parseInt(configuration.get("filerNumberLimit"));
			if(value.get() > numberLimit) {
				context.write(key, value);
			}
		}
	}

	@Override
	public int run(String[] paramters) throws Exception {
		if(paramters != null && paramters.length == 3) {
			Configuration configuration = new Configuration();
			configuration.setInt("filerNumberLimit", Integer.parseInt(paramters[2]));
			WordCount.deleteExistOutputDirectory(configuration, paramters[1]);
			Job job = Job.getInstance(configuration, "word-count-with-number-over-20");
			job.setJarByClass(WordCountChain.class);
			job.setInputFormatClass(TextInputFormat.class);
			
			ChainMapper.addMapper(job, TokenizerMapper.class, LongWritable.class, Text.class
					, Text.class, IntWritable.class, configuration);
			ChainReducer.setReducer(job, IntSumReducer.class, Text.class, IntWritable.class
					, Text.class, IntWritable.class, configuration);
			ChainReducer.addMapper(job, RangeFilterMapper.class, Text.class, IntWritable.class
					, Text.class, IntWritable.class, configuration);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(job, new Path(paramters[0]));
			FileOutputFormat.setOutputPath(job, new Path(paramters[1]));
			return job.waitForCompletion(true) ? 0 : 1;
		} else {
			System.out.println("Parameters wrong!");
		}
		return 0;
	}
		
	
}

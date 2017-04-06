package com.richard.test.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 6, 2017 1:16:55 PM
*/
public class WordCount {

	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration();
		String[] otherArgs = new GenericOptionsParser(config, args).getRemainingArgs();
		if(otherArgs.length != 2) {
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		deleteExistOutputDirectory(config, args[1]);
		
		@SuppressWarnings("deprecation")
		Job job = new Job(config, "wordcount");
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0:1);
	}
	
	/**
	 * Delete oupt directory if aleardy exist this directory
	 * @param config
	 * @param output
	 * @throws IOException
	 */
	public static void deleteExistOutputDirectory(Configuration config, String output) throws IOException {
		Path outputPath = new Path(output);
		outputPath.getFileSystem(config).delete(outputPath, true);
	}
}

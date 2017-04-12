package com.richard.test.hadoop.wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.richard.test.hadoop.kpi.browser.BrowserMR;
import com.richard.test.hadoop.kpi.ip.IpMR;
import com.richard.test.hadoop.util.BaseDriver;
import com.richard.test.hadoop.util.JobInitModel;
import com.richard.test.hadoop.util.WriteFileToHdfs;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 6, 2017 1:16:55 PM
*/
public class WordCount {
	
	public static void main(String[] args) throws Exception {
		if(args.length > 0) {
			BrowserMR mr = new BrowserMR();
			mr.submitJob(args);
		}
		IpMR ipMR = new IpMR();
		ipMR.submitJob(args);
		WriteFileToHdfs.mainExecute(args);
		//ipMR.submitJobWithDefinedMysqlOutput(args);
		//wordcountMR(args);
		//wordcountJar(args);
	}
	
	public static void wordcountMR(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		if(args.length != 2) {
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		String[] input = new String[]{args[0]};
		String output = args[1];
		Configuration conf = new Configuration();
		String jobName = "wordcount-changed";
		JobInitModel model = new JobInitModel(input, output, conf, null, jobName,
				WordCount.class, TokenizerMapper.class, IntSumReducer.class, IntSumReducer.class, 
				Text.class, IntWritable.class);
		BaseDriver.submitJob(new JobInitModel[]{model});
	}

	public static void wordcountJar(String[] args) throws Exception {
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

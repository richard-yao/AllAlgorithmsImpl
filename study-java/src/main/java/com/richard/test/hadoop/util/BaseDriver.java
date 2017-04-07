package com.richard.test.hadoop.util;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 2:55:34 PM
*/
public class BaseDriver {

	public static void submitJob(JobInitModel[] jobs) throws IOException, ClassNotFoundException, InterruptedException {
		if(jobs != null) {
			for(JobInitModel model:jobs) {
				//Check the output path and delete existed directory
				FileSystem fs = FileSystem.get(model.getConfiguration());
				Path outputPath = new Path(model.getOutputPath());
				if(fs.exists(outputPath)) {
					fs.delete(outputPath, true);
				}
				
				//Initialize MR job
				Job job;
				if(model.getJob() == null) {
					job = Job.getInstance(model.getConfiguration(), model.getJobName());
				} else {
					job = model.getJob();
				}
				job.setJarByClass(model.getJarClass());
				//Set input paths
				String[] inputPaths = model.getInputPaths();
				Path[] inPaths = new Path[inputPaths.length];
				for(int i=0;i<inputPaths.length;i++) {
					inPaths[i] = new Path(inputPaths[i]);
				}
				FileInputFormat.setInputPaths(job, inPaths);
				job.setInputFormatClass(TextInputFormat.class);
				
				job.setMapperClass(model.getMapper());
				if(model.getCombiner() != null) {
					job.setCombinerClass(model.getCombiner());
				}
				if(model.getReducer() != null) {
					job.setReducerClass(model.getReducer());
					job.setOutputKeyClass(model.getReducerOutputKey());
					job.setOutputValueClass(model.getReducerOutputValue());
				}
				
				FileOutputFormat.setOutputPath(job, outputPath);
				job.setOutputFormatClass(TextOutputFormat.class);
				job.waitForCompletion(true);
			}
		}
	}
}

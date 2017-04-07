package com.richard.test.hadoop.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 3:04:13 PM
*/
public class JobInitModel {

	private String[] inputPaths; //source data files path in hdfs
	private String outputPath; //process completely result output path in hdfs
	private Configuration configuration; //MR program used configuration
	private Job job; //MR job
	private String jobName; //MR job name
	private Class<?> jarClass; //MR program's main class
	@SuppressWarnings("rawtypes")
	private Class<? extends Mapper> mapper; //the specify class extends Mapper
	@SuppressWarnings("rawtypes")
	private Class<? extends Reducer> combiner; //the specify class extends Reducer and defined as combiner
	@SuppressWarnings("rawtypes")
	private Class<? extends Reducer> reducer; //the specify class extends Reducer
	private Class<?> reducerOutputKey; //reducer output key class
	private Class<?> reducerOutputValue; //reducer output value class
	
	public JobInitModel() {
		
	}

	
	@SuppressWarnings("rawtypes")
	public JobInitModel(String[] inputPaths, String outputPath, Configuration configuration, Job job, String jobName,
			Class<?> jarClass, Class<? extends Mapper> mapper, Class<? extends Reducer> combiner,
			Class<? extends Reducer> reducer, Class<?> reducerOutputKey, Class<?> reducerOutputValue) {
		this.inputPaths = inputPaths;
		this.outputPath = outputPath;
		this.configuration = configuration;
		this.job = job;
		this.jobName = jobName;
		this.jarClass = jarClass;
		this.mapper = mapper;
		this.combiner = combiner;
		this.reducer = reducer;
		this.reducerOutputKey = reducerOutputKey;
		this.reducerOutputValue = reducerOutputValue;
	}



	public String[] getInputPaths() {
		return inputPaths;
	}

	public void setInputPaths(String[] inputPaths) {
		this.inputPaths = inputPaths;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Class<?> getJarClass() {
		return jarClass;
	}

	public void setJarClass(Class<?> jarClass) {
		this.jarClass = jarClass;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends Mapper> getMapper() {
		return mapper;
	}

	@SuppressWarnings("rawtypes")
	public void setMapper(Class<? extends Mapper> mapper) {
		this.mapper = mapper;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends Reducer> getCombiner() {
		return combiner;
	}

	@SuppressWarnings("rawtypes")
	public void setCombiner(Class<? extends Reducer> combiner) {
		this.combiner = combiner;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends Reducer> getReducer() {
		return reducer;
	}

	@SuppressWarnings("rawtypes")
	public void setReducer(Class<? extends Reducer> reducer) {
		this.reducer = reducer;
	}

	public Class<?> getReducerOutputKey() {
		return reducerOutputKey;
	}

	public void setReducerOutputKey(Class<?> reducerOutputKey) {
		this.reducerOutputKey = reducerOutputKey;
	}

	public Class<?> getReducerOutputValue() {
		return reducerOutputValue;
	}

	public void setReducerOutputValue(Class<?> reducerOutputValue) {
		this.reducerOutputValue = reducerOutputValue;
	}
}

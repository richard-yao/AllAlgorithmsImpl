package com.richard.test.hadoop.kpi.ip;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;

import com.richard.test.hadoop.util.DBOutputWritable;
import com.richard.test.hadoop.util.MysqlOutputFormat;

/**
 * @author RichardYao richardyao@tvunetworks.com
 * @date Apr 7, 2017 6:01:09 PM
 */
public class IpMR extends Configured implements Tool {
	
	public final static Configuration configuration = new Configuration();
	public final static String configPath = "/user/hadoop/config/config.properties";
	
	public int submitJob(String[] args) throws Exception {
		String input = "hdfs://hadoop-master:9000/user/hadoop/accesslog/*";
		Configuration conf = new Configuration();
		conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 10);
		conf.setInt("mapreduce.tasktracker.reduce.tasks.maximum", 10);
		DBConfiguration.configureDB(conf, "com.mysql.jdbc.Driver", "jdbc:mysql://10.12.22.78:3306/statistic_data", "root", "tvu1p2ack3");

		Job job = Job.getInstance(conf, "statistic-ip-into-db");
		job.setJarByClass(IpMR.class);
		job.setMapperClass(StatisticIpMapper.class);
		job.setReducerClass(StatisticIpReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(DBOutputWritable.class);
		job.setOutputValueClass(NullWritable.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(DBOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		DBOutputFormat.setOutput(job, "ip_statistic", new String[] {"IP", "NUMBER"});
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	/**
	 * 使用自定义的MysqlOutputFormat配合c3p0连接池将数据写入数据库中
	 * @param args
	 * @throws Exception
	 */
	public int submitJobWithDefinedMysqlOutput(String[] args) throws Exception {
		Configuration conf = IpMR.configuration;
		/*conf.setInt("mapreduce.tasktracker.map.tasks.maximum", 20);
		conf.setInt("mapreduce.tasktracker.reduce.tasks.maximum", 50);*/
		
		String input = "hdfs://hadoop-master:9000/user/hadoop/accesslog/*";
		String output = "hdfs://hadoop-master:9000/user/hadoop/accesslog/output";
		FileSystem fs = FileSystem.get(conf);
		Path outputPath = new Path(output);
		if(fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}

		Job job = Job.getInstance(conf, "statistic-ip-into-db");
		job.setJarByClass(IpMR.class);
		job.setMapperClass(StatisticIpMapper.class);
		job.setReducerClass(StatisticIpDbReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(MysqlOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		MysqlOutputFormat.setOutputPath(job, new Path(output));
		return job.waitForCompletion(true) ? 0 : 1;
	}

	@Override
	public int run(String[] arg0) throws Exception {
		//return submitJob(arg0);
		return submitJobWithDefinedMysqlOutput(arg0);
	}
}

package com.richard.test.hadoop.kpi.ip;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import com.richard.test.hadoop.util.DBOutputWritable;
import com.richard.test.hadoop.util.MysqlOutputFormat;

/**
 * @author RichardYao richardyao@tvunetworks.com
 * @date Apr 7, 2017 6:01:09 PM
 */
public class IpMR {

	public void submitJob(String[] args) throws Exception {
		String input = "hdfs://hadoop-master:9000/user/hadoop/accesslog/*";
		Configuration conf = new Configuration();
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
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
	/**
	 * 此方法不可用，因为MapReducer程序无法读取本地系统中的配置文件，导致数据库连接没法进行
	 * @param args
	 * @throws Exception
	 */
	public void submitJobWithDefinedMysqlOutput(String[] args) throws Exception {
		String input = "hdfs://hadoop-master:9000/user/hadoop/accesslog/*";
		String output = "hdfs://hadoop-master:9000/user/hadoop/accesslog/output";
		Configuration conf = new Configuration();

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
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

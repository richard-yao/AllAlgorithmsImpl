package com.richard.test.hadoop.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * @author RichardYao richardyao@tvunetworks.com
 * @date Apr 11, 2017 4:42:58 PM
 */
public class MysqlRecordWriter extends RecordWriter<Text, IntWritable> {

	@Override
	public void close(TaskAttemptContext paramTaskAttemptContext) throws IOException, InterruptedException {

	}

	@Override
	public void write(Text paramK, IntWritable paramV) throws IOException, InterruptedException {
		// write paramK and paramV into mysql
		Connection conn = DBManager.getInstance().getConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			conn.setAutoCommit(false);
			String sql = "insert into ip_statistic values('" + paramK.toString() + "', " + paramV.get() + ")";
			statement.execute(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

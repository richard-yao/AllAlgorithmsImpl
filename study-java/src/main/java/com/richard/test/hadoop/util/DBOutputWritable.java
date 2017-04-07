package com.richard.test.hadoop.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 7, 2017 5:46:09 PM
*/
public class DBOutputWritable implements Writable, DBWritable{

	private String ip;
	private int number;
	
	public DBOutputWritable(String ip, int number) {
		this.ip = ip;
		this.number = number;
	}
	
	@Override
	public void readFields(ResultSet rs) throws SQLException {
		this.ip = rs.getString(1);
		this.number = rs.getInt(2);
	}

	@Override
	public void write(PreparedStatement ps) throws SQLException {
		ps.setString(1, ip);
		ps.setInt(2, number);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		
	}

}

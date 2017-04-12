package com.richard.test.hadoop.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author RichardYao richardyao@tvunetworks.com
 * @date Apr 11, 2017 4:53:44 PM
 */
public class DBManager {

	private static Logger logger = Logger.getLogger(DBManager.class);
	public static DBManager dbManager;
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("Hadoop-integrated");

	private DBManager() {

	}

	public synchronized static DBManager getInstance() {
		if(dbManager == null) {
			dbManager = new DBManager();
			ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();
			cpds.setJdbcUrl(configurationUtil.getDbUrl());
			try {
				cpds.setDriverClass(configurationUtil.getDbDriver());
			} catch (PropertyVetoException e) {
				logger.error(e, e);
			}
			cpds.setUser(configurationUtil.getDbUser());
			cpds.setPassword(configurationUtil.getDbPasswd());
			cpds.setMaxPoolSize(Integer.valueOf(configurationUtil.getDbMaxPoolSize()));
			cpds.setMinPoolSize(Integer.valueOf(configurationUtil.getDbMinPoolSize()));
			cpds.setAcquireIncrement(Integer.valueOf(configurationUtil.getDbAcquireIncrement()));
			cpds.setInitialPoolSize(Integer.valueOf(configurationUtil.getDbInitialPoolSize()));
			cpds.setMaxIdleTime(Integer.valueOf(configurationUtil.getDbMaxIdleTime()));
			cpds.setMaxStatements(50);
		}
		return dbManager;
	}

	public Connection getConnection() {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			logger.error(e, e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		DOMConfigurator.configure("target/log4j.xml");
		String sql = " SELECT * FROM `ip_statistic` ORDER BY NUMBER desc limit 1 ";
		Connection connection = DBManager.getInstance().getConnection();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(1) + "---" + rs.getInt(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

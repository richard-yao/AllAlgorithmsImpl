package com.richard.test.hadoop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Apr 11, 2017 4:59:49 PM
*/
public class ConfigurationUtil {
	
	private static Logger logger = Logger.getLogger(ConfigurationUtil.class);
	private static ConfigurationUtil configurationUtil;
	
	private String dbUrl;
	private String dbUser;
	private String dbPasswd;
	private String dbDriver;
	private String dbMinPoolSize;
	private String dbMaxPoolSize;
	private String dbAcquireIncrement;
	private String dbInitialPoolSize;
	private String dbMaxIdleTime;
	
	private ConfigurationUtil() {
		Properties properties = new Properties();
		String fileName = "config.properties";
		getPropertiesFile(fileName, properties);
		this.dbUrl = properties.getProperty("db_url");
		this.dbUser = properties.getProperty("db_user");
		this.dbPasswd = properties.getProperty("db_password");
		this.dbDriver = properties.getProperty("c3p0.driver");
		this.dbMinPoolSize = properties.getProperty("c3p0.minPoolSize");
		this.dbMaxPoolSize = properties.getProperty("c3p0.maxPoolSize");
		this.dbAcquireIncrement = properties.getProperty("c3p0.acquireIncrement");
		this.dbInitialPoolSize = properties.getProperty("c3p0.initialPoolSize");
		this.dbMaxIdleTime = properties.getProperty("c3p0.maxIdleTime");
	}
	
	/**
	 * load all properties files to cache
	 * @param fileName, config file's name
	 * @param properties, used properties instance
	 */
	private void getPropertiesFile(String fileName, Properties properties) {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		logger.info(inputStream);
		try {
			if(inputStream != null){
				properties.load(inputStream);
			}else{
				String filename = System.getProperty("user.dir")+File.separator+fileName;
				logger.info("filename:"+filename);
				File file = new File(filename);
				if(file != null && file.exists()){
					FileInputStream in = new FileInputStream(file);
					properties.load(in);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	public synchronized static ConfigurationUtil getInstance() {
		if(configurationUtil == null) {
			configurationUtil = new ConfigurationUtil();
		}
		return configurationUtil;
	}
	
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPasswd() {
		return dbPasswd;
	}

	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbMinPoolSize() {
		return dbMinPoolSize;
	}

	public void setDbMinPoolSize(String dbMinPoolSize) {
		this.dbMinPoolSize = dbMinPoolSize;
	}

	public String getDbMaxPoolSize() {
		return dbMaxPoolSize;
	}

	public void setDbMaxPoolSize(String dbMaxPoolSize) {
		this.dbMaxPoolSize = dbMaxPoolSize;
	}

	public String getDbAcquireIncrement() {
		return dbAcquireIncrement;
	}

	public void setDbAcquireIncrement(String dbAcquireIncrement) {
		this.dbAcquireIncrement = dbAcquireIncrement;
	}

	public String getDbInitialPoolSize() {
		return dbInitialPoolSize;
	}

	public void setDbInitialPoolSize(String dbInitialPoolSize) {
		this.dbInitialPoolSize = dbInitialPoolSize;
	}

	public String getDbMaxIdleTime() {
		return dbMaxIdleTime;
	}

	public void setDbMaxIdleTime(String dbMaxIdleTime) {
		this.dbMaxIdleTime = dbMaxIdleTime;
	}

}

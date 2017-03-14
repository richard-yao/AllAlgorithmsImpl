package com.richard.test.spring.annotation.bean;
/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 13, 2017 11:11:21 AM
*/
public class LogonService {
	
	public UserDao userDao;
	
	public LogDao logDao;
	
	public void printOut() {
		userDao.printOut();
		logDao.printOut();
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}
}

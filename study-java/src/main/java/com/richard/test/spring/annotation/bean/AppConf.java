package com.richard.test.spring.annotation.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 13, 2017 11:10:33 AM
*/
@Configuration
public class AppConf {
	
	@Bean
	public UserDao userDao() {
		return new UserDao();
	}
	
	@Bean
	public LogDao logDao() {
		return new LogDao();
	}
	
	@Bean
	public LogonService logonService() {
		LogonService logonService = new LogonService();
		logonService.setUserDao(userDao());
		logonService.setLogDao(logDao());
		return logonService;
	}

}

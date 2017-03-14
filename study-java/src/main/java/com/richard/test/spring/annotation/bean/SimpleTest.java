package com.richard.test.spring.annotation.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 13, 2017 11:18:11 AM
*/
public class SimpleTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(AppConf.class); 
		applicationContext.refresh();
		
		LogonService logonService = (LogonService) applicationContext.getBean("logonService");
		logonService.printOut();
	}

}

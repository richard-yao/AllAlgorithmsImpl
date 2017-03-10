package com.richard.test.spring.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 10, 2017 10:41:15 AM
* 使用自定义组件，这里必须把AnnotationUse加上Configuration注解，否则会导致Spring扫描InjectClass时找不到内部类定义
* 或者不使用注解而单独在applicationContext容器中将AnnotationUse, InjectClass都注册了
*/
@Configuration
public class AnnotationUse {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(AnnotationUse.class); 
		//applicationContext.register(InjectClass.class);
		applicationContext.register(SimpleTest.class); //需要单独在applicationContext.register(class)中注册后才可以正常使用applicationContext.getBean(class)来实例化
		applicationContext.refresh();
		InjectClass injectClass = applicationContext.getBean(InjectClass.class);
		injectClass.print();
		SimpleTest beanTest = applicationContext.getBean(SimpleTest.class);
		beanTest.printOut();
		applicationContext.close();
	}
	
	/**
	 * @author RichardYao
	 * applicationContext.getBean(Class ...)无法实例化非静态内部类
	 */
	@MyComponent
	public static class InjectClass {
		
		public void print() {
			System.out.println("Hello world!");
		}
	}

}

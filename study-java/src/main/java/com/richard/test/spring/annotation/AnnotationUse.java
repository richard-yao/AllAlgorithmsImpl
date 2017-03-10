package com.richard.test.spring.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 10, 2017 10:41:15 AM
* 使用自定义组件，这里必须把AnnotationUse加上Configuration注解，否则会导致Spring扫描InjectClass时找不到内部类定义
*/
@Configuration
public class AnnotationUse {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(AnnotationUse.class);
		applicationContext.refresh();
		InjectClass injectClass = applicationContext.getBean(InjectClass.class);
		injectClass.print();
		SimpleTest beanTest = applicationContext.getBean(SimpleTest.class); //这里会抛出异常：No qualifying bean of type [com.richard.test.spring.annotation.SimpleTest] is defined
		beanTest.printOut();
		applicationContext.close();
	}
	
	/**
	 * @author RichardYao
	 * 这里必须是静态类才能使得applicationContext.getBean()方法调用正常, why?
	 */
	@MyComponent
	public static class InjectClass {
		
		public void print() {
			System.out.println("Hello world!");
		}
	}

}

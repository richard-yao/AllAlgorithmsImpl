package com.richard.test.java.feature;

import java.lang.reflect.Modifier;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 10, 2017 1:17:10 PM
*/
public class ReflectInnerClassTest {

	public static void main(String[] args) {
		ReflectInnerClass ric = ReflectInnerClass.getInstance();
		try {
			reflectInnerClass(ric);
			/*Class clazz = ric.getClass();
			clazz.getConstructor(ric.getClass()).newInstance(ric);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void reflectInnerClass(ReflectInnerClass ric) throws Exception {
		Class clazz = ric.getClass();
		Class classes[] = clazz.getDeclaredClasses(); //getClasses()只能得到访问级别为public的内部类，而getDeclaredClasses()则能得到所有声明了的内部类
		for(Class c : classes) {
			int i = c.getModifiers();
			String s = Modifier.toString(i);
			if(s.contains("static")) {
				c.getConstructor().newInstance();
			} else {
				c.getConstructor(ric.getClass()).newInstance(ric);
			}
		}
		
		//由于匿名内部类没有构建器，因此无法创建实例，也无法直接访问其中的方法，但可以通过下面的方式巧妙的执行其中的方法或成员变量
		Runnable runnable = (Runnable) clazz.getField("runnable").get(ric);
		runnable.run();
	}

}

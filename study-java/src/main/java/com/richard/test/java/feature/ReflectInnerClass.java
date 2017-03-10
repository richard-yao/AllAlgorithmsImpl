package com.richard.test.java.feature;
/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 10, 2017 1:09:55 PM
* 内部类： 实例内部类，静态内部类，匿名内部类
*/
public class ReflectInnerClass {
	
	private static ReflectInnerClass reflectInnerClass = null;
	
	//私有构造器防止外部类使用反射创建新的实例
	private ReflectInnerClass() {
		
	}
	
	//懒汉式单例，双重锁定防止并发
	public static ReflectInnerClass getInstance() {
		if(reflectInnerClass == null) {
			synchronized (ReflectInnerClass.class) {
				if(reflectInnerClass == null) {
					reflectInnerClass = new ReflectInnerClass();
				}
			}
		}
		return reflectInnerClass;
	}

	public Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			System.out.println("匿名内部类中的方法被执行了");
		}
	};
	
	static class Inner1 {
		
		public Inner1() {
			System.out.println("Inner1静态内部类实例化了");
		}
	}
	
	@SuppressWarnings("unused")
	private class Inner2 {
		
		public Inner2() {
			System.out.println("Inner2私有内部类实例化了");
		}
	}
	
	public class Inner3 {
		
		public Inner3() {
			System.out.println("Inner3内部类实例化了");
		}
	}
	
	class Inner4 {
		
		public Inner4() {
			System.out.println("Inner4内部类实例化了");
		}
	}
}

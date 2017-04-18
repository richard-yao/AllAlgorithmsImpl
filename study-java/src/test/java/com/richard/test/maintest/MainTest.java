package com.richard.test.maintest;
/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 10, 2017 9:31:32 AM
*/
public class MainTest {

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		boolean flag = false;
		try {
			int i = 0;
			while (i < 50) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				if (i == 10) {
					flag = true;
					System.out.println(flag);
					return;
				}
			} 
		} finally {
			flag = false;
			System.out.println(flag);
		}
		System.out.println(false);
		System.out.println(System.currentTimeMillis());
	}

}

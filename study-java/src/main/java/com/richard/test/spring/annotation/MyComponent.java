package com.richard.test.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
* @author RichardYao richardyao@tvunetworks.com
* @date Mar 10, 2017 10:43:51 AM
* 使用spring自定义注解
*/
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MyComponent {

	String value() default "";
}

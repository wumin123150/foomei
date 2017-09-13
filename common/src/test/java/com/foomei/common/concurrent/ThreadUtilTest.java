package com.foomei.common.concurrent;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import com.foomei.common.base.ObjectUtil;
public class ThreadUtilTest {
	@Test
	public void testCaller(){
		hello();
		new MyClass().hello();
		assertThat(ThreadUtil.getCurrentClass()).isEqualTo("com.foomei.common.concurrent.ThreadUtilTest");
		assertThat(ThreadUtil.getCurrentMethod()).isEqualTo("com.foomei.common.concurrent.ThreadUtilTest.testCaller()");
		
	}
	
	private void hello(){
		assertThat(ThreadUtil.getCallerClass()).isEqualTo("com.foomei.common.concurrent.ThreadUtilTest");
		assertThat(ThreadUtil.getCallerMethod()).isEqualTo("com.foomei.common.concurrent.ThreadUtilTest.testCaller()");
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		System.out.println(ObjectUtil.toPrettyString(stacktrace));
	}

	public static class MyClass{
		public void hello(){
			assertThat(ThreadUtil.getCallerClass()).isEqualTo("com.foomei.common.concurrent.ThreadUtilTest");
			assertThat(ThreadUtil.getCallerMethod()).isEqualTo("com.foomei.common.concurrent.ThreadUtilTest.testCaller()");
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
			System.out.println(ObjectUtil.toPrettyString(stacktrace));
		}
	}
}

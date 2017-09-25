
package com.njq.nongfadai;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SleepSecondsCallable implements Callable<String> {
	private String name;

	private int seconds;

	public SleepSecondsCallable(String name, int seconds) {
		this.name = name;
		this.seconds = seconds;
	}

	public String call() throws Exception {
		System.out.println(name + ",begin to execute");

		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			System.out.println(name + " was disturbed during sleeping.");
			return name + "_SleepSecondsCallable_failed";
		}

		System.out.println(name + ",success to execute");

		return name + "_SleepSecondsCallable_succes";
	}

}

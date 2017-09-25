
package com.njq.nongfadai;

import java.util.concurrent.Callable;

public class ExceptionCallable implements Callable<String> {

	private String name = null;

	public ExceptionCallable() {

	}

	public ExceptionCallable(String name) {
		this.name = name;
	}

	@Override
	public String call() throws Exception {
		System.out.println("begin to ExceptionCallable.");

		System.out.println(name.length());

		System.out.println("end to ExceptionCallable.");

		return name;
	}

}

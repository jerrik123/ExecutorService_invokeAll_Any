
package com.njq.nongfadai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：
 * @Package: com.njq.nongfadai.executor 
 * @author: Jerrik   
 * @date: Sep 25, 2017 9:50:56 AM
 */
public class TestExecutor {

	@Test
	public void testInvokeAny1() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		List<Callable<String>> tasks = new ArrayList<Callable<String>>();

		tasks.add(new SleepSecondsCallable("t1", 1));
		tasks.add(new SleepSecondsCallable("t2", 2));

		String result = executorService.invokeAny(tasks);

		System.out.println("result=" + result);

		executorService.shutdown();
	}

	@Test
	public void testInvokeAny2() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		List<Callable<String>> tasks = new ArrayList<Callable<String>>();

		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());

		String result = executorService.invokeAny(tasks);

		System.out.println("result=" + result);

		executorService.shutdown();
	}

	@Test
	public void testInvokeAny3() throws InterruptedException, ExecutionException {

		ExecutorService executorService = Executors.newFixedThreadPool(3);

		List<Callable<String>> tasks = new ArrayList<Callable<String>>();

		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());

		tasks.add(new SleepSecondsCallable("t1", 2));

		String result = executorService.invokeAny(tasks);

		System.out.println("result=" + result);
		executorService.shutdown();
	}

	@Test
	public void testInvokeAnyTimeout() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		List<Callable<String>> tasks = new ArrayList<Callable<String>>();

		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());
		tasks.add(new ExceptionCallable());

		String result = executorService.invokeAny(tasks, 2, TimeUnit.SECONDS);

		System.out.println("result=" + result);

		executorService.shutdown();
	}

	@Test
	public void testInvokeAll() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		List<Callable<String>> tasks = new ArrayList<Callable<String>>();
		tasks.add(new SleepSecondsCallable("t1", 2));
		tasks.add(new SleepSecondsCallable("t2", 2));
		tasks.add(new RandomTenCharsTask());
		tasks.add(new ExceptionCallable());

		// 调用该方法的线程会阻塞,直到tasks全部执行完成(正常完成/异常退出)
		List<Future<String>> results = executorService.invokeAll(tasks);

		// 任务列表中所有任务执行完毕,才能执行该语句
		System.out.println("wait for the result." + results.size());

		executorService.shutdown();

		for (Future<String> f : results) {
			// isCanceled=false,isDone=true
			System.out.println("isCanceled=" + f.isCancelled() + ",isDone=" + f.isDone());

			// ExceptionCallable任务会报ExecutionException
			System.out.println("task result=" + f.get());
		}
	}

	@Test
	public void testInvokeAllTimeout() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		List<Callable<String>> tasks = new ArrayList<Callable<String>>();
		tasks.add(new SleepSecondsCallable("t1", 2));
		tasks.add(new SleepSecondsCallable("t2", 2));
		tasks.add(new SleepSecondsCallable("t3", 3));
		tasks.add(new RandomTenCharsTask());

		List<Future<String>> results = executorService.invokeAll(tasks, 1, TimeUnit.SECONDS);

		System.out.println("wait for the result." + results.size());

		for (Future<String> f : results) {
			System.out.println("isCanceled=" + f.isCancelled() + ",isDone=" + f.isDone());
		}

		executorService.shutdown();

	}

	@Test
	public void testInvokeAllWhenInterrupt() throws Exception {
		final ExecutorService executorService = Executors.newFixedThreadPool(5);

		// 调用invokeAll的线程
		Thread invokeAllThread = new Thread() {

			@Override
			public void run() {
				List<Callable<String>> tasks = new ArrayList<Callable<String>>();
				tasks.add(new SleepSecondsCallable("t1", 2));
				tasks.add(new SleepSecondsCallable("t2", 2));
				tasks.add(new RandomTenCharsTask());

				// 调用线程会阻塞,直到tasks全部执行完成(正常完成/异常退出)
				try {
					List<Future<String>> results = executorService.invokeAll(tasks);
					System.out.println("wait for the result." + results.size());
				} catch (InterruptedException e) {
					System.out.println("I was wait,but my thread was interrupted.");
					e.printStackTrace();
				}

			}
		};

		invokeAllThread.start();

		Thread.sleep(200);

		invokeAllThread.interrupt();

		executorService.shutdown();

	}
}

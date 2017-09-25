
package com.njq.nongfadai;

import java.util.Random;
import java.util.concurrent.Callable;

public class RandomTenCharsTask implements Callable<String> {

	@Override
	public String call() throws Exception {
		System.out.println("RandomTenCharsTask begin to execute...");

		StringBuffer content = new StringBuffer();

		String base = "abcdefghijklmnopqrstuvwxyz0123456789";

		Random random = new Random();

		for (int i = 0; i < 10; i++) {
			int number = random.nextInt(base.length());
			content.append(base.charAt(number));
		}

		System.out.println("RandomTenCharsTask complete.result=" + content);
		return content.toString();
	}

}

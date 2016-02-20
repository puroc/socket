package com.example.mina2.test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class Counter {

	private Timer timer = new Timer();

	private AtomicLong num = new AtomicLong();

	public void add() {
		num.incrementAndGet();
	}

	public void clear() {
		num.set(0);
	}

	public long get() {
		return num.get();
	}

	public void start() {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println(Counter.this.get());
				Counter.this.clear();
			}
		}, 0, 1000);
	}

	public void stop() {
		timer.cancel();
	}

}

package com.service.cloud.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author liuqs
 * @date   2017年9月1日 上午10:19:14
 */
public class FixedIntervalThread {
	private static Logger logger = LoggerFactory.getLogger(FixedIntervalThread.class);

	private static final long MIN_INTERVAL = 30000l;

	private volatile long interval = 300000;// 5分钟
	private volatile boolean stop = true;

	private Runnable task;
	private ExceptionHandler exceptionHandler;

	private Thread thread;

	public FixedIntervalThread(long interval, Runnable task) {
		setInterval(interval);

		this.task = task;

		thread = new Worker("FixedInterval-Thread");
	}

	public void setInterval(long interval) {
		if (interval < MIN_INTERVAL) {
			interval = MIN_INTERVAL;
		}

		this.interval = interval;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public synchronized void start() {
		if (!stop) {
			return;
		}

		thread.start();

		stop = false;

		logger.info("FixedIntervalThread start, interval is " + (interval / 1000) + " seconds");
	}

	public synchronized void stop() {
		if (stop) {
			return;
		}

		stop = true;

		thread.interrupt();
	}

	class Worker extends Thread {
		public Worker(String name) {
			super(name);
		}

		public void run() {
			if (task == null) {
				logger.warn("No task to run, shutdown");

				return;
			}

			// delay
			sleep0(interval);

			while (!stop) {
				logger.debug("FixedIntervalThread worker run");

				try {
					task.run();
				} catch (Throwable e) {
					logger.error("Interval task execute error", e);

					if (exceptionHandler != null) {
						exceptionHandler.handle(e);
					}
				}

				sleep0(interval);
			}

			logger.info("FixedIntervalThread destroy");
		}

		private void sleep0(long interval) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
			}
		}
	}

	public interface ExceptionHandler {

		public void handle(Throwable e);
	}
}

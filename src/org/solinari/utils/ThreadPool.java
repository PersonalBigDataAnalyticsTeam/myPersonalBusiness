/**
 * 
 */
package org.solinari.utils;

import java.lang.reflect.Constructor;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.solinari.data.collector.DataCollector;

/**
 * @author solinari
 * 调用这个线程池的类必须有一个构造函数使用CountDownLatch，这个计数器用于线程同步
 */
public class ThreadPool {
	private static Logger LOG = Logger.getLogger(ThreadPool.class);
	public static CountDownLatch signal=new CountDownLatch(1);
	private int threadNum = 1;

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public void preRun(Runnable thread){
		
	}
	
	public void run(String clazz){
		ExecutorService taskExecutor = Executors.newCachedThreadPool();
		signal = new CountDownLatch(threadNum);
		LOG.info("execute job class: " + clazz + " threads: " + threadNum);
		
		try{
			for (int i=0; i<threadNum; i++){
				Class<?> cla = Class.forName(clazz);
				Constructor<?> cons = cla.getConstructor(CountDownLatch.class);
				Runnable thread = (Runnable)cons.newInstance(signal);
				preRun(thread);
				taskExecutor.submit(thread);
			}		
		}catch (Exception ex){
			LOG.error("execute thread failed StackTrace: " + ex.getStackTrace());
		}
		
		try {
			signal.await();
			LOG.error("execute job success");
		} catch (InterruptedException e) {
			LOG.error("execute job error class: " + clazz + " StackTrace: " + e.getStackTrace());
			return;
		}
		
		taskExecutor.shutdown();
	}
	
}

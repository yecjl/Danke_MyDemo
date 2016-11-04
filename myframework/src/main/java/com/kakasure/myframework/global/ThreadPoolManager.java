package com.kakasure.myframework.global;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 管理线程
 * @author danke
 *
 */
public class ThreadPoolManager {
	private static ThreadPoolManager mInstance = new ThreadPoolManager();
	private int corePoolSize;				//核心线程池的数量,表示同时能够执行的线程数量
	private int maximumPoolSize;			//最大的线程池的数量，包括corePoolSize的数量
	private int keepAliveTime = 1;			//存活时间
	private TimeUnit unit = TimeUnit.HOURS;	//小时
	private ThreadPoolExecutor executor;
	
	private ThreadPoolManager(){
		//计算corePoolSize
		corePoolSize = Runtime.getRuntime().availableProcessors()*2 + 1;	//最佳线程数
		maximumPoolSize = corePoolSize;		//最大线程池本不能为0
		executor = new ThreadPoolExecutor(
				corePoolSize, //
				maximumPoolSize, //
				keepAliveTime,
				unit, 
				new LinkedBlockingQueue<Runnable>(), 	//缓冲队列,无容量限制
				Executors.defaultThreadFactory(), 		//线程工厂
				new ThreadPoolExecutor.AbortPolicy());
	}
	
	/**
	 * 执行任务
	 * @param runnable
	 */
	public void execute(Runnable runnable){
		if(runnable==null)return;
		executor.execute(runnable);
	}
	/**
	 * 移除任务
	 */
	public void remove(Runnable runnable){
		if(runnable==null)return;
		executor.remove(runnable);
	}
	
	public static ThreadPoolManager getInstance(){
		return mInstance;
	}
}

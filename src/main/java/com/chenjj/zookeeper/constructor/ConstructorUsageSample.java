package com.chenjj.zookeeper.constructor;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ConstructorUsageSample implements Watcher {

	private static CountDownLatch countDownLatch = new CountDownLatch(1);

	public static void main(String[] args) {
		try {
			// 会话建立是一个异步的过程,初始化之后立即返回,大多数情况下,并没有真正建立好一个可用的会话,处于connecting状态
			ZooKeeper zooKeeper = new ZooKeeper("192.168.72.128:2181", 5000, new ConstructorUsageSample());
			System.out.println(zooKeeper.getState());
			countDownLatch.await();// 当前调用线程阻塞直到CountDownLatch内部计数器减为0
			System.out.println("ZooKeeper session established.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event：" + event);
		if (KeeperState.SyncConnected == event.getState()) {// 收到服务端发来的SyncConnected事件后,表示会话已经创建完毕,此时解除主线程的阻塞
			countDownLatch.countDown();
		}
	}

}

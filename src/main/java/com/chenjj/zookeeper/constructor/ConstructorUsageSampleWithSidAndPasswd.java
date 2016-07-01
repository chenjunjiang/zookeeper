package com.chenjj.zookeeper.constructor;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ConstructorUsageSampleWithSidAndPasswd implements Watcher {

	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	private static String connectString = "192.168.72.128:2181";

	public static void main(String[] args) {
		try {
			ZooKeeper zooKeeper = new ZooKeeper(connectString, 5000, new ConstructorUsageSampleWithSidAndPasswd());
			System.out.println(zooKeeper.getState());
			countDownLatch.await();
			System.out.println("ZooKeeper session established.");
			long sessionId = zooKeeper.getSessionId();
			byte[] sessionPasswd = zooKeeper.getSessionPasswd();
			// Use illegal sessionId and sessionPassWd
			zooKeeper = new ZooKeeper(connectString, 5000, new ConstructorUsageSampleWithSidAndPasswd(), 11,
					"test".getBytes());
			// Use correct sessionId and sessionPassWd
			zooKeeper = new ZooKeeper(connectString, 5000, new ConstructorUsageSampleWithSidAndPasswd(), sessionId,
					sessionPasswd);
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event：" + event);
		if (KeeperState.SyncConnected == event.getState()) {
			countDownLatch.countDown();
		}
	}

}

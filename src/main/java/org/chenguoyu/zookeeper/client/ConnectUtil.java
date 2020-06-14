package org.chenguoyu.zookeeper.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ConnectUtil {
    public static ZooKeeper connect() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ZooKeeper zookeeper = null;
        try {
            zookeeper = new ZooKeeper("192.168.223.133:2181", 5000, (WatchedEvent x) -> {
                if (x.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    System.out.println("连接成功");
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return zookeeper;
    }
}

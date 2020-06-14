package org.chenguoyu.zookeeper.client.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class KeepStateTest {
    private static final String IP = "192.168.223.133:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public class ZkConnectionWatcher implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {
            Watcher.Event.KeeperState state = watchedEvent.getState();
            if (state == Event.KeeperState.SyncConnected) {
                // 正常
                System.out.println("正常连接");
            } else if (state == Event.KeeperState.Disconnected) {
                // 可以用Windows断开虚拟机网卡的方式模拟
                // 当会话断开会出现，断开连接不代表不能重连，在会话超时时间内重连可以恢复正常
                System.out.println("断开连接");
            } else if (state == Event.KeeperState.Expired) {
                // 没有在会话超时时间内重新连接，而是当会话超时被移除的时候重连会走进这里
                System.out.println("连接过期");
            } else if (state == Event.KeeperState.AuthFailed) {
                // 在操作的时候权限不够会出现
                System.out.println("授权失败");
            }
            countDownLatch.countDown();
        }

        @Test
        public void main() throws Exception {
            // 5000为会话超时时间
            ZooKeeper zooKeeper = new ZooKeeper(IP, 5000, new ZkConnectionWatcher());
            countDownLatch.await();
            // 模拟授权失败
            zooKeeper.addAuthInfo("digest1", "itcast1:123451".getBytes());
            byte[] data = zooKeeper.getData("/hadoop", false, null);
            System.out.println(new String(data));
            TimeUnit.SECONDS.sleep(50);
        }
    }
}

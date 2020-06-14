package org.chenguoyu.zookeeper.client.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EventTypeTest {
    private static final String IP = "192.168.223.133:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    /**
     * 采用zookeeper连接创建时的监听器
     *
     * @throws Exception
     */
    public static void exists1() throws Exception {
        zooKeeper.exists("/watcher1", true);
    }

    /**
     * 自定义监听器
     *
     * @throws Exception
     */
    public static void exists2() throws Exception {
        zooKeeper.exists("/watcher1", (WatchedEvent w) -> {
            System.out.println("自定义" + w.getType());
        });
    }

    /**
     * 使用多次的监听器
     *
     * @throws Exception
     */
    @Test
    public void exists3() throws Exception {
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println("自定义的" + watchedEvent.getType());
                } finally {
                    try {
                        zooKeeper.exists("/watcher1", this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 演示一节点注册多个监听器
     *
     * @throws Exception
     */
    @Test
    public void exists4() throws Exception {
        zooKeeper.exists("/watcher1", (WatchedEvent w) -> {
            System.out.println("自定义1" + w.getType());
        });
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println("自定义2" + watchedEvent.getType());
                } finally {
                    try {
                        zooKeeper.exists("/watcher1", this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Before
    public void before() throws Exception {
        zooKeeper = new ZooKeeper(IP, 5000, new ZKWatcher());
        countDownLatch.await();
    }

    @After
    public void after() throws Exception {
        TimeUnit.SECONDS.sleep(50);
    }

    static class ZKWatcher implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {
            countDownLatch.countDown();
            System.out.println("zk的监听器" + watchedEvent.getType());
        }
    }

}

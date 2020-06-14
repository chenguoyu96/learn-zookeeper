package org.chenguoyu.zookeeper.client;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class DeleteTest {
    private ZooKeeper zookeeper;

    @Before
    public void before() {
        zookeeper = ConnectUtil.connect();
    }

    @After
    public void after() {
        try {
            zookeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步删除数据
     *
     * @throws Exception
     */
    @Test
    public void deleteData1() throws Exception {
        // path 节点路径
        // version 版本
        zookeeper.delete("/hadoop", 1);
    }

    /**
     * 异步删除数据
     *
     * @throws Exception
     */
    @Test
    public void deleteData2() throws Exception {
        // path 节点路径
        // version 版本
        // callBack 数据的版本号， -`1`代表不使用版本号，乐观锁机制
        // 传递上下文参数
        zookeeper.delete("/hadoop", 1, new AsyncCallback.VoidCallback() {
            /**
             * @param rc 状态，0 则为成功，以下的所有示例都是如此
             * @param path 路径
             * @param ctx 上下文参数
             * @param stat 节点状态
             */
            @Override
            public void processResult(int rc, String path, Object ctx) {
                System.out.println(rc + " " + path + " " + ctx);
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(1);
    }
}

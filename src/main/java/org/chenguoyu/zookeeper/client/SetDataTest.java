package org.chenguoyu.zookeeper.client;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SetDataTest {
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
     * 同步修改节点数据
     *
     * @throws Exception
     */
    @Test
    public void setData1() throws Exception {
        // arg1:节点的路径
        // arg2:修改的数据
        // arg3:数据的版本号 -1 代表版本号不参与更新
        Stat stat = zookeeper.setData("/hadoop", "hadoop-1".getBytes(), -1);
    }

    /**
     * 异步修改节点数据
     *
     * @throws Exception
     */
    @Test
    public void setData2() throws Exception {
        zookeeper.setData("/hadoop", "hadoop-1".getBytes(), 3, new AsyncCallback.StatCallback() {
            /**
             * @param rc 状态，0 则为成功，以下的所有示例都是如此
             * @param path 路径
             * @param ctx 上下文参数
             * @param stat 节点状态
             */
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                // 讲道理，要判空
                System.out.println(rc + " " + path + " " + stat.getVersion() + " " + ctx);
            }
        }, "I am context");
    }
}

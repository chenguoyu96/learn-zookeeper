package org.chenguoyu.zookeeper.client.get;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.chenguoyu.zookeeper.client.ConnectUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class GetNodeTest {
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

    @Test
    public void getData1() throws Exception {
        Stat stat = new Stat();
        // path 节点路径
        // boolean 是否使用连接对象中注册的监听器
        // stat 元数据
        byte[] data = zookeeper.getData("/hadoop", false, stat);
        System.out.println(new String(data));
        // 判空
        System.out.println(stat.getCtime());
    }

    @Test
    public void getData2() throws Exception {
        // path 节点路径
        // boolean 是否使用连接对象中注册的监听器
        // stat 元数据
        // callBack 异步回调接口，可以获得状态和数据
        // ctx 传递上下文参数
        zookeeper.getData("/hadoop", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
                // 判空
                System.out.println(rc + " " + path + " " + ctx + " " + new String(bytes) + " " + stat.getCzxid());
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(3);
    }
}

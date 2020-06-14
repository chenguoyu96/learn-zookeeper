package org.chenguoyu.zookeeper.client.get;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.chenguoyu.zookeeper.client.ConnectUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetChildrenTest {

    @Test
    public void getChildren_1() throws Exception {
        // path 节点路径
        // watch 是否使用连接对象中注册的监听器
        List<String> hadoop = zookeeper.getChildren("/hadoop", false);
        hadoop.forEach(System.out::println);
    }

    @Test
    public void getChildren_2() throws Exception {
        // path 节点路径
        // watch 是否使用连接对象中注册的监听器
        // callBack 异步回调，可以获取节点列表
        // ctx 传递上下文参数
        zookeeper.getChildren("/hadoop", false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> list) {
                list.forEach(System.out::println);
                System.out.println(rc + " " + path + " " + ctx);
            }
        }, "I am children");
        TimeUnit.SECONDS.sleep(3);
    }

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
}

package org.chenguoyu.zookeeper.client;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

public class ExistTest {
    public  void exists1() throws Exception{
        // path 节点路径
        // watch 是否使用连接对象中注册的监听器
        Stat exists = zookeeper.exists("/hadoopx", false);
        // 判空
        System.out.println(exists.getVersion() + "成功");
    }
    public  void exists2() throws Exception{
        // path 节点路径
        // watch 是否使用连接对象中注册的监听器
        // callBack 异步回调，可以获取节点列表
        // ctx 传递上下文参数
        zookeeper.exists("/hadoopx", false, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                // 判空
                System.out.println(rc + " " + path + " " + ctx +" " + stat.getVersion());
            }
        }, "I am children");
        TimeUnit.SECONDS.sleep(1);
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

package org.chenguoyu.zookeeper.client;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateTest {
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
     * 通过枚举的方式设置访问控制权限
     *
     * @throws Exception
     */
    @Test
    public void createTest1() throws Exception {
        String str = "node";
        // path 节点路径
        // data 数据
        // acl 节点的访问控制权限列表
        // createNode 节点的类型
        String s = zookeeper.create("/node", str.getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);
    }

    /**
     * 自定义访问控制权限
     * ip方式授权
     *
     * @throws Exception
     */
    @Test
    public void createTest2() throws Exception {
        ArrayList<ACL> acls = new ArrayList<>();
        Id id = new Id("ip", "192.168.133.133");
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zookeeper.create("/create/node4", "node4".getBytes(), acls, CreateMode.PERSISTENT);
    }

    /**
     * digest方式授权
     *
     * @throws Exception
     */
    @Test
    public void createTest3() throws Exception {
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("digest", "itcast:qUFSHxJjItUW/93UHFXFVGlvryY=");
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        zookeeper.create("/create/node7", "node7".getBytes(), acls, CreateMode.PERSISTENT);
    }

    /**
     * 异步授权
     *
     * @throws Exception
     */
    @Test
    public void createTest4() throws Exception {
        zookeeper.create("/node12", "node12".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
            /**
             * @param rc 状态，0 则为成功，以下的所有示例都是如此
             * @param path 路径
             * @param ctx 上下文参数
             * @param name 路径
             */
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                System.out.println(rc + " " + path + " " + name + " " + ctx);
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("结束");
    }
}

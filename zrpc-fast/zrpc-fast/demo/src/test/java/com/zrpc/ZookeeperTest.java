package com.zrpc;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ZookeeperTest {
    ZooKeeper zooKeeper;

    @Before
    public void Zookeeper(){
        String path = "127.0.0.1:2181";
        int timeout = 10000;
        try {
            zooKeeper = new ZooKeeper(path,timeout,null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void createPNode(){
        String result = null;
        try {
            result = zooKeeper.create("/zrpc", "hello,zookeeper".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("result = " + result);

    }

    @Test
    public void deletePNode(){
        try {
            // version: cas  mysql  乐观锁，  也可以无视版本号  -1
            zooKeeper.delete("/zrpc", -1);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void setData(){
        try {
            Stat stat = zooKeeper.setData("/zrpc", "hi,zrpc".getBytes(), -1);
            // 当前节点的acl数据版本
            int aversion = stat.getAversion();
            // 当前节点的数据版本
            int version = stat.getVersion();
            // 当前子节点数据的版本
            int cversion = stat.getCversion();
            System.out.println("aversion = " + aversion);
            System.out.println("version = " + version);
            System.out.println("cversion = " + cversion);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package com.zrpc;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class ZWatcher implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.EventType.NodeCreated.equals(watchedEvent.getType())) {
            System.out.println(watchedEvent.getPath() + "已创建");
        } else if (Event.EventType.NodeDataChanged.equals(watchedEvent.getType())) {
            System.out.println(watchedEvent.getPath() + "数据被修改，修改后的数据为");
        }else if (Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
            System.out.println(watchedEvent.getPath() + "节点被删除");
        }
    }
}

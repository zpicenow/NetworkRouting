/*
 * NeighNode
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.node;

import java.io.Serializable;

/**
 * 相邻节点类
 * 实现了Serializable接口保证网络传输有效
 */
public class NeighNode implements Serializable {
    /**
     * id 临节点ID
     * port 临节点端口号
     * length 路径长度
     * updateTime 更新时间
     * online 是否宕机
     */
    private String id;
    private int port;
    private int length;
    private long updateTime;
    private boolean online;

    /**
     * 构造函数
     * 更新时间初始化当前时间
     * @param id 节点ID
     * @param port 节点端口号
     * @param length 路径长度
     */
    public NeighNode(String id, int port, int length){
        this.id = id;
        this.port = port;
        this.length = length;
        this.updateTime = System.currentTimeMillis();
        this.online = true;
    }

    public String getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public int getLength() {
        return length;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }


    public boolean isOnline() {
        return online;
    }
    /**
     * 直接调用设置是否宕机
     *
     */
    public void online(){
        this.online = true;
    }

    public void offline(){
        this.online = false;
    }

}

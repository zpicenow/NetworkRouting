/*
 * ProtocolData
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.protocol;

import com.zp.protocol.data.Package;

import java.io.Serializable;

/**
 * 协议数据类
 * 规定了udp交换的协议数据的格式
 * type int 数据类型 约定0为广播包1为心跳包2为更新时间包
 * time long 更新时间
 * receivePort int 接收端口号
 * sendPort int 发送端口号
 * aPacket Packet 具体包 运行时实例化为子类
 */
public class ProtocolData implements Serializable {
    private int type;
    private long time;
    private int receivePort;
    private int sendPort;
    private Package aPackage;

    public ProtocolData(int type,int sendPort, int receivePort, Package aPackage){
        this.type = type;
        this.time = System.currentTimeMillis();
        this.receivePort = receivePort;
        this.sendPort = sendPort;
        this.aPackage = aPackage;
    }

    public int getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public int getReceivePort() {
        return receivePort;
    }

    public int getSendPort() {
        return sendPort;
    }

    public Package getaPackage() {
        return aPackage;
    }
}

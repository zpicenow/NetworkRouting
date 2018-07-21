/*
 * UdpData
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.udp;

import com.zp.protocol.ProtocolData;

import java.util.concurrent.LinkedBlockingQueue;

public class UdpData {
    private LinkedBlockingQueue<ProtocolData> receiveData;  //阻塞队列 https://blog.csdn.net/javazejian/article/details/77410889?locationNum=1&fps=1
    private LinkedBlockingQueue<ProtocolData> sendData;

    public UdpData(){
        this.receiveData = new LinkedBlockingQueue<>();
        this.sendData = new LinkedBlockingQueue<>();
    }

    public void addReceiveData(ProtocolData data) throws InterruptedException{
        receiveData.put(data);
    }

    public ProtocolData getReceiveData() throws InterruptedException{
        return receiveData.take();
    }

    public ProtocolData getSendData() throws InterruptedException{
        return sendData.take();
    }

    public void setSendData(ProtocolData data) throws InterruptedException{
        sendData.put(data);
    }
}

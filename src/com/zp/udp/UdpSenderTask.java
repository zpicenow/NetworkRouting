/*
 * SenderThread
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.udp;

import com.zp.protocol.ProtocolData;
import com.zp.lib.ObjectAndBytes;
import com.zp.threadPool.Task;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpSenderTask extends Task {
    private UdpData udpData;

    public UdpSenderTask(UdpData udpData){
        this.udpData = udpData;
    }

    /**
     * UDP发送服务
     * 配置主机和端口号
     * 主机为默认参数
     * 初始化数组
     * 对象封装
     */
    @Override
    public void run() {
        try{
            DatagramSocket socket = new DatagramSocket();
            while (true){
                ProtocolData sendData = udpData.getSendData();
                byte[] data = ObjectAndBytes.toByteArray(sendData);
                DatagramPacket packet = new DatagramPacket(data, data.length);
                packet.setSocketAddress(new InetSocketAddress(sendData.getReceivePort()));  //默认主机地址，只设定端口号
                socket.send(packet);
            }
        }catch (Exception err){
            err.printStackTrace();
        }
    }
}

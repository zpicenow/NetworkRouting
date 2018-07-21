/*
 * ReceiverThread
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.udp;

import com.zp.protocol.ProtocolData;
import com.zp.lib.ObjectAndBytes;
import com.zp.threadPool.Task;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpReceiverTask extends Task {
    private UdpData udpData;
    private int port;

    public UdpReceiverTask(UdpData udpData, int port){
        this.udpData = udpData;
        this.port = port;
    }

    /**
     * UDP接收服务
     * 配置端口
     * 初始化数组
     * 数组解析
     * 加入已收到数据队列
     */
    @Override
    public void run() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(port);
            while (true){
                byte[] buffer = new byte[1024*16];
                DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length);
                datagramSocket.receive(datagramPacket);
                byte[] data = datagramPacket.getData();
                byte[] receiveData = new byte[datagramPacket.getLength()];
                System.arraycopy(buffer, 0, receiveData, 0, receiveData.length);
                ProtocolData object =(ProtocolData) ObjectAndBytes.toObject(receiveData);
                udpData.addReceiveData(object);
            }
        }catch (SocketException err){
            err.printStackTrace();
            if(datagramSocket!=null){
                datagramSocket.close();
            }
        } catch (InterruptedException | IOException err){
            err.printStackTrace();
        }
    }
}

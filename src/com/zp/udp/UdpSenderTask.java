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

    @Override
    public void run() {
        try{
            DatagramSocket socket = new DatagramSocket();
            while (true){
                ProtocolData sendData = udpData.getSendData();
                byte[] data = ObjectAndBytes.toByteArray(sendData);
                DatagramPacket packet = new DatagramPacket(data, data.length);
                packet.setSocketAddress(new InetSocketAddress(sendData.getReceivePort()));
                socket.send(packet);
            }
        }catch (Exception err){
            err.printStackTrace();
        }
    }
}

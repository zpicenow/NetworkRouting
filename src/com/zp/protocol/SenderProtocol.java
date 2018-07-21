/*
 * SenderProtocol
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.protocol;

import com.zp.protocol.data.Package;
import com.zp.udp.UdpData;

public class SenderProtocol {
    private int sendPort;
    private UdpData udpData;

    /**
     * 构造判断目标端口号是否有效
     * @param sendPort 目标端口号
     * @param udpData UDP数据
     * @throws Exception 端口号越界
     */
    public SenderProtocol(int sendPort, UdpData udpData) throws Exception {
        if(sendPort < 1000 || sendPort > 65535){
            throw new Exception("Error receivePort value, the value is" + sendPort);
        }
        this.sendPort = sendPort;
        this.udpData = udpData;
    }

    /**
     * 对于每个发送数据进行源设置
     * 判断合法性并加入待发送队列中
     * @param type  数据包类型
     * @param receivePort 目的端口号
     * @param aPackage 数据包运行时向下转型
     * @throws Exception 数据格式异常
     */

    public void generateProtocolData(int type, int receivePort, Package aPackage) throws Exception {
        if(type>3||type<0){
            throw new Exception("Error type value, the value is " + type);
        }
        if(receivePort < 1000 || receivePort > 65535){
            throw new Exception("Error receivePort value, the value is" + receivePort);
        }
        if(aPackage == null){
            throw new Exception("The aPackage is null.");
        }
        try {
            udpData.setSendData(new ProtocolData(type,sendPort,receivePort, aPackage));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

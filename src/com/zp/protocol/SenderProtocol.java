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

    public SenderProtocol(int sendPort, UdpData udpData) throws Exception {
        if(sendPort < 1000 || sendPort > 65535){
            throw new Exception("Error receivePort value, the value is" + sendPort);
        }
        this.sendPort = sendPort;
        this.udpData = udpData;
    }


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

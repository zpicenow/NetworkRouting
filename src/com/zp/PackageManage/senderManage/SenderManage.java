/*
 * UdpSenderTask
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.senderManage;

import com.zp.node.ReadNodeData;

import com.zp.protocol.SenderProtocol;
import com.zp.threadPool.Task;
import com.zp.threadPool.ThreadPool;
import com.zp.udp.UdpData;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class SenderManage extends Task {
    private Properties config;
    private SenderProtocol sendProtocol;
    private ThreadPool threadPool;
    private ReadNodeData node;

    /**
     * 构造初始化
     * 获取配置文件并解析
     * 设置发包时间，心跳包时间，更新时间等等
     * @param node 读自身数据
     * @param threadPool 调度线程池
     * @param udpData 自身的udp数据集
     */
    public SenderManage(ReadNodeData node, ThreadPool threadPool, UdpData udpData){
        try {
            this.sendProtocol = new SenderProtocol(node.getPort(),udpData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            this.config = new Properties();

            File file = new File("testLoc.txt");
            String path = file.getCanonicalPath(); //..../NetworkRouting/testloc.txt
            path = path.substring(0, path.lastIndexOf(File.separator) );
            File configFile = new File(path + File.separator + "src" + File.separator + "com" + File.separator + "zp" + File.separator + "assignment.properties" );

            this.config.load(new FileInputStream(configFile));
        }catch (Exception err){
            err.printStackTrace();
        }
        this.threadPool = threadPool;
        this.node = node;
    }

    /**
     * 根据配置文件设置时间信息
     * 发送线程任务
     * 初始化分配数据包的发送线程任务加入到就绪队列
     */
    @Override
    public void run() {
        threadPool.execute(new HeartbeatManage(sendProtocol,node,Integer.parseInt(this.config.getProperty("HeartbeatTime"))));
        try{
            Thread.sleep(Integer.parseInt(this.config.getProperty("DelayTime")));
        }catch (InterruptedException err){
            err.printStackTrace();
        }
        threadPool.execute(new BroadcastManage(sendProtocol,node));
        threadPool.execute(new TimedUpdateManage(sendProtocol,node,Integer.parseInt(this.config.getProperty("TimedUpdateTime"))));
    }
}

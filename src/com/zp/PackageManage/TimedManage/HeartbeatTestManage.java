/*
 * HeartbeatTestManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.TimedManage;

import com.zp.node.NeighNode;
import com.zp.node.WriteNodeData;
import com.zp.threadPool.Task;

import java.util.LinkedList;
import java.util.logging.Logger;

public class HeartbeatTestManage extends Task {
    private WriteNodeData node;
    private int time;

    public HeartbeatTestManage(WriteNodeData node, int time){
        this.node = node;
        this.time = time;
    }

    /**
     * 心跳包处理线程任务
     * 当前时间与最后更新时间的差如果大于配置文件中的心跳频率的三倍
     * 即说明丢包三次，认为丢失
     * 删除节点处理
     */
    @Override
    public void run() {
        while (true){
            try{
                Thread.sleep(time);
            }catch (InterruptedException err){
                err.printStackTrace();
            }
            LinkedList<NeighNode> neighNodes = node.getNeighNodes();
            for (NeighNode neighNode : neighNodes){
                if(System.currentTimeMillis() - neighNode.getUpdateTime() > 3*time){
                    System.out.println(neighNode.getId() + "   offline");
                    node.deleteLinkNode(neighNode);
                }
            }
        }
    }
}

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
                    Logger.getGlobal().info("delete " + neighNode.getId());
                    node.deleteLinkNode(neighNode);
                }
            }
        }
    }
}

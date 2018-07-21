/*
 * DijkstraManage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.PackageManage.TimedManage;

import com.zp.graph.Graph;
import com.zp.node.ReadNodeData;
import com.zp.threadPool.Task;

public class DijkstraManage extends Task {
    private ReadNodeData node;
    private int time;

    public DijkstraManage(ReadNodeData node, int time){
        this.node = node;
        this.time = time;
    }

    /**
     * 迪杰斯特拉线程任务
     * 用于更新输出迪杰斯特拉信息
     */
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(time);
            }catch (InterruptedException err){
                err.printStackTrace();
            }
            if(node.getGraphEdgeLinkedList().size() == 0){
                System.out.println("孤立节点");
            }else {
                Graph graph = new Graph();
                graph.addGraphEdges(node.getGraphEdges());
                graph.dijkstra(node.getId());
            }
        }
    }
}

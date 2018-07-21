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

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(time);
            }catch (InterruptedException err){
                err.printStackTrace();
            }
            if(node.getGraphEdgeLinkedList().size() == 0){
                System.out.println("当前无任何图信息");
            }else {
                Graph graph = new Graph();
                graph.addGraphEdges(node.getGraphEdges());
                graph.dijkstra(node.getId());
            }
        }
    }
}

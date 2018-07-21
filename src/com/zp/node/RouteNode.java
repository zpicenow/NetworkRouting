/*
 * RouteNode
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.node;

import com.zp.protocol.data.BroadcastPackage;
import com.zp.protocol.data.HeartbeatPackage;
import com.zp.protocol.data.TimedUpdatePackage;
import com.zp.graph.GraphEdge;

import java.io.*;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * 网络节点类
 */
public class RouteNode implements WriteNodeData {
    private String id;
    private int port;
    private LinkedList<NeighNode> neighNodes;
    private LinkedList<GraphEdge> graphEdgeLinkedList;
    private Set<String> receivedNodes;
    private final Object graphEdgeLock = new Object();
    private final Object receiveNodesLock = new Object();

    public RouteNode(String id, int port, String configPath) {
        this.id = id;
        this.port = port;
        this.graphEdgeLinkedList = new LinkedList<>();
        this.neighNodes = new LinkedList<>();
        this.receivedNodes = new TreeSet<>();
        try {
            init(configPath);
        }catch (Exception err){
            err.printStackTrace();
        }
    }

    /**
     * linkedNodes初始化函数
     * 创建定位文件，适配不同运行环境
     * @param configPath 配置文件路径
     * @throws IOException
     */
    ///home/zhaopeng/JavaSmallSemester/NetworkRouting/src/com/zp/config
    private void init(String configPath) throws Exception {
        int num = 0;
        File file = new File("testLoc.txt");
        String path = file.getCanonicalPath(); //..../NetworkRouting/testloc.txt
        path = path.substring(0, path.lastIndexOf(File.separator) );
        File configFile = new File(path + File.separator + "src" + File.separator + "com" + File.separator + "zp" + File.separator + "config" + File.separator + configPath);
        FileReader fileReader = new FileReader(configFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        if ((path = bufferedReader.readLine())!=null) num = Integer.parseInt(path);
        for (int i = 0; i < num; i++) {
            String[] strings = bufferedReader.readLine().split(" ");
            String nodeId = strings[0];
            int nodeLength = Integer.parseInt(strings[1]);
            int nodePort = Integer.parseInt(strings[2]);

            neighNodes.addLast(new NeighNode(nodeId,nodePort,nodeLength));
        }

    }

    public LinkedList<GraphEdge> getGraphEdges(){
        LinkedList<GraphEdge> temp = new LinkedList<>();
        synchronized (graphEdgeLock){
            for (GraphEdge graphEdge:graphEdgeLinkedList){
                temp.addLast(graphEdge);
            }
        }
        for (NeighNode neighNode : getNeighNodes()){
            temp.addLast(new GraphEdge(this.id, neighNode.getId(), neighNode.getLength()));
        }
        return temp;
    }

    private LinkedList<GraphEdge> deleteGraphEdgeById(LinkedList<GraphEdge> graphEdges){
        LinkedList<GraphEdge> temp = new LinkedList<>();
        for (GraphEdge graphEdge:graphEdges){
            if(!graphEdge.getStart().equals(id) && !graphEdge.getEnd().equals(id)){
                temp.add(graphEdge);
            }
        }
        return temp;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public LinkedList<NeighNode> getNeighNodes() {
        LinkedList<NeighNode> temp = new LinkedList<>();
        for (NeighNode neighNode : neighNodes){
            if(neighNode.isOnline()){
                temp.addLast(neighNode);
            }
        }
        return temp;
    }

    @Override
    public LinkedList<GraphEdge> getGraphEdgeLinkedList() {
        return getGraphEdges();
    }

    @Override
    public boolean processBroadcast(BroadcastPackage broadcastPackage) {
        synchronized (receiveNodesLock){
            if(receivedNodes.contains(broadcastPackage.getId())){
                return false;
            }else {
                receivedNodes.add(broadcastPackage.getId());
                LinkedList<NeighNode> temp = broadcastPackage.getNeighNodes();
                synchronized (graphEdgeLock){
                    for (NeighNode neighNode :temp){
                        GraphEdge graphEdge = new GraphEdge(broadcastPackage.getId(), neighNode.getId(), neighNode.getLength());
                        if (!graphEdgeLinkedList.contains(graphEdge)){
                            graphEdgeLinkedList.addLast(graphEdge);
                        }
                    }
                }
                return true;
            }
        }
    }

    @Override
    public void setHeartbeatTime(HeartbeatPackage heartbeatData) {
        for (int i = 0; i < neighNodes.size(); i++){
            if (neighNodes.get(i).getId().equals(heartbeatData.getId())){
                neighNodes.get(i).online();
                neighNodes.get(i).setUpdateTime(System.currentTimeMillis());
                break;
            }
        }
    }

    @Override
    public void processTimedUpdate(TimedUpdatePackage timedUpdateData) {
        LinkedList<GraphEdge> temp = deleteGraphEdgeById(timedUpdateData.getGraphEdges());
        synchronized (graphEdgeLock){
            graphEdgeLinkedList = temp;
        }
    }

    @Override
    public void deleteLinkNode(NeighNode neighNode) {
        neighNodes.get(neighNodes.indexOf(neighNode)).offline();
        synchronized (graphEdgeLock){
            LinkedList<GraphEdge> toDelete = new LinkedList<>();
            for (GraphEdge graphEdge:graphEdgeLinkedList){
                if(graphEdge.getStart().equals(neighNode.getId()) || graphEdge.getEnd().equals(neighNode.getId())){
                    toDelete.addLast(graphEdge);
                }
            }
            graphEdgeLinkedList.removeAll(toDelete);
        }
    }
}

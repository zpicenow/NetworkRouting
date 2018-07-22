/*
 * RouteNode
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.node;

import com.zp.netView.RouteTable;
import com.zp.protocol.data.BroadcastPackage;
import com.zp.protocol.data.HeartbeatPackage;
import com.zp.protocol.data.TimedUpdatePackage;

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
    private LinkedList<RouteTable> routeTableLinkedList;
    private Set<String> receivedNodes;
    private final Object graphEdgeLock = new Object();
    private final Object receiveNodesLock = new Object();

    public RouteNode(String id, int port, String configPath) {
        this.id = id;
        this.port = port;
        this.routeTableLinkedList = new LinkedList<>();
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

    /**
     * 获取抽象图的边
     * 依据邻接点集动态更新保证准确性
     * @return LinkedList<RouteTable>
     */
    public LinkedList<RouteTable> getGraphEdges(){
        LinkedList<RouteTable> temp = new LinkedList<>();
        synchronized (graphEdgeLock){
            for (RouteTable routeTable : routeTableLinkedList){
                temp.addLast(routeTable);
            }
        }
        for (NeighNode neighNode : getNeighNodes()){
            temp.addLast(new RouteTable(this.id, neighNode.getId(), neighNode.getLength()));
        }
        return temp;
    }

    /**
     * 移除无效边
     * 将原本的集合与新接收的集合比较，起终点都不匹配说明已经不存在该条路径，即删去
     * @param routeTables 新收到的边集
     * @return 删改之后的边集
     */
    private LinkedList<RouteTable> deleteGraphEdgeById(LinkedList<RouteTable> routeTables){
        LinkedList<RouteTable> temp = new LinkedList<>();
        for (RouteTable routeTable : routeTables){
            if(!routeTable.getStart().equals(id) && !routeTable.getEnd().equals(id)){
                temp.add(routeTable);
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

    /**
     * 获取邻接点表
     * 同时动态移除离线的节点
     * @return 邻接点集合
     */
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

    /**
     * 获取边集
     * @return 边集
     */
    @Override
    public LinkedList<RouteTable> getRouteTableLinkedList() {
        return getGraphEdges();
    }

    /**
     * 处理收到的广播包
     *
     * @param broadcastPackage  由UDP服务收到并解析出来的广播包
     * @return false 邻接点已计算过的ID，不处理
     *          true 邻接点集不包含的ID，添加到邻接点集，添加到路径图集
     */
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
                        RouteTable routeTable = new RouteTable(broadcastPackage.getId(), neighNode.getId(), neighNode.getLength());
                        if (!routeTableLinkedList.contains(routeTable)){
                            routeTableLinkedList.addLast(routeTable);
                        }
                    }
                }
                return true;
            }
        }
    }

    /**
     * 处理收到的心跳包
     * 如果心跳包包含设置在线同时更新时间，以确定是否超时三次
     * @param heartbeatData 接收到的心跳包
     */
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

    /**
     * 处理收到的更新包
     * 调用删改函数，将边集保持最新
     * @param timedUpdateData 接收的更新包
     */
    @Override
    public void processTimedUpdate(TimedUpdatePackage timedUpdateData) {
        LinkedList<RouteTable> temp = deleteGraphEdgeById(timedUpdateData.getRouteTables());
        synchronized (graphEdgeLock){
            routeTableLinkedList = temp;
        }
    }

    /**
     * 移除离线节点
     * 将传入的节点调用offline函数标志下线
     * 同时移除节点set里存储的信息以及边集中对应节点的路径
     * @param neighNode 待删除的节点
     */
    @Override
    public void deleteLinkNode(NeighNode neighNode) {
        neighNodes.get(neighNodes.indexOf(neighNode)).offline();
        synchronized (graphEdgeLock){
            LinkedList<RouteTable> toDelete = new LinkedList<>();
            for (RouteTable routeTable : routeTableLinkedList){
                if(routeTable.getStart().equals(neighNode.getId()) || routeTable.getEnd().equals(neighNode.getId())){
                    toDelete.addLast(routeTable);
                }
            }
            routeTableLinkedList.removeAll(toDelete);
        }
    }
}

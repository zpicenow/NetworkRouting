/*
 * Graph
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.graph;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 图类
 * 实现了Dijkstra接口
 * 抽象化表示当前网络的二维图结构，并输出Dijkstra路径
 */
public class Graph {
    /**
     * 所有边的集合
     */
    private final LinkedList<GraphEdge> graphEdges;

    /**
     * 点的数目
     */
    private int nodesNumber = 0;

    /**
     * 图的抽象矩阵
     */
    private int[][] matrix = null;

    /**
     * 标记数组
     */
    private int[] mark = null;

    /**
     * 构造函数
     */
    public Graph(){
        graphEdges = new LinkedList<>();
    }

    /**
     * 添加边
     * @param graphEdges　边的集合
     */

    public void addGraphEdges(LinkedList<GraphEdge> graphEdges) {
        for (GraphEdge graphEdge:graphEdges){
            this.graphEdges.addLast(new GraphEdge(graphEdge.getStart(),graphEdge.getEnd(),graphEdge.getLength()));
        }
    }

    /**
     * 计算Dijkstra路径所需的两个数组
     * @param start　出发点
     * @param length　长度数组
     * @param dist　记录路径数组
     */
    private void calculator(int start, int[] length, int[] dist){
        int min = 0;
        for (int j = 0; j < nodesNumber; ++j) {
            mark[j] = 0;
        }
        mark[start] = 1;
        for (int k = 0; k < nodesNumber - 1; ++k) {
            for (int j = 0; j < nodesNumber; ++j) {
                if (mark[j] == 0) {
                    if ((length[j] == 0 || length[j] > length[start] + matrix[start][j]) && matrix[start][j] != 0) {
                        length[j] = length[start] + matrix[start][j];
                        dist[j] = start;
                    }
                }
            }
            for (int m = 0; m < nodesNumber; ++m) {
                if(mark[m] == 0 && length[m] != 0){
                    min = m;
                    break;
                }
            }
            for (int l = 0; l < nodesNumber; ++l) {
                if (mark[l] == 0 && length[l] != 0) {
                    if (length[l] < length[min]) {
                        min = l;
                    }
                }
            }
            start = min;
            mark[start] = 1;
        }
    }

    /**
     * 初始化matrix矩阵
     * @param map id数字映射
     */
    private void initMatrix(Map<String,Integer> map){
        matrix = new int[nodesNumber][nodesNumber];
        for (GraphEdge edge:graphEdges){
            matrix[map.get(edge.getStart())][map.get(edge.getEnd())] = edge.getLength();
            matrix[map.get(edge.getEnd())][map.get(edge.getStart())] = edge.getLength();
        }
    }



    /**
     * 打印当前结点到其他结点的最短路径
     * @param dist
     * @param length
     * @param table
     */
    private void printfInfo(int[] dist, int[] length, Map<Integer,String> table, int start){
        System.out.println("node  "+table.get(start)+"  to others：");
        int end = 0;
        for (int i = 0; i < nodesNumber; i++){
            StringBuilder builder = new StringBuilder();
            builder.append(table.get(i));
            end = i;
            if(i != start){
                int j = i;
                do{
                    j = dist[j];
                    builder.append(table.get(j));
                }while(start != j);
                System.out.println("least-cost path to node "+table.get(end)+": "+builder.reverse().toString() + " and the cost is " + length[i]);
            }
        }
    }

    /**
     * Dijkstra接口
     * @param id　起点的id
     */

    public void dijkstra(String id) {
        TreeSet<String> treeSet = new TreeSet<>();  //得到所有节点
        for (GraphEdge edge:graphEdges){
            treeSet.add(edge.getStart());
            treeSet.add(edge.getEnd());
        }
        nodesNumber = treeSet.size();
        Map<String,Integer> map = new TreeMap<>();      //把节点转化为数字方便操作
        Map<Integer,String> table = new TreeMap<>();    //把数字转回节点用来输出
        int count = 0;
        for (String s:treeSet){                 //初始化节点数字与对应关系
            map.put(s,count); table.put(count,s);
            count++;
        }
        matrix = new int[nodesNumber][nodesNumber];     //初始化临接矩阵
        for (GraphEdge edge:graphEdges){
            matrix[map.get(edge.getStart())][map.get(edge.getEnd())] = edge.getLength();
            matrix[map.get(edge.getEnd())][map.get(edge.getStart())] = edge.getLength();
        }
        mark = new int[nodesNumber];
        int[] dist = new int[nodesNumber];
        int[] length = new int[nodesNumber];
        int start = map.get(id);
        calculator(start,length,dist);
        printfInfo(dist,length,table,start);
    }
}

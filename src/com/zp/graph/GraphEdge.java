/*
 * GraphEdge
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.graph;

import java.io.Serializable;

/**
 * 图的边类
 * 实现了序列化接口
 */
public class GraphEdge implements Serializable {
    /**
     * 起点
     */
    private String start;
    /**
     * 终点
     */
    private String end;
    /**
     * 长度
     */
    private int length;

    /**
     * 构造函数
     * @param start　边的起点
     * @param end　边的终点
     * @param length　边的长度
     */
    public GraphEdge(String start, String end, int length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }

    /**
     * 获得边的起点
     * @return String
     */
    public String getStart() {
        return start;
    }

    /**
     * 返回边的终点
     * @return String
     */
    public String getEnd() {
        return end;
    }

    /**
     * 返回边的长度
     * @return int
     */
    public int getLength() {
        return length;
    }

    /**
     * 两边的长度相同，并且起点和终点的相等即可
     * @param obj 比较对象
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == GraphEdge.class) {
            if(((GraphEdge) obj).getLength() == this.getLength()){
                return (((GraphEdge) obj).getStart().equals(this.start) && ((GraphEdge) obj).getEnd().equals(this.end)) || (((GraphEdge) obj).getStart().equals(this.end) && ((GraphEdge) obj).getEnd().equals(this.start));
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * 将一条边输出，格式为(start,end,length)
     * @return String
     */
    @Override
    public String toString() {
        return "("+start+","+end+","+length+")";
    }
}

/*
 * TimedUpdatePackage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.protocol.data;

import com.zp.graph.GraphEdge;

import java.io.Serializable;
import java.util.LinkedList;

public class TimedUpdatePackage extends Package implements Serializable {
    private LinkedList<GraphEdge> graphEdges;

    public TimedUpdatePackage(LinkedList<GraphEdge> graphEdges){
        this.graphEdges = graphEdges;
    }

    public LinkedList<GraphEdge> getGraphEdges() {
        return graphEdges;
    }
}

/*
 * NodeData
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.node;

import com.zp.graph.GraphEdge;

import java.util.LinkedList;

public interface ReadNodeData {

    String getId();

    int getPort();

    LinkedList<NeighNode> getNeighNodes();

    LinkedList<GraphEdge> getGraphEdgeLinkedList();

    LinkedList<GraphEdge> getGraphEdges();
}
